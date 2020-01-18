package cn.atool.distributor.snowflake.builder.zookeeper;


import cn.atool.distributor.snowflake.model.SnowFlakeGenerator;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.core.utility.MessageHelper;

import java.util.Arrays;

/**
 * @author darui.wu
 * @create 2019-09-18 16:31
 */
@Disabled
public class ZkSnowFlakeBuilderTest {

    String zkUrl = "localhost:2181";

    ZkSnowFlakeBuilder reset(String... tradeTypes) throws Exception {
        ZkSnowFlakeBuilder builder = new ZkSnowFlakeBuilder();
        builder.setZkConnection(zkUrl);
        Arrays.stream(tradeTypes)
                .map(SnowFlakeProp::new)
                .forEach(builder::addSnowFlakeProperty);
        builder.reset();
        return builder;
    }

    @Test
    public void generate() throws Exception {
        String tradeType = "default";
        SnowFlakeGenerator sfg = reset(tradeType).findGenerator(tradeType);

        MessageHelper.info(sfg.nextId());

        long curr = System.currentTimeMillis();
        long[] ids = sfg.nextId(10000);
        sfg.nextId11Char(10000);
        sfg.nextId13Char(10000);
        MessageHelper.info(System.currentTimeMillis() - curr);
    }
}
