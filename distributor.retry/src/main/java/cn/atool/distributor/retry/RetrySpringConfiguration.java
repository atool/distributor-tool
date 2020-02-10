package cn.atool.distributor.retry;

import cn.atool.distributor.retry.service.RetryAspect;
import cn.atool.distributor.retry.service.RetryHandler;
import cn.atool.distributor.retry.service.RetryPersistence;
import cn.atool.distributor.retry.service.base.RetryHandlerImpl;
import cn.atool.distributor.retry.service.database.RetryDbPersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static cn.atool.distributor.retry.model.RetryConstant.Retry_Db_Persistence_Bean;

/**
 * 幂等处理spring配置相关
 *
 * @author darui.wu
 * @create 2019/12/20 1:59 下午
 */
public abstract class RetrySpringConfiguration {
    /**
     * 重试方法切面
     *
     * @return
     */
    @Bean
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }

    /**
     * 持久化实现
     *
     * @return
     */
    @Bean
    @Qualifier(Retry_Db_Persistence_Bean)
    public RetryPersistence retryDbPersistence() {
        return new RetryDbPersistence(retryPersistenceDataSource());
    }

    @Bean
    public RetryHandler retryHandler(@Qualifier(Retry_Db_Persistence_Bean) RetryPersistence retryPersistence) {
        return new RetryHandlerImpl().setRetryPersistence(retryPersistence);
    }

    /**
     * 重试持久化数据源
     *
     * @return
     */
    public abstract DataSource retryPersistenceDataSource();
}
