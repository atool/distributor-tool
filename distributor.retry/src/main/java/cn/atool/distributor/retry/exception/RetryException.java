package cn.atool.distributor.retry.exception;

import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.serialize.SerializeFactory;

/**
 * 重试异常
 *
 * @author darui.wu
 * @create 2020/1/8 4:25 下午
 */
public class RetryException extends RuntimeException {
    public RetryException(RetryBody body) {
        super("RetryBody:" + SerializeFactory.defaultProtocol().toString(body));
    }

    public RetryException(Throwable e) {
        super(e);
    }

    public RetryException(String error) {
        super(error);
    }

    public RetryException(RetryBody body, Throwable e) {
        super("RetryBody:" + SerializeFactory.defaultProtocol().toString(body), e);
    }

    public RetryException(String error, RetryBody body) {
        super("RetryBody:" + SerializeFactory.defaultProtocol().toString(body) + ", error:" + error);
    }
}
