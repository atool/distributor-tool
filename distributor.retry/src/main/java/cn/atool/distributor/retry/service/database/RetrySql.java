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
            "(target_bean, target_class, target_method, retry_key, method_signature, protocol, method_args " +
            ", retry_status, err_message, max_retry, has_retry, gmt_create, gmt_modified, is_deleted) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?" +
            String.format(", '%s', ?, ?, 0, now(), now(), 0)", RetryStatus.FAILURE);

    /**
     * 批量查询重试消息
     */
    String SELECT_EVENTS = "SELECT id, target_bean, target_class, target_method, retry_key, method_signature, protocol, method_args " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE target_bean=? " +
            "AND target_method=? " +
            String.format("AND retry_status = '%s' ", RetryStatus.FAILURE) +
            "AND has_retry < max_retry " +
            "AND is_deleted=0 " +
            "AND id >= ? " +
            "ORDER BY id+0 " +
            "LIMIT 100";

    /**
     * 聚合统计重试消息
     */
    String SUMMARY_EVENTS = "SELECT target_bean, target_class, target_method, count(1) as summary " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE is_deleted=0 " +
            "AND retry_status=? " +
            "GROUP BY target_bean, target_class, target_method";

    /**
     * 根据重试事件业务键值获取重试事件id
     */
    String FIND_EXISTS = "SELECT id " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE target_bean=? " +
            "AND target_method=? " +
            "AND retry_key=?";
    /**
     * 根据重试事件业务键值获取重试事件
     */
    String SELECT_SPEC_EVENT = "SELECT id, target_bean, target_class, target_method, retry_key, method_signature, protocol, method_args " +
            String.format("FROM %s ", Retry_Table) +
            "WHERE target_bean=? " +
            "AND target_method=? " +
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
            String.format("retry_status='%s' ", RetryStatus.FINISH) +
            "WHERE target_bean=? " +
            "and target_method=? " +
            "AND retry_key = ?";

    /**
     * 字段: id
     */
    String Field_Id = "id";
    /**
     * 字段: 重试对象bean定义
     */
    String Field_Target_Bean = "target_bean";
    /**
     * 字段: 重试对象类型
     */
    String Field_Target_Class = "target_class";
    /**
     * 字段: 重试方法名称
     */
    String Field_Target_Method = "target_method";
    /**
     * 字段: 重试键值
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
