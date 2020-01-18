package cn.atool.distributor.idempotent.service.database;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/20.
 */
public interface IdemSql {
    /**
     * 幂等数据库表
     */
    String Idem_Table = "distributor_idempotent";
    /**
     * 查找幂等消息
     */
    String SELECT_STATUS = "SELECT idem_status, protocol, idem_value, (DATE_ADD(gmt_create, INTERVAL expire_time SECOND) <= now()) as expired " +
            String.format("FROM %s ", Idem_Table) +
            "WHERE idem_type=? " +
            "AND idem_key=? " +
            "AND is_deleted=0";
    /**
     * 初始化幂等消息
     */
    String INSERT_IDEM = "insert into " + Idem_Table +
            "(idem_type, idem_key, protocol, idem_status, expire_time, gmt_create, gmt_modified, is_deleted) " +
            "value(?, ?, ?, ?, ?, now(), now(), 0)";
    /**
     * 回滚幂等消息
     */
    String ROLLBACK_SQL = String.format("update %s ", Idem_Table) +
            "set idem_status=?, expire_time=?, is_deleted=?, gmt_modified=now() " +
            "where idem_type=? and idem_key=? and is_deleted=0";
    /**
     * 提交幂等消息
     */
    String COMMIT_SQL = String.format("update %s ", Idem_Table) +
            "set idem_status=?, idem_value=?, expire_time=?, gmt_modified=now() " +
            "where idem_type=? and idem_key=? and is_deleted=0";

    /**
     * 幂等状态字段
     */
    String Field_Idem_Status = "idem_status";
    /**
     * 是否过期判断
     */
    String Field_Expired = "expired";
    /**
     * 协议字段
     */
    String Field_Protocol = "protocol";
    /**
     * 幂等结果值字段
     */
    String Field_Idem_Value = "idem_value";
}
