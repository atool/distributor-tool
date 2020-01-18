package cn.atool.distributor.retry.service.base;

import cn.atool.distributor.ognl.KeyGenerator;
import cn.atool.distributor.retry.annotation.Retry;
import cn.atool.distributor.retry.model.MethodArg;
import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.serialize.SerializeFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * @author darui.wu
 * @create 2020/1/8 3:40 下午
 */
public class RetryHelper {
    /**
     * 重试信息构建
     *
     * @param targetBean 重试目标bean
     * @param pjp        切面
     * @param retry      重试注解
     * @return
     */
    public static RetryBody buildRetryBody(String targetBean, ProceedingJoinPoint pjp, Retry retry) {
        String retryKey = KeyGenerator.key(retry.retryKey(), pjp);

        List<MethodArg> args = getMethodArgs(retry.protocol(), pjp);
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        String targetClass = ms.getDeclaringTypeName();
        String targetMethod = ms.getName();
        RetryBody body = new RetryBody(targetBean, targetClass, targetMethod, args, retry.protocol());
        body.setRetryKey(retryKey);
        return body;
    }

    /**
     * 获取参数名称和对应参数值
     *
     * @param protocol
     * @param pjp
     * @return
     */
    static List<MethodArg> getMethodArgs(String protocol, ProceedingJoinPoint pjp) {
        MethodSignature signature = ((MethodSignature) pjp.getSignature());

        String[] paramNames = signature.getParameterNames();
        Class[] paraTypes = signature.getParameterTypes();
        Object[] args = pjp.getArgs();
        if (paramNames == null || args == null) {
            return new ArrayList<>();
        }
        List<MethodArg> list = new ArrayList<>();
        for (int i = 0; i < paramNames.length; i++) {
            MethodArg arg = new MethodArg()
                    .setType(paraTypes[i].getName())
                    .setValue(SerializeFactory.protocol(protocol).toString(args[i]));
            list.add(arg);
        }
        return list;
    }
}
