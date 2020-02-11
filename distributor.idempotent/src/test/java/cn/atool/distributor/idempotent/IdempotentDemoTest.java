package cn.atool.distributor.idempotent;

import cn.atool.distributor.idempotent.exception.IdemConcurrentException;
import cn.atool.distributor.idempotent.fortest.SpringTestConfig;
import cn.atool.distributor.idempotent.fortest.datamap.IdempotentTableMap;
import cn.atool.distributor.idempotent.model.IdemStatus;
import cn.atool.distributor.idempotent.fortest.datamap.IdempotentMP;
import cn.atool.distributor.idempotent.fortest.service.IdempotentService;
import cn.atool.distributor.serialize.SerializeProtocol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.junit5.Test4J;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author darui.wu
 * @create 2019/12/20 4:08 下午
 */
@ContextConfiguration(classes = {
        SpringTestConfig.class
})
public class IdempotentDemoTest extends Test4J {
    final String Idem_Table = IdempotentMP.Table_Name;

    @Autowired
    private IdempotentService service;

    @Test
    public void test_normal() {
        db.table(Idem_Table).clean();
        Map<String, String> result = service.doSomeThing(new HashMap<String, String>() {
            {
                this.put("name", "demo");
            }
        }, Arrays.asList("one", "two"));

        want.map(result).eqHashMap(new HashMap<String, String>() {
            {
                this.put("test", "test");
            }
        });
        db.table(Idem_Table).query().eqDataMap(IdempotentTableMap.create(1)
                .idem_type.values("doSomeThing")
                .idem_key.values("Key_demo_one")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values("{\"test\":\"test\"}")
                .expire_time.values(31536000)
                .idem_status.values(IdemStatus.COMMIT)
        );
    }

    @Test
    public void test__manual_normal() throws Throwable {
        db.table(Idem_Table).clean();
        String result = service.doIdemInManual("test_manual_input");
        want.string(result).eq("manual");
        db.table(Idem_Table).query().eqDataMap(IdempotentTableMap.create(1)
                .idem_type.values("IdemInManual")
                .idem_key.values("Key-test_manual_input")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values("\"manual\"")
                .expire_time.values(31536000)
                .idem_status.values(IdemStatus.COMMIT)
        );
    }

    @Test
    public void test__manual_normal_failure() throws Throwable {
        db.table(Idem_Table).clean();
        Assertions.assertThrows(RuntimeException.class, () -> service.doIdemInManual("failure"));
        db.table(Idem_Table).query().eqDataMap(IdempotentTableMap.create(1)
                .idem_type.values("IdemInManual")
                .idem_key.values("Key-failure")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values((Object) null)
                .expire_time.values(31536000)
                .idem_status.values(IdemStatus.ROLLBACK)
        );
    }

    @Test
    public void test_noneReturn() {
        db.table(Idem_Table).clean();
        service.noneReturn("myworld");
        db.table(Idem_Table).query().eqDataMap(IdempotentTableMap.create(1)
                .idem_type.values("noneReturn")
                .idem_key.values("Key_myworld")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values("null")
                .expire_time.values(31536000)
                .idem_status.values(IdemStatus.COMMIT)
        );
    }

    @Test
    public void test_rollback() {
        db.table(Idem_Table).clean();
        assertThrows(RuntimeException.class, () -> service.doRollback("myworld"));
        db.table(Idem_Table).query().eqDataMap(IdempotentTableMap.create(1)
                .idem_type.values("doRollback")
                .idem_key.values("Key_myworld")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values((Object) null)
                .expire_time.values(31536000)
                .idem_status.values(IdemStatus.ROLLBACK)
        );
    }

    @Test
    public void test_expired() throws InterruptedException {
        db.table(Idem_Table).clean().insert(IdempotentTableMap.create(1)
                .init()
                .idem_type.values("doExpired")
                .idem_key.values("Key_myworld")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values((Object) "\"test2\"")
                .expire_time.values(1)
                .idem_status.values(IdemStatus.BEGIN)
                .is_deleted.values(0)
        );
        Thread.sleep(1001L);
        String result = service.doExpired("myworld");
        want.string(result).eq("test2");
        db.table(Idem_Table).query().eqDataMap(IdempotentTableMap.create(2)
                        .idem_type.values("doExpired")
                        .idem_key.values("Key_myworld")
                        .protocol.values(SerializeProtocol.FAST_JSON)
                        .idem_value.values("\"test2\"", "\"test2\"")
                        .expire_time.values(31536000)
                        .idem_status.values(IdemStatus.TIME_OUT, IdemStatus.COMMIT)
                , EqMode.IGNORE_ORDER);
    }

    @Test
    void test_concurrent() {
        db.table(Idem_Table).clean().insert(IdempotentTableMap.create(1)
                .init()
                .idem_type.values("doExpired")
                .idem_key.values("Key_myworld")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .idem_value.values((Object) "\"test2\"")
                .expire_time.values(10000)
                .idem_status.values(IdemStatus.BEGIN)
                .is_deleted.values(0)
        );
        assertThrows(IdemConcurrentException.class, () -> service.doExpired("myworld"));
    }
}