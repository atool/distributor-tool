package cn.atool.distributor.retry.service;

import cn.atool.distributor.retry.model.RetryBody;

import java.util.List;
import java.util.function.BiConsumer;

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
     * @param targetBean
     * @param targetMethod
     * @param retryKey
     * @param successCallback
     * @param <T>
     */
    default <T> void doRetry(String targetBean, String targetMethod, String retryKey, BiConsumer<T, Object[]> successCallback) {
        RetryBody body = this.getRetryPersistence().findRetry(targetBean, targetMethod, retryKey);
        this.doRetry(body, successCallback);
    }

    /**
     * 批量执行重试任务
     *
     * @param targetBean
     * @param targetMethod
     * @param startId
     * @param successCallback
     * @param <T>
     * @return
     */
    default <T> long doRetry(String targetBean, String targetMethod, int startId, BiConsumer<T, Object[]> successCallback) {
        List<RetryBody> list = this.getRetryPersistence().findRetry(targetBean, targetMethod, startId);
        long baseId = 0L;
        for (RetryBody body : list) {
            baseId = Math.max(baseId, body.getId());
            this.doRetry(body, successCallback);
        }
        return baseId;
    }

    /**
     * 单个执行重试任务
     *
     * @param body            重试消息体
     * @param successCallback 重试成功后的回调
     * @param <T>
     */
    <T> void doRetry(RetryBody body, BiConsumer<T, Object[]> successCallback);

    /**
     * 返回持久化处理器
     *
     * @return
     */
    RetryPersistence getRetryPersistence();
}
