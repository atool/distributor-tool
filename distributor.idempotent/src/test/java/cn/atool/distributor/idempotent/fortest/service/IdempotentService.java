package cn.atool.distributor.idempotent.fortest.service;

import cn.atool.distributor.idempotent.annotation.Idempotent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author darui.wu
 * @create 2019/12/20 4:00 下午
 */
@Service
public class IdempotentService {
    @Idempotent(key = "Key_${first.name}_${second[0]}")
    public Map<String, String> doSomeThing(Map<String, String> first, List<String> second) {
        Map<String, String> map = new HashMap<>();
        map.put("test", "test");
        return map;
    }

    @Idempotent(key = "Key_${input}")
    public void noneReturn(String input) {
    }

    @Idempotent(key = "Key_${input}")
    public void doRollback(String input) {
        throw new RuntimeException("roll back");
    }

    @Idempotent(key = "Key_${input}")
    public String doExpired(String input) {
        return "test2";
    }
}
