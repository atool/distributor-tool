package cn.atool.distributor.retry.model;

/**
 * 重试消息状态
 *
 * @author darui.wu
 * @create 2020/1/8 4:35 下午
 */
public interface RetryStatus {
    String FAILURE = "failure";

    String FINISH = "success";

    String EXCEED = "exceed";
}
