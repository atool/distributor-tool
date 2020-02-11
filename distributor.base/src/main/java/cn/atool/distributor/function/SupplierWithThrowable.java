package cn.atool.distributor.function;

/**
 * 带异常的Supplier
 *
 * @author darui.wu
 * @create 2020/2/11 1:49 下午
 */
@FunctionalInterface
public interface SupplierWithThrowable<T> {
    /**
     * Gets a result.
     *
     * @return
     * @throws Throwable
     */
    T get() throws Throwable;
}