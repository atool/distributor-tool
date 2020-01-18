package cn.atool.distributor.idempotent.model;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/20.
 */
public interface IdemStatus {
    /**
     * 幂等操作正在执行过程中
     */
    String BEGIN = "begin";
    /**
     * 幂等操作执行失败，已经被回滚
     */
    String ROLLBACK = "rollback";
    /**
     * 幂等操作执行成功
     */
    String COMMIT = "commit";
    /**
     * 超时删除
     */
    String TIME_OUT = "timeout";
}
