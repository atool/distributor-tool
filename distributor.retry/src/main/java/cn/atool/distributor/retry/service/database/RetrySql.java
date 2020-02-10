package cn.atool.distributor.retry.service.database;

import cn.atool.distributor.retry.model.RetryStatus;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/20.
 */
public interface RetrySql {
    /**
     * 重试事件表
     */
    String Retry_Table = "distributor_retry_event";
    /**
     * 新增重试消息
     */
    String SAVE_RETRY = "INSERT INTO " + Retry_Table +
            "(retry_category, retry_key, method_signature, protocol, method_args " +
            ", retry_status, err_message, max_retry, has_retry, gmt_create, gmt_modified, is_deleted) " +
            "VALUES(?, ?, ?, ?, ?" +
            String.format(", '%s', ?, ?, 0, now(), now(), 0)", RetryStatus.FAILURE);

    /**
     * 批量查询重试消息
     */
    String SELECT_EVENTS = "SELECT id, retry_category, retry_key, method_signature, protocol, method_args " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE retry_category=? " +
            String.format("AND retry_status = '%s' ", RetryStatus.FAILURE) +
            "AND has_retry < max_retry " +
            "AND is_deleted=0 " +
            "AND id >= ? " +
            "ORDER BY id+0 " +
            "LIMIT 100";

    /**
     * 聚合统计重试消息
     */
    String SUMMARY_EVENTS = "SELECT retry_category, count(1) as summary " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE is_deleted=0 " +
            "AND retry_status=? " +
            "GROUP BY retry_category";

    /**
     * 根据重试事件业务键值获取重试事件id
     */
    String FIND_EXISTS = "SELECT id " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE retry_category=? " +
            "AND retry_key=?";
    /**
     * 根据重试事件业务键值获取重试事件
     */
    String SELECT_SPEC_EVENT = "SELECT id, retry_category, retry_key, method_signature, protocol, method_args " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE retry_category=? " +
            "AND retry_key=? " +
            "AND is_deleted=0 " +
            String.format("AND retry_status = '%s' ", RetryStatus.FAILURE);
    /**
     * 更新指定重试消息
     */
    String UPDATE_RETRY = String.format("UPDATE %s ", Retry_Table) +
            "SET err_message = ?, " +
            String.format("retry_status = case when (has_retry+1) >=max_retry then '%s' else '%s' end, ", RetryStatus.EXCEED, RetryStatus.FAILURE) +
            "has_retry = has_retry + 1, " +
            "gmt_modified = now() " +
            "WHERE id = ?";
    /**
     * 关闭重试消息
     */
    String CLOSE_RETRY = String.format("UPDATE %s ", Retry_Table) +
            "SET gmt_modified = now(), " +
            "has_retry = has_retry + 1, " +
            "is_deleted = ?, " +
            String.format("retry_status='%s' ", RetryStatus.FINISH) +
            "WHERE retry_category=? " +
            "AND retry_key = ?";

    /**
     * 字段: id
     */
    String Field_Id = "id";
    /**
     * 字段: 重试事件分类标识
     */
    String Field_Retry_Category = "retry_category";
    /**
     * 字段: 重试事件键值
     */
    String Field_Retry_Key = "retry_key";
    /**
     * 字段: 方法签名
     */
    String Field_Method_Signature = "method_signature";
    /**
     * 字段: 方法参数值
     */
    String Field_Method_Args = "method_args";
    /**
     * 协议字段
     */
    String Field_Protocol = "protocol";
}
