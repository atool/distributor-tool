package cn.atool.distributor.retry.fortest;

import cn.atool.distributor.retry.RetrySpringConfiguration;
import cn.atool.distributor.retry.service.RetryHandler;
import cn.atool.distributor.retry.service.base.RetryHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.test4j.module.database.sql.DataSourceCreatorFactory;

import javax.sql.DataSource;

/**
 * @author darui.wu
 * @create 2020/1/13 11:00 上午
 */
@Configuration
@ComponentScan(
        basePackages = {
                "cn.atool.distributor.retry.fortest.service"
        }
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringTestConfig extends RetrySpringConfiguration {
    @Bean("dataSource")
    public DataSource dataSource() {
        return DataSourceCreatorFactory.create("dataSource");
    }

    @Override
    public DataSource retryPersistenceDataSource() {
        return dataSource();
    }
}
