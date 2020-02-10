package cn.atool.distributor.retry.service.base;

import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.retry.service.RetryHandler;
import cn.atool.distributor.retry.service.RetryPersistence;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.function.Consumer;

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
    public void doRetry(RetryBody body, Consumer<Object[]> retryHandler) {
        Object[] args = body.toObjects();
        try {
            retryHandler.accept(args);
            retryPersistence.closeRetry(body);
        } catch (RuntimeException e) {
            log.error("doRetry Error:" + e.getMessage(), e);
            throw e;
        }
    }
}
