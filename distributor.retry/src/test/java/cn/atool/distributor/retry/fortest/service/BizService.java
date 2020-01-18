package cn.atool.distributor.retry.fortest.service;

import java.util.List;
import java.util.Map;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/1/13.
 */
public interface BizService {
    /**
     * 示例方法
     *
     * @param arg1
     * @param arg2
     * @return
     */
    Map<String, String> doSomeThing(String requestId, Integer arg1, List<String> arg2);

    /**
     * doSomeThing
     *
     * @param arg1
     * @param arg2
     * @return
     */
    Map<String, String> doSomeThing(Integer arg1, List<String> arg2);
}
