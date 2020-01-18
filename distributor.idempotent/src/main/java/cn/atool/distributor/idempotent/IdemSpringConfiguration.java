package cn.atool.distributor.idempotent;

import cn.atool.distributor.idempotent.service.IdemPersistence;
import cn.atool.distributor.idempotent.service.IdempotentAspect;
import cn.atool.distributor.idempotent.service.database.DbIdemPersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static cn.atool.distributor.idempotent.model.IdemConstant.Idem_Db_Persistence_Bean;

/**
 * 幂等处理spring配置相关
 *
 * @author darui.wu
 * @create 2019/12/20 1:59 下午
 */
public abstract class IdemSpringConfiguration {
    @Bean
    public IdempotentAspect newIdempotentAspect() {
        return new IdempotentAspect();
    }

    @Bean
    @Qualifier(Idem_Db_Persistence_Bean)
    public IdemPersistence idemDbPersistence() {
        return new DbIdemPersistence(idempotentDataSource());
    }

    /**
     * 幂等持久化数据源
     *
     * @return
     */
    public abstract DataSource idempotentDataSource();
}
