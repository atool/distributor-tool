package cn.atool.distributor.retry.service;

import cn.atool.distributor.retry.model.RetryBody;

import java.util.List;
import java.util.function.Consumer;

/**
 * 外部重试实现
 *
 * @author darui.wu
 * @create 2020/1/10 3:30 下午
 */
public interface RetryHandler {
    /**
     * 执行单个重试任务
     *
     * @param category     重试事件类型
     * @param retryKey     重试事件键值
     * @param retryHandler
     * @param <T>
     */
    default <T> void doRetry(String category, String retryKey, Consumer<Object[]> retryHandler) {
        RetryBody body = this.getRetryPersistence().findRetry(category, retryKey);
        this.doRetry(body, retryHandler);
    }

    /**
     * 批量执行重试任务
     *
     * @param category     重试事件类型
     * @param startId
     * @param retryHandler
     * @param <T>
     * @return
     */
    default <T> long doRetry(String category, int startId, Consumer<Object[]> retryHandler) {
        List<RetryBody> list = this.getRetryPersistence().findRetry(category, startId);
        long baseId = 0L;
        for (RetryBody body : list) {
            baseId = Math.max(baseId, body.getId());
            this.doRetry(body, retryHandler);
        }
        return baseId;
    }

    /**
     * 单个执行重试任务
     *
     * @param body         重试消息体
     * @param retryHandler 重试方法调用
     * @param <T>
     */
    <T> void doRetry(RetryBody body, Consumer<Object[]> retryHandler);

    /**
     * 返回持久化处理器
     *
     * @return
     */
    RetryPersistence getRetryPersistence();
}
