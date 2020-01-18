package cn.atool.distributor.snowflake.builder.database;

import cn.atool.distributor.snowflake.builder.BaseSnowFlakeBuilder;
import cn.atool.distributor.snowflake.builder.SnowFlakeBuilder;
import cn.atool.distributor.snowflake.fortest.SnowFlakeTestSpring;
import cn.atool.distributor.snowflake.fortest.datamap.SnowflakeTableMap;
import cn.atool.distributor.snowflake.model.SnowFlakeGenerator;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;

import java.util.Arrays;

import static cn.atool.distributor.snowflake.builder.database.SnowFlakeSql.SNOW_FLAKE_TABLE;

/**
 * @author darui.wu
 * @create 2020/1/16 7:25 下午
 */
@ContextConfiguration(classes = {SnowFlakeTestSpring.class})
public class DbSnowFlakeBuilderTest extends Test4J {
    @Autowired
    private SnowFlakeBuilder snowFlakeBuilder;

    public void reset(SnowflakeTableMap init, String... tradeTypes) throws Exception {
        db.table(SNOW_FLAKE_TABLE).clean();
        new MockUp<BaseSnowFlakeBuilder>() {
            @Mock
            public String getMachineIp() {
                return "127.0.0.2";
            }
        };
        ((DbSnowFlakeBuilder) snowFlakeBuilder).getSnowFlakeProps().clear();
        Arrays.stream(tradeTypes)
                .map(SnowFlakeProp::new)
                .forEach(snowFlakeBuilder::addSnowFlakeProperty);
        if (init != null) {
            db.table(SNOW_FLAKE_TABLE).insert(init);
        }
        snowFlakeBuilder.reset();
    }

    @Test
    void test() throws Exception {
        this.reset(null, "trade1", "trade2", "trade3");

        SnowFlakeGenerator sfg = snowFlakeBuilder.findGenerator("trade1");
        MessageHelper.info(sfg.nextId());
        MessageHelper.info(sfg.nextId13Char());
        MessageHelper.info(sfg.nextId11Char());
        db.table(SNOW_FLAKE_TABLE).query().eqDataMap(SnowflakeTableMap.create(3)
                .machine_ip.values("127.0.0.2")
                .trade_type.values("trade1", "trade2", "trade3")
        );
    }

    @Test
    void test2() throws Exception {
        this.reset(SnowflakeTableMap.create(3)
                        .trade_type.values("trade2")
                        .machine_ip.values("127.0.0.1", "127.0.0.3", "127.0.0.4")
                        .machine_no.values(0, 1, 3),
                "trade1", "trade2", "trade3");

        db.table(SNOW_FLAKE_TABLE).query().eqDataMap(SnowflakeTableMap.create(6)
                .trade_type.values("trade2", "trade2", "trade2", "trade1", "trade2", "trade3")
                .machine_ip.values("127.0.0.1", "127.0.0.3", "127.0.0.4", "127.0.0.2")
                .machine_no.values(0, 1, 3, 0, 2, 0)
        );
    }

    @Test
    void test_3() throws Exception {
        this.reset(SnowflakeTableMap.create(100)
                        .trade_type.values("trade1")
                        .machine_ip.functionAutoIncrease(index -> "127.0.0." + (index == 2 ? 101 : index))
                        .machine_no.functionAutoIncrease((index) -> index < 50 ? index - 1 : index),
                "trade1");

        db.table(SNOW_FLAKE_TABLE).query().eqDataMap(SnowflakeTableMap.create(101)
                        .trade_type.values("trade1")
                        .machine_ip.functionAutoIncrease(i -> "127.0.0." + (i < 101 ? (i == 2 ? 101 : i) : (i == 101 ? 2 : i)))
                        .machine_no.functionAutoIncrease(i -> i < 50 ? (i - 1) : (i == 101 ? 49 : i))
                , EqMode.IGNORE_ORDER);
    }

    @Test
    void test_concurrent() throws Exception {
        new MockUp<SnowFlakeDao>() {
            int index = 0;

            @Mock
            public void createMachineNo(Invocation it, String tradeType, String localIP, int machineNo) {
                if (index < 10) {
                    db.table(SNOW_FLAKE_TABLE).insert(SnowflakeTableMap.create(1)
                            .machine_ip.values("127.0.0." + (102 + index))
                            .trade_type.values(tradeType)
                            .machine_no.values(machineNo)
                    );
                    index++;
                }
                it.proceed(tradeType, localIP, machineNo);
            }
        };

        this.reset(SnowflakeTableMap.create(100)
                        .trade_type.values("trade1")
                        .machine_ip.functionAutoIncrease(index -> "127.0.0." + (index == 2 ? 101 : index))
                        .machine_no.functionAutoIncrease((index) -> index - 1),
                "trade1");

        db.table(SNOW_FLAKE_TABLE).query().eqDataMap(SnowflakeTableMap.create(111)
                .trade_type.values("trade1")
                .machine_ip.functionAutoIncrease(i -> "127.0.0." + (i < 101 ? (i == 2 ? 101 : i) : (i == 111 ? 2 : i + 1)))
                .machine_no.functionAutoIncrease(i -> i - 1)
        );
    }
}
