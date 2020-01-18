package cn.atool.distributor.retry.service.base;

import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.retry.service.RetryHandler;
import cn.atool.distributor.retry.service.RetryPersistence;
import cn.atool.distributor.retry.exception.RetryException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ApplicationObjectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 异步重试实现
 *
 * @author darui.wu
 * @create 2020/1/10 3:32 下午
 */
@Accessors(chain = true)
@Slf4j
public class RetryHandlerImpl extends ApplicationObjectSupport implements RetryHandler {
    @Setter
    @Getter
    private RetryPersistence retryPersistence;

    @Override
    public <T> void doRetry(RetryBody body, BiConsumer<T, Object[]> successCallback) {
        Object target = super.getApplicationContext().getBean(body.getTargetBean());
        Method method = this.getMethodFromCached(target, body);
        Object[] args = body.toObjects();
        try {
            Object o = method.invoke(target, args);
            retryPersistence.closeRetry(body);
            if (successCallback != null) {
                successCallback.accept((T) o, body.toObjects());
            }
        } catch (IllegalAccessException e) {
            log.error("doRetry Error:" + e.getMessage(), e);
            throw new RetryException(body, e);
        } catch (InvocationTargetException e) {
            log.error("doRetry Error:" + e.getMessage(), e);
            throw new RetryException(body, e);
        } catch (RuntimeException e) {
            log.error("doRetry Error:" + e.getMessage(), e);
            throw e;
        }
    }

    static final Map<String, Method> METHOD_CACHED = new HashMap<>();

    private Method getMethodFromCached(Object target, RetryBody body) {
        String signature = body.getTargetClass() + "#" + body.getTargetMethod() + body.getSignature();
        if (!METHOD_CACHED.containsKey(signature)) {
            synchronized (METHOD_CACHED) {
                Method method = this.getMethod(target, body);
                METHOD_CACHED.put(signature, method);
            }
        }
        return METHOD_CACHED.get(signature);
    }

    private Method getMethod(Object target, RetryBody body) {
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (!method.getName().equals(body.getTargetMethod())) {
                continue;
            }
            Class<?>[] types = method.getParameterTypes();
            if (types.length != body.getArgs().size()) {
                continue;
            }
            boolean matched = true;
            for (int i = 0; i < types.length; i++) {
                if (!types[i].getName().equals(body.getArgs().get(i).getType())) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return method;
            }
        }
        throw new RetryException("not found method", body);
    }
}
