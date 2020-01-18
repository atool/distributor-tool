package cn.atool.distributor.idempotent.service;

import java.util.List;

/**
 * @Descriotion: 对幂等操作进行报警处理
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/23.
 */
public interface IdempotentAlert {
    /**
     * 查找超时（timeout）时长的业务
     *
     * @param timeout 超时报警时长
     * @param limit
     * @return
     */
    List findTimeout(int timeout, int limit);

    /**
     * 查找连续回滚times次的业务
     *
     * @param times
     * @param limit
     * @return
     */
    List findRollback(int times, int limit);
}
