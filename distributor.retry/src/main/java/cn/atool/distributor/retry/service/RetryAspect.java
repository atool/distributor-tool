package cn.atool.distributor.retry.service;

import cn.atool.distributor.retry.annotation.Retry;
import cn.atool.distributor.retry.exception.RetryException;
import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.retry.service.base.RetryHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 重试方法切面
 *
 * @author darui.wu
 * @create 2020/1/8 3:27 下午
 */
@Slf4j
@Aspect
public class RetryAspect extends ApplicationObjectSupport {
    @Pointcut("@annotation(retry)")
    public void retryPointcut(Retry retry) {
    }

    /**
     * 重试处理逻辑
     *
     * @param pjp   接入点
     * @param retry 重试注解对象
     * @return 目标方法的执行结果
     * @throws Throwable 底层异常
     */
    @Around(value = "retryPointcut(retry)", argNames = "pjp,retry")
    public Object retryProceed(ProceedingJoinPoint pjp, Retry retry) throws Throwable {
        try {
            int localRetry = 0;
            int maxRetry = Math.max(0, retry.localMaxRetry());
            while (localRetry <= maxRetry) {
                localRetry++;
                try {
                    return pjp.proceed();
                } catch (Throwable e) {
                    if (!needRetry(retry, e) || localRetry > maxRetry) {
                        throw e;
                    }
                }
            }
            throw new RetryException("Internal logic error, execution process should not arrive here!");
        } catch (Throwable e) {
            this.recordRetry(pjp, retry, e);
            throw e;
        }
    }

    /**
     * 是否需要重试
     *
     * @param retry
     * @param e
     * @return
     */
    public static boolean needRetry(Retry retry, Throwable e) {
        boolean inInclude = retry.includes() == null || retry.includes().length == 0;
        boolean notExclude = retry.excludes() == null || retry.excludes().length == 0;
        if (!inInclude) {
            for (Class<Throwable> klass : retry.includes()) {
                if (e.getClass().isAssignableFrom(klass)) {
                    inInclude = true;
                    break;
                }
            }
        }
        if (!notExclude) {
            for (Class<Throwable> klass : retry.excludes()) {
                if (e.getClass().isAssignableFrom(klass)) {
                    break;
                }
            }
            notExclude = true;
        }
        return inInclude && notExclude;
    }

    /**
     * 记录重试事件
     *
     * @param pjp
     * @param retry
     * @param e
     */
    private void recordRetry(ProceedingJoinPoint pjp, Retry retry, Throwable e) {
        if (retry.asyncMaxRetry() <= 0 || !needRetry(retry, e)) {
            return;
        }

        RetryBody body = RetryHelper.buildRetryBody(pjp, retry);
        RetryPersistence handler = this.findPersistence(retry);
        if (handler == null) {
            throw new RetryException(body, e);
        }
        handler.save(body, retry.asyncMaxRetry(), e);
    }

    private RetryPersistence findPersistence(Retry retry) {
        try {
            RetryPersistence persistence = (RetryPersistence) getApplicationContext().getBean(retry.persistence());
            return persistence;
        } catch (Throwable e) {
            log.warn("findHandler error: {}", e.getMessage(), e);
            throw new RetryException(e);
        }
    }
}
