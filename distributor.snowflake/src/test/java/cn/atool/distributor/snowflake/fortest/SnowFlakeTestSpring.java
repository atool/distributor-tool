package cn.atool.distributor.snowflake.fortest;

import cn.atool.distributor.snowflake.SnowFlakeSpringConfiguration;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test4j.module.database.sql.DataSourceCreatorFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author darui.wu
 * @create 2020/1/16 7:25 下午
 */
@Configuration
public class SnowFlakeTestSpring extends SnowFlakeSpringConfiguration {
    @Bean
    public DataSource dataSource() {
        return DataSourceCreatorFactory.create("dataSource");
    }

    @Override
    public DataSource snowFlakeDataSource() {
        return this.dataSource();
    }

    @Override
    public List<SnowFlakeProp> snowFlakeProps() {
        return Arrays.asList(new SnowFlakeProp().setTradeType("test"));
    }
}
