package cn.atool.distributor.retry.test;

import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.retry.model.RetryStatus;
import cn.atool.distributor.retry.service.RetryHandler;
import cn.atool.distributor.serialize.SerializeProtocol;
import cn.atool.distributor.retry.fortest.SpringTestConfig;
import cn.atool.distributor.retry.fortest.datamap.RetryEventMP;
import cn.atool.distributor.retry.fortest.datamap.RetryEventTableMap;
import cn.atool.distributor.retry.fortest.service.BizService;
import cn.atool.distributor.retry.fortest.service.BizServiceImpl;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.junit5.Test4J;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author darui.wu
 * @create 2020/1/13 10:56 上午
 */
@ContextConfiguration(classes = {
        SpringTestConfig.class
})
public class RetryEventTest extends Test4J {
    static final String TABLE_RETRY_EVENT = RetryEventMP.Table_Name;
    @Autowired
    private BizService bizService;

    @Autowired
    private RetryHandler retryHandler;

    @Test
    void test_run_method1_exception() {
        db.table(TABLE_RETRY_EVENT).clean();
        assertThrows(RuntimeException.class, () -> bizService.doSomeThing("request1", 11, Arrays.asList("list1", "list2")));
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod_3)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(0)
        );
    }

    @Test
    void retry_method1_again_fail() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod_3)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(0)
        );
        assertThrows(RuntimeException.class, () -> bizService.doSomeThing("request1", 11, Arrays.asList("list1", "list2")));
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod_3)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(1)
        );
    }

    @Test
    void retry_method1_again_fail2() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod_3)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(2)
        );
        db.table(TABLE_RETRY_EVENT).query().print();
        assertThrows(RuntimeException.class, () -> this.retryHandler.doRetry("doSomeThing_3", "request1", (args) -> {
            bizService.doSomeThing((String) args[0], (Integer) args[1], (List<String>) args[2]);
        }));
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod_3)
                .retry_status.values(RetryStatus.EXCEED)
                .has_retry.values(3)
        );
    }

    @Test
    void retry_method1_again_success() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod_3)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(2)
        );
        new MockUp<BizServiceImpl>() {
            @Mock
            public Map<String, String> doSomeThing(Integer arg1, List<String> arg2) {
                return new HashMap<>();
            }
        };
        this.retryHandler.doRetry("doSomeThing_3", "request1", (args) -> {
            want.array(args).eqReflect(new Object[]{"request1", 11, Arrays.asList("list1", "list2")});
            Map result = this.bizService.doSomeThing((String) args[0], (Integer) args[1], (List<String>) args[2]);
            want.map(result).notNull().sizeEq(0);
        });
        db.table(TABLE_RETRY_EVENT).query()
                .eqDataMap(new RetryEventTableMap(1)
                        .with(this::setMethod_3)
                        .retry_status.values(RetryStatus.FINISH)
                        .has_retry.values(3)
                );
    }

    RetryEventTableMap setMethod_3(RetryEventTableMap map) {
        return map.retry_category.values("doSomeThing_3")
                .retry_key.values("request1")
                .method_signature.values("(java.lang.String,java.lang.Integer,java.util.List)")
                .max_retry.values(3)
                .err_message.values("java.lang.RuntimeException#test exception")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .method_args.values("[{\"type\":\"java.lang.String\",\"value\":\"\\\"request1\\\"\"},{\"type\":\"java.lang.Integer\",\"value\":\"11\"},{\"type\":\"java.util.List\",\"value\":\"[\\\"list1\\\",\\\"list2\\\"]\"}]")
                .has_retry.values(0)
                .is_deleted.values(0);
    }

    @Test
    void test_run_method2_exception() {
        db.table(TABLE_RETRY_EVENT).clean();
        assertThrows(RuntimeException.class, () -> bizService.doSomeThing(11, Arrays.asList("list1", "list2")));

        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod2)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(0)
        );
    }

    @Test
    void retry_method2_success() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod2)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(0)
        );
        new MockUp<BizServiceImpl>() {
            @Mock
            public Map<String, String> doSomeThing(Integer arg1, List<String> arg2) {
                return new HashMap<>();
            }
        };
        this.retryHandler.doRetry("doSomeThing_2", "3nnat27fk7zocfvtxiupufmg4", (args) -> {
            want.array(args).eqReflect(new Object[]{11, Arrays.asList("list1", "list2")});
            Map result = bizService.doSomeThing((Integer) args[0], (List<String>) args[1]);
            want.map(result).notNull().sizeEq(0);
        });
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod2)
                .retry_status.values(RetryStatus.FINISH)
                .has_retry.values(1)
        );
    }


    RetryEventTableMap setMethod2(RetryEventTableMap map) {
        return map.retry_category.values("doSomeThing_2")
                .retry_key.values("3nnat27fk7zocfvtxiupufmg4")
                .method_signature.values("(java.lang.Integer,java.util.List)")
                .max_retry.values(3)
                .err_message.values("java.lang.RuntimeException#test exception")
                .protocol.values(SerializeProtocol.FAST_JSON)
                .method_args.values("[{\"type\":\"java.lang.Integer\",\"value\":\"11\"},{\"type\":\"java.util.List\",\"value\":\"[\\\"list1\\\",\\\"list2\\\"]\"}]")
                .has_retry.values(0)
                .is_deleted.values(0)
                ;
    }

    @Test
    public void batch_retry() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod_3)
                .id.values(1)
                .retry_status.values(RetryStatus.FAILURE)
        ).insert(new RetryEventTableMap(1).init()
                .with(this::setMethod2)
                .id.values(2)
                .retry_status.values(RetryStatus.FAILURE)
        );
        new MockUp<BizServiceImpl>() {
            @Mock
            public Map<String, String> doSomeThing(Integer arg1, List<String> arg2) {
                return new HashMap<>();
            }
        };
        this.retryHandler.doRetry("doSomeThing_3", 0, (args) -> {
            this.bizService.doSomeThing((String) args[0], (Integer) args[1], (List<String>) args[2]);
        });
        this.retryHandler.doRetry("doSomeThing_2", 0, (args) -> {
            this.bizService.doSomeThing((Integer) args[0], (List<String>) args[1]);
        });
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(2)
                .retry_category.values("doSomeThing_3", "doSomeThing_2")
                .retry_status.values(RetryStatus.FINISH)
                .has_retry.values(1)
                .retry_key.values("request1", "3nnat27fk7zocfvtxiupufmg4")
        );
    }

    @Test
    public void summary_retry() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(5).init()
                .retry_category.values("method1", "method2", "method1", "method2", "method1")
                .retry_status.values(RetryStatus.FAILURE, RetryStatus.FAILURE, RetryStatus.FINISH, RetryStatus.FAILURE)
                .is_deleted.values(0)
        );

        List<RetryBody> bodies = this.retryHandler.getRetryPersistence().summaryRetry(RetryStatus.FAILURE);
        want.list(bodies).eqDataMap(new DataMap(2)
                .kv("retryCategory", "method1", "method2")
                .kv("summary", 2, 2)
        );
    }
}
