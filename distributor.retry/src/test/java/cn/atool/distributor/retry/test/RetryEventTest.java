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
                .with(this::setMethod1)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(0)
        );
    }

    @Test
    void retry_method1_again_fail() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod1)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(0)
        );
        assertThrows(RuntimeException.class, () -> bizService.doSomeThing("request1", 11, Arrays.asList("list1", "list2")));
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod1)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(1)
        );
    }

    @Test
    void retry_method1_again_fail2() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod1)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(2)
        );
        db.table(TABLE_RETRY_EVENT).query().print();
        assertThrows(RuntimeException.class, () -> this.retryHandler.doRetry("bizServiceImpl", "doSomeThing", "request1", null));
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod1)
                .retry_status.values(RetryStatus.EXCEED)
                .has_retry.values(3)
        );
    }

    @Test
    void retry_method1_again_success() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(1).init()
                .with(this::setMethod1)
                .retry_status.values(RetryStatus.FAILURE)
                .has_retry.values(2)
        );
        new MockUp<BizServiceImpl>() {
            @Mock
            public Map<String, String> doSomeThing(Integer arg1, List<String> arg2) {
                return new HashMap<>();
            }
        };
        this.retryHandler.doRetry("bizServiceImpl", "doSomeThing", "request1", (result, args) -> {
            want.map((Map) result).notNull().sizeEq(0);
            want.array(args).eqReflect(new Object[]{"request1", 11, Arrays.asList("list1", "list2")});
        });
        db.table(TABLE_RETRY_EVENT).query()
                .eqDataMap(new RetryEventTableMap(1)
                        .with(this::setMethod1)
                        .retry_status.values(RetryStatus.FINISH)
                        .has_retry.values(3)
                );
    }

    RetryEventTableMap setMethod1(RetryEventTableMap map) {
        return map.target_bean.values("bizServiceImpl")
                .target_class.values(BizServiceImpl.class.getName())
                .target_method.values("doSomeThing")
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
        this.retryHandler.doRetry("bizServiceImpl", "doSomeThing", "3nnat27fk7zocfvtxiupufmg4", (result, args) -> {
            want.map((Map) result).notNull().sizeEq(0);
            want.array(args).eqReflect(new Object[]{11, Arrays.asList("list1", "list2")});
        });
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(1)
                .with(this::setMethod2)
                .retry_status.values(RetryStatus.FINISH)
                .has_retry.values(1)
        );
    }


    RetryEventTableMap setMethod2(RetryEventTableMap map) {
        return map.target_bean.values("bizServiceImpl")
                .target_class.values(BizServiceImpl.class.getName())
                .target_method.values("doSomeThing")
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
                .with(this::setMethod1)
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
        long id = this.retryHandler.doRetry("bizServiceImpl", "doSomeThing", 0, (result, args) -> {
        });
        db.table(TABLE_RETRY_EVENT).query().eqDataMap(new RetryEventTableMap(2)
                .target_class.values(BizServiceImpl.class.getName())
                .target_method.values("doSomeThing")
                .retry_status.values(RetryStatus.FINISH)
                .has_retry.values(1)
                .retry_key.values("request1", "3nnat27fk7zocfvtxiupufmg4")
        );
        want.number(id).isGt(1L);
    }

    @Test
    public void summary_retry() {
        db.table(TABLE_RETRY_EVENT).clean().insert(new RetryEventTableMap(5).init()
                .target_bean.values("bizServiceImpl")
                .target_class.values(BizServiceImpl.class.getName())
                .target_method.values("method1", "method2", "method1", "method2", "method1")
                .retry_status.values(RetryStatus.FAILURE, RetryStatus.FAILURE, RetryStatus.FINISH, RetryStatus.FAILURE)
                .is_deleted.values(0)
        );

        List<RetryBody> bodies = this.retryHandler.getRetryPersistence().summaryRetry(RetryStatus.FAILURE);
        want.list(bodies).eqDataMap(new DataMap(2)
                .kv("targetBean", "bizServiceImpl")
                .kv("targetClass", BizServiceImpl.class.getName())
                .kv("targetMethod", "method1", "method2")
                .kv("summary", 2, 2)
        );
    }
}
