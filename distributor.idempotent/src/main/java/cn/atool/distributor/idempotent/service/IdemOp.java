package cn.atool.distributor.idempotent.service;

import cn.atool.distributor.function.SupplierWithThrowable;
import cn.atool.distributor.idempotent.model.IdemBody;
import cn.atool.distributor.idempotent.model.IdemStatus;
import cn.atool.distributor.serialize.SerializeFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 幂等操作上下文
 *
 * @author darui.wu
 * @create 2020/2/11 11:54 上午
 */
@Slf4j
@Getter
public class IdemOp {
    private final IdemPersistence persistence;

    private final IdemBody body;

    private IdemOp(IdemPersistence persistence, IdemBody body) {
        if (persistence == null) {
            throw new RuntimeException("the idempotent persistence can't be null.");
        }
        if (body == null) {
            throw new RuntimeException("the idempotent body can't be null.");
        }
        this.persistence = persistence;
        this.body = body;
    }

    private static ThreadLocal<IdemOp> idemContextThreadLocal = new ThreadLocal<>();

    /**
     * 设置幂等操作上下文
     *
     * @param persistence
     * @param body
     */
    static void setContext(IdemPersistence persistence, IdemBody body) {
        idemContextThreadLocal.set(new IdemOp(persistence, body));
    }

    /**
     * 手动将操作包含在幂等处理中，如下编码, 入参和出参只是示例（如果需要，可以把IdemOp.doIdempotent放到事务中一起控制）:
     * <pre>
     *     @ Idempotent(key = "key ognl表达式", auto = false)
     *     public String exampleFunction(String input) throws Throwable {
     *         // 前置操作
     *         String result = IdemOp.doIdempotent(() -> {
     *             //执行业务逻辑
     *             return "output";
     *         });
     *         // 后置操作
     *         return result;
     *     }
     * </pre>
     *
     * @param supplier 执行具体业务逻辑
     * @param <T>
     * @return 具体业务逻辑返回值
     * @throws Throwable
     */
    public static <T> T doIdempotent(SupplierWithThrowable<T> supplier) throws Throwable {
        if (idemContextThreadLocal.get() == null) {
            throw new RuntimeException("the idempotent context can't be null, please define the @Idempotent method first.");
        }
        IdemBody body = idemContextThreadLocal.get().getBody();
        IdemPersistence persistence = idemContextThreadLocal.get().getPersistence();
        persistence.begin(body);
        try {
            T result = supplier.get();
            persistence.commit(body, result);
            return result;
        } catch (Throwable e) {
            log.error("Do Idempotent Error : {}, rollback idempotent.", SerializeFactory.protocol(body.getProtocol()).toString(body), e);
            persistence.delete(body, IdemStatus.ROLLBACK);
            throw e;
        } finally {
            idemContextThreadLocal.remove();
        }
    }
}