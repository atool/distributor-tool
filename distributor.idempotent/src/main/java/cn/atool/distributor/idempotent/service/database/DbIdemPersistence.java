package cn.atool.distributor.idempotent.service.database;

import cn.atool.distributor.idempotent.model.IdemBody;
import cn.atool.distributor.idempotent.model.IdemValue;
import cn.atool.distributor.idempotent.service.IdemPersistence;
import cn.atool.distributor.idempotent.exception.IdemConcurrentException;
import cn.atool.distributor.idempotent.exception.IdemLockException;
import cn.atool.distributor.idempotent.model.IdemStatus;
import cn.atool.distributor.serialize.SerializeFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author darui.wu
 * @create 2019/12/20 4:07 下午
 */
public class DbIdemPersistence extends JdbcDaoSupport implements IdemPersistence {
    public DbIdemPersistence(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public IdemValue existIdempotent(IdemBody body) throws IdemConcurrentException {
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(IdemSql.SELECT_STATUS, body.getType(), body.getKey());
        Map<String, Object> map = CollectionUtils.isEmpty(list) ? null : list.get(0);
        if (map == null) {
            return null;
        }
        String status = (String) map.get(IdemSql.Field_Idem_Status);
        Long isExpired = (Long) map.get(IdemSql.Field_Expired);
        if (IdemStatus.COMMIT.equalsIgnoreCase(status)) {
            String value = (String) map.get(IdemSql.Field_Idem_Value);
            return new IdemValue(body.getProtocol(), value, body.getValueType());
        } else if (IdemStatus.BEGIN.equalsIgnoreCase(status) && isExpired == 0) {
            throw new IdemConcurrentException(body);
        } else {
            this.delete(body, IdemStatus.TIME_OUT);
            return null;
        }
    }

    @Override
    public void begin(IdemBody body) throws IdemLockException {
        try {
            super.getJdbcTemplate()
                    .update(IdemSql.INSERT_IDEM, body.getType(), body.getKey(), body.getProtocol(), IdemStatus.BEGIN, body.getTimeout());
        } catch (RuntimeException e) {
            throw new IdemLockException(body);
        }
    }

    @Override
    public void delete(IdemBody body, String status) {
        super.getJdbcTemplate()
                .update(IdemSql.ROLLBACK_SQL, status, body.getRetainTime(), System.currentTimeMillis(), body.getType(), body.getKey());
    }

    @Override
    public void commit(IdemBody body, Object value) {
        String _value = SerializeFactory.protocol(body.getProtocol()).toString(value);
        super.getJdbcTemplate()
                .update(IdemSql.COMMIT_SQL, IdemStatus.COMMIT, _value, body.getRetainTime(), body.getType(), body.getKey());
    }
}
