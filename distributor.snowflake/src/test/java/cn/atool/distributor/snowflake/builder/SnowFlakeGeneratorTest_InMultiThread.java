package cn.atool.distributor.snowflake.builder;

import cn.atool.distributor.snowflake.model.SnowFlakeConstant;
import cn.atool.distributor.snowflake.model.SnowFlakeGenerator;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 测试雪花算法生成
 *
 * @author darui.wu
 * @create 2019-09-18 12:08
 */
public class SnowFlakeGeneratorTest_InMultiThread extends Test4J {
    /**
     * 验证在多线程下的生成正确性
     *
     * @throws Exception
     */
    @Test
    public void checkMultiThread() throws Exception {
        final int[] count = {0};
        final long curr = SnowFlakeConstant.START_STAMP + 1;
        new MockUp<SnowFlakeGenerator>() {
            @Mock
            public long currentTimeMillis() {
                return curr + (++count[0]) / 2000;
            }
        };
        final SnowFlakeGenerator generator = new SnowFlakeGenerator(new SnowFlakeProp().setTradeType("default").init(1, 1));
        int call_count = 1024 + 16;
        Set<Long> ids = this.nextIdInMultiThread(generator, call_count);
        Set<Long> expects = buildExpectedIds(call_count);
        //验证值的正确性
        want.list(ids).eqReflect(expects);
        //验证单一毫秒内的时间自旋
        want.number(count[0]).eq(2000 + (call_count - 1024) - 1);
    }

    /**
     * 在多线程中执行雪花算法
     *
     * @param generator
     * @param count     执行次数
     * @return 返回所有在多线程中产生的id值
     * @throws Exception
     */
    private Set<Long> nextIdInMultiThread(final SnowFlakeGenerator generator, int count) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(100);
        List<Future<Long>> futures = new ArrayList<>();
        for (int loop = 0; loop < count; loop++) {
            Future<Long> future = pool.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return generator.nextId();
                }
            });
            futures.add(future);
        }
        Set<Long> ids = new HashSet<>();
        for (int loop = 0; loop < count; loop++) {
            ids.add(futures.get(loop).get());
        }
        return ids;
    }

    /**
     * 构造期望的id值列表
     * 第一毫秒内，有1024个id
     * 第二毫秒内，有16个id
     *
     * @param count 调用次数
     * @return
     * @throws Exception
     */
    private Set<Long> buildExpectedIds(int count) throws Exception {
        Set<Long> expects = new HashSet<>(4000);
        long fstMsBaseId = Long.parseLong("010010000000010000000000", 2);
        long sndMsBaseId = Long.parseLong("100010000000010000000000", 2);
        for (int loop = 0; loop < 1024; loop++) {
            expects.add(fstMsBaseId + loop);
        }
        for (int loop = 0; loop < count - 1024; loop++) {
            expects.add(sndMsBaseId + loop);
        }
        return expects;
    }
}
