package cn.atool.distributor.retry.fortest.service;

import cn.atool.distributor.retry.annotation.Retry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author darui.wu
 * @create 2020/1/13 10:53 上午
 */
@Component
public class BizServiceImpl implements BizService {
    @Retry(retryKey = "${requestId}")
    @Override
    public Map<String, String> doSomeThing(String requestId, Integer arg1, List<String> arg2) {
        return this.doSomeThing(arg1, arg2);
    }

    @Retry
    @Override
    public Map<String, String> doSomeThing(Integer arg1, List<String> arg2) {
        throw new RuntimeException("test exception");
    }
}
