package cn.atool.distributor.snowflake.builder;

import cn.atool.distributor.snowflake.model.SnowFlakeConstant;
import cn.atool.distributor.snowflake.model.SnowFlakeGenerator;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;


/**
 * 测试雪花算法生成
 *
 * @author darui.wu
 * @create 2019-09-18 12:08
 */
public class SnowFlakeGeneratorTest extends Test4J {
    /**
     * 检查数据中心+机器码占位符
     *
     * @throws Exception
     */
    @Test
    public void testWorkId() throws Exception {
        long workId = new SnowFlakeProp().setTradeType("default").init(1, 2).getWorkId();
        MessageHelper.info(Long.toBinaryString(workId));
        want.string(Long.toBinaryString(workId)).eq(String.format("%d%09d%010d", 1, 10, 0));
    }

    /**
     * 验证毫秒周期内的最大序号计算的正确性（2的分配位数次方）
     *
     * @throws Exception
     */
    @Test
    public void testMaxSequence() throws Exception {
        want.number(new SnowFlakeProp().setTradeType("default").init(1, 1)
                .getMaxSequence()).eq(((Double) Math.pow(2, 10)).intValue() - 1);
        want.number(new SnowFlakeProp().setTradeType("default").setIdcBits(2).setMachineBits(5).init(1, 1)
                .getMaxSequence()).eq(((Double) Math.pow(2, 15)).intValue() - 1);
    }

    /**
     * 如果在使用雪花算法前，SnowFlakeProp未被初始化，需要抛出异常
     *
     * @throws Exception
     */
    @Test
    public void noInitException() throws Exception {
        try {
            SnowFlakeGenerator generator = new SnowFlakeGenerator(new SnowFlakeProp());
            want.fail();
        } catch (Exception e) {
            want.string(e.getMessage()).eq("the SnowFlakeProp should be init.");
        }
    }

    /**
     * 测试一毫秒周期内序号自增情况
     *
     * @throws Exception
     */
    @Test
    public void generate() throws Exception {
        final long curr = SnowFlakeConstant.START_STAMP + 1;
        new MockUp<SnowFlakeGenerator>() {
            int count = 0;

            @Mock
            public long currentTimeMillis() {
                return curr + (count++) / 1000;
            }
        };
        SnowFlakeGenerator generator = new SnowFlakeGenerator(new SnowFlakeProp().setTradeType("default").init(1, 1));
        long id = generator.nextId();
        want.string(Long.toBinaryString(id)).eq(String.format("%d%03d%09d%010d", 1, 1, 1, 0));
        id = generator.nextId();
        want.string(Long.toBinaryString(id)).eq(String.format("%d%03d%09d%010d", 1, 1, 1, 1));
        id = generator.nextId();
        want.string(Long.toBinaryString(id)).eq(String.format("%d%03d%09d%010d", 1, 1, 1, 10));
    }

    /**
     * 如果在一毫秒内序号没有达到最大值，并且时间到了下一毫秒（反映在这里就是调用currentTimeMillis达到1000次）
     * 那么在新的毫秒周期内， 序号从0开始计数
     *
     * @throws Exception
     */
    @Test
    public void generate_nextMSecond() throws Exception {
        final long curr = SnowFlakeConstant.START_STAMP + 1;
        new MockUp<SnowFlakeGenerator>() {
            int count = 0;

            @Mock
            public long currentTimeMillis() {
                return curr + (count++) / 1000;
            }
        };
        SnowFlakeGenerator generator = new SnowFlakeGenerator(new SnowFlakeProp().setTradeType("default").init(1, 1));
        // 调用1000次，根据mock时间跑到下一毫秒
        generator.nextId(1000);
        // 序号重新开始计数
        long id = generator.nextId();
        want.string(Long.toBinaryString(id)).eq(String.format("%d%03d%09d%010d", 10, 1, 1, 0));
    }

    /**
     * 检查一毫秒内，如果序号达到最大序列号1024个
     * 需要自旋到下一毫秒（反映在这里就是调用currentTimeMillis达到2000次, 且生成的id高位的时间戳为新的毫秒数）
     * 且序号从0重新开始
     *
     * @throws Exception
     */
    @Test
    public void generate_maxSequence_utilNextMS() throws Exception {
        final int[] count = {0};
        final long curr = SnowFlakeConstant.START_STAMP + 1;
        new MockUp<SnowFlakeGenerator>() {
            @Mock
            public long currentTimeMillis() {
                return curr + (++count[0]) / 2000;
            }
        };
        SnowFlakeGenerator generator = new SnowFlakeGenerator(new SnowFlakeProp().setTradeType("default").init(1, 1));
        long id = generator.nextId();
        want.string(Long.toBinaryString(id)).eq(String.format("%d%03d%09d%010d", 1, 1, 1, 0));
        generator.nextId(1023);
        want.number(count[0]).eq(1024);
        id = generator.nextId();
        want.number(count[0]).eq(2000);
        // 检查高位时间戳和序号
        want.string(Long.toBinaryString(id)).eq(String.format("%d%03d%09d%010d", 10, 1, 1, 0));
    }

    /**
     * 检查补位字符串长度
     */
    @Test
    public void checkSupplyChar() {
        for (int len = 0; len <= 13; len++) {
            want.number(SnowFlakeConstant.SUPPLEMENT_STR[len].length()).eq(len);
        }
    }

    /**
     * 检查START_STAMP和 @{IdWorker} 中的值一样
     */
    @Test
    public void checkStartStamp() {
        want.number(SnowFlakeConstant.START_STAMP).eq(1433908800660L);
    }
}
