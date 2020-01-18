package cn.atool.distributor.snowflake.builder.database;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.*;

import static cn.atool.distributor.snowflake.builder.database.SnowFlakeSql.*;
import static java.util.stream.Collectors.joining;

/**
 * snowflake数据库操作
 *
 * @author darui.wu
 * @create 2020/1/16 1:39 下午
 */
public class SnowFlakeDao extends JdbcDaoSupport {
    public SnowFlakeDao(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * 查找和本机关联的所有业务类型及机器号
     *
     * @param tradeTypes
     * @param localIP
     * @return
     */
    public Map<String, Integer> findExistMachineNo(List<String> tradeTypes, String localIP) {
        Map<String, Integer> tradeTypeMachineNos = new HashMap<>();
        if (tradeTypes == null || tradeTypes.size() == 0) {
            return tradeTypeMachineNos;
        }
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(SELECT_TRADE_TYPE_AND_MACHINE_NO, localIP);
        for (Map<String, Object> map : list) {
            String tradeType = (String) map.get(Field_Trade_Type);
            if (tradeTypes.contains(tradeType)) {
                tradeTypeMachineNos.put(tradeType, (Integer) map.get(Field_Machine_No));
            }
        }
        return tradeTypeMachineNos;
    }

    /**
     * 查找已使用的机器号列表
     *
     * @param tradeType
     * @return
     */
    public List<Integer> findTradeMachineNos(String tradeType) {
        List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(SELECT_MACHINE_NOS_BY_TRADE, tradeType);
        List<Integer> existedMachineNos = new ArrayList<>();
        for (Map<String, Object> map : list) {
            existedMachineNos.add((Integer) map.get(Field_Machine_No));
        }
        return existedMachineNos;
    }

    /**
     * 分配机器号
     *
     * @param tradeType
     * @param localIP
     * @param machineNo
     */
    public void createMachineNo(String tradeType, String localIP, int machineNo) {
        int count = super.getJdbcTemplate().update(CREATE_TRADE_MACHINE_NO, tradeType, localIP, machineNo);
        if (count < 1) {
            throw new RuntimeException(String.format("createMachineNo[tradeType=%s, machineIp=%s, machineNo=%d] fail.", tradeType, localIP, machineNo));
        }
    }

    /**
     * 更新tradeType + machineNo记录的使用时间
     *
     * @param machineIp
     * @param tradeTypes
     */
    public void updateHeartByIp(String machineIp, Set<String> tradeTypes) {
        int size = tradeTypes.size();
        if (size == 0) {
            return;
        }
        String sql = String.format(UPDATE_MODIFIED, tradeTypes.stream().map(t -> "?").collect(joining(",")));
        Object[] args = new Object[size + 1];
        args[0] = machineIp;
        int index = 1;
        for (Iterator<String> it = tradeTypes.iterator(); it.hasNext(); ) {
            args[index] = it.next();
            index++;
        }
        super.getJdbcTemplate().update(sql, args);
    }
}
