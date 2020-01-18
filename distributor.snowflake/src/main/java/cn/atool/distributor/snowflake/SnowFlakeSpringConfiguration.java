package cn.atool.distributor.snowflake;

import cn.atool.distributor.snowflake.builder.SnowFlakeBuilder;
import cn.atool.distributor.snowflake.builder.database.DbSnowFlakeBuilder;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.List;

import static cn.atool.distributor.snowflake.model.SnowFlakeConstant.Reset_Method;

/**
 * @author darui.wu
 * @create 2020/1/16 6:58 下午
 */
public abstract class SnowFlakeSpringConfiguration {
    /**
     * 雪花算法机器分配数据源
     *
     * @return
     */
    public abstract DataSource snowFlakeDataSource();

    public abstract List<SnowFlakeProp> snowFlakeProps();

    @Bean(initMethod = Reset_Method)
    public SnowFlakeBuilder dbSnowFlakeBuilder() {
        return new DbSnowFlakeBuilder(this.snowFlakeDataSource(), this.snowFlakeProps());
    }
}
