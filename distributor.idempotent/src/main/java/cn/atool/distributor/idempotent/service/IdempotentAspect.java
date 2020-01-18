package cn.atool.distributor.idempotent.service;

import cn.atool.distributor.idempotent.model.IdemBody;
import cn.atool.distributor.idempotent.model.IdemValue;
import cn.atool.distributor.idempotent.annotation.Idempotent;
import cn.atool.distributor.idempotent.model.IdemStatus;
import cn.atool.distributor.ognl.KeyGenerator;
import cn.atool.distributor.serialize.SerializeFactory;
import cn.atool.distributor.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 幂等处理切面
 *
 * @author darui.wu
 * @create 2019/12/19 4:42 下午
 */
@Slf4j
@Aspect
public class IdempotentAspect extends ApplicationObjectSupport {
    @Pointcut("@annotation(idempotent)")
    public void idempotentPointcut(Idempotent idempotent) {
    }

    /**
     * 幂等处理逻辑
     *
     * @param pjp        接入点
     * @param idempotent 幂等注解对象
     * @return 目标方法的执行结果
     * @throws Throwable 底层异常
     */
    @Around(value = "idempotentPointcut(idempotent)", argNames = "pjp,idempotent")
    public Object idempotentProceed(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        IdemPersistence persistence = this.findIdempotentPersistence(idempotent);
        IdemBody body = this.buildIdempotentBody(pjp, idempotent);
        IdemValue idemValueObject = persistence.existIdempotent(body);
        if (idemValueObject != null) {
            return persistence.strategy(body, idemValueObject.getValue());
        }
        persistence.begin(body);
        try {
            Object result = pjp.proceed();
            persistence.commit(body, result);
            return result;
        } catch (Exception e) {
            log.error("Do Idempotent Error : {}, rollback idempotent.", SerializeFactory.protocol(idempotent.protocol()).toString(pjp.getArgs()), e);
            persistence.delete(body, IdemStatus.ROLLBACK);
            throw e;
        }
    }

    /**
     * 查找幂等持久处理对象
     *
     * @param idempotent
     * @return
     */
    private IdemPersistence findIdempotentPersistence(Idempotent idempotent) {
        try {
            IdemPersistence persistence = (IdemPersistence) getApplicationContext().getBean(idempotent.persistence());
            return persistence;
        } catch (Throwable e) {
            log.error("findIdempotentPersistence error: {}", e.getMessage(), e);
            throw new RuntimeException("findIdempotentPersistence error", e);
        }
    }

    /**
     * 幂等信息构建
     *
     * @param pjp
     * @param idempotent
     * @return
     */
    private IdemBody buildIdempotentBody(ProceedingJoinPoint pjp, Idempotent idempotent) {
        IdemBody body = new IdemBody(idempotent);
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        body.setType(StringHelper.isBlank(idempotent.type()) ? ms.getName() : idempotent.type());
        body.setValueType(ms.getReturnType());
        try {
            String key = KeyGenerator.key(idempotent.key(), pjp);
            body.setKey(key);
        } catch (Exception e) {
            log.error("buildIdempotentBody error", e);
            throw new RuntimeException("can't build idempotent info by method: " + pjp.getSignature().getName(), e);
        }
        return body;
    }
}