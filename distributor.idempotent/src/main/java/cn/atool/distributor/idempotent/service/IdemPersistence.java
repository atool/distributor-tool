package cn.atool.distributor.idempotent.service;

import cn.atool.distributor.idempotent.model.IdemBody;
import cn.atool.distributor.idempotent.model.IdemValue;
import cn.atool.distributor.idempotent.exception.IdemConcurrentException;
import cn.atool.distributor.idempotent.exception.IdemLockException;

/**
 * @Descriotion: 幂等持久化处理过程，需要业务系统自己实现幂等机制
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/19.
 */
public interface IdemPersistence {
    /**
     * 是否已存在幂等调用
     * o 如果已经幂等执行过了，返回幂等执行结果
     * o 如果没有执行过幂等，或者前一个幂等操作超时了，则返回null对象
     * o 如果前一个幂等操作还在执行中，并且未超时，则抛出出幂等并发异常
     *
     * @param body 幂等信息
     * @return 返回幂等结果值
     * @throws IdemConcurrentException 幂等并发执行异常
     */
    IdemValue existIdempotent(IdemBody body) throws IdemConcurrentException;

    /**
     * @param body
     * @param result
     * @return
     */
    default Object strategy(IdemBody body, Object result) {
        return result;
    }

    /**
     * 开始执行幂等操作
     *
     * @param body
     * @throws IdemLockException 获得幂等锁异常
     */
    void begin(IdemBody body) throws IdemLockException;

    /**
     * 逻辑删除幂等信息, 表示业务逻辑方法执行失败
     *
     * @param body
     * @param status 逻辑删除后的状态
     * @throws Throwable
     */
    void delete(IdemBody body, String status);

    /**
     * 提交幂等信息, 表示业务逻辑已经执行成功
     *
     * @param body
     * @param value
     * @throws Throwable
     */
    void commit(IdemBody body, Object value);
}
