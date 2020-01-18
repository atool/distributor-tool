package cn.atool.distributor.idempotent.exception;

import cn.atool.distributor.idempotent.model.IdemBody;

/**
 * 幂等锁异常
 *
 * @author darui.wu
 * @create 2019/12/23 7:39 下午
 */
public class IdemLockException extends RuntimeException {
    public IdemLockException(IdemBody body) {
        super(String.format("Idempotent[type=%s, key=%s] lock failed.", body.getType(), body.getKey()));
    }
}
