package cn.atool.distributor.idempotent.fortest.service;

import cn.atool.distributor.idempotent.annotation.Idempotent;
import cn.atool.distributor.idempotent.service.IdemOp;
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

    @Idempotent(type = "IdemInManual", key = "Key-${input}", auto = false)
    public String doIdemInManual(String input) throws Throwable {
        // 前置操作
        String result = IdemOp.doIdempotent(() -> {
            //执行业务逻辑
            if ("failure".equals(input)) {
                throw new RuntimeException("failure");
            } else {
                return "manual";
            }
        });
        // 后置操作
        return result;
    }
}
