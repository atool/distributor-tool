package cn.atool.distributor.idempotent.exception;

import cn.atool.distributor.idempotent.model.IdemBody;

/**
 * 幂等并发执行异常
 *
 * @author darui.wu
 * @create 2019/12/23 7:46 下午
 */
public class IdemConcurrentException extends RuntimeException {
    public IdemConcurrentException(IdemBody body) {
        super(String.format("the task[type=%s, key=%s] is in progress. Please try again later.", body.getType(), body.getKey()));
    }
}
