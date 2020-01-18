package cn.atool.distributor.idempotent.fortest;

import cn.atool.distributor.idempotent.IdemSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.test4j.module.database.sql.DataSourceCreatorFactory;

import javax.sql.DataSource;

/**
 * @author darui.wu
 * @create 2020/1/15 9:59 上午
 */
@Configuration
@ComponentScan(basePackages = {"cn.atool.distributor.idempotent.fortest.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringTestConfig extends IdemSpringConfiguration {
    @Bean(name = "dataSource")
    public DataSource newDataSource() {
        return DataSourceCreatorFactory.create("dataSource");
    }

    @Override
    public DataSource idempotentDataSource() {
        return this.newDataSource();
    }
}
