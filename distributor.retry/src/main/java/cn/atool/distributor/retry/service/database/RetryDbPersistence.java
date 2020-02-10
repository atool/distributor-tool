package cn.atool.distributor.retry.service.database;

import cn.atool.distributor.retry.model.RetryBody;
import cn.atool.distributor.retry.service.RetryPersistence;
import cn.atool.distributor.retry.exception.RetryException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author darui.wu
 * @create 2019/12/20 4:07 下午
 */
public class RetryDbPersistence extends JdbcDaoSupport implements RetryPersistence {
    public RetryDbPersistence(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public List<RetryBody> findRetry(String retryCategory, long begin) throws RetryException {
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(RetrySql.SELECT_EVENTS, retryCategory, begin);
        List<RetryBody> bodyList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            bodyList.add(new RetryBody(map));
        }
        return bodyList;
    }

    @Override
    public RetryBody findRetry(String retryCategory, String retryKey) throws RetryException {
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(RetrySql.SELECT_SPEC_EVENT, retryCategory, retryKey);
        if (list == null || list.size() == 0) {
            return null;
        }
        return new RetryBody(list.get(0));
    }

    @Override
    public List<RetryBody> summaryRetry(String retryStatus) {
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(RetrySql.SUMMARY_EVENTS, retryStatus);
        List<RetryBody> bodies = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return bodies;
        }
        for (Map<String, Object> map : list) {
            bodies.add(new RetryBody()
                    .setRetryCategory((String) map.get(RetrySql.Field_Retry_Category))
                    .setSummary((Long) map.get("summary")
                    )
            );
        }
        return bodies;
    }


    @Override
    public void save(RetryBody body, int maxRetry, Throwable e) throws RetryException {
        try {
            String args = body.getArgsStr();
            long id = this.findExist(body.getRetryCategory(), body.getRetryKey());
            String err = e.getClass().getName() + "#" + e.getMessage();
            if (id > 0) {
                super.getJdbcTemplate().update(RetrySql.UPDATE_RETRY, err, id);
            } else {
                super.getJdbcTemplate().update(RetrySql.SAVE_RETRY,
                        body.getRetryCategory(), body.getRetryKey(), body.getSignature(), body.getProtocol(), args, err, maxRetry);
            }
        } catch (RuntimeException e1) {
            e1.printStackTrace();
            throw new RetryException(body, e1);
        }
    }

    @Override
    public void closeRetry(RetryBody body) {
        try {
            super.getJdbcTemplate().update(RetrySql.CLOSE_RETRY, System.currentTimeMillis(), body.getRetryCategory(), body.getRetryKey());
        } catch (RuntimeException e1) {
            throw new RetryException(body, e1);
        }
    }

    public long findExist(String retryCategory, String hash) {
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(RetrySql.FIND_EXISTS, retryCategory, hash);
        if (list == null || list.size() == 0) {
            return 0L;
        } else {
            return (Long) list.get(0).get(RetrySql.Field_Id);
        }
    }
}
