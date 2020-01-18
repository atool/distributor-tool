package cn.atool.distributor.retry.fortest.datamap;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RetryEventMP
 * @Description RetryEventMP
 *
 * @author generate code
 */
public interface RetryEventMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.id, Column.id);
            this.put(Property.gmtCreate, Column.gmt_create);
            this.put(Property.gmtModified, Column.gmt_modified);
            this.put(Property.isDeleted, Column.is_deleted);
            this.put(Property.errMessage, Column.err_message);
            this.put(Property.hasRetry, Column.has_retry);
            this.put(Property.maxRetry, Column.max_retry);
            this.put(Property.methodArgs, Column.method_args);
            this.put(Property.methodSignature, Column.method_signature);
            this.put(Property.protocol, Column.protocol);
            this.put(Property.retryKey, Column.retry_key);
            this.put(Property.retryStatus, Column.retry_status);
            this.put(Property.targetBean, Column.target_bean);
            this.put(Property.targetClass, Column.target_class);
            this.put(Property.targetMethod, Column.target_method);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "distributor_retry_event";
    /**
    * 实体名称
    */
    String Entity_NAME = "RetryEventEntity";

    /**
     * 表atool_retry_event字段定义
     */
    interface Column{
        /**
         * 主键
         */
        String id = "id";
        /**
         * 记录创建时间
         */
        String gmt_create = "gmt_create";
        /**
         * 记录修改时间
         */
        String gmt_modified = "gmt_modified";
        /**
         * 逻辑删除
         */
        String is_deleted = "is_deleted";
        /**
         * 错误信息
         */
        String err_message = "err_message";
        /**
         * 已经重试次数
         */
        String has_retry = "has_retry";
        /**
         * 最大重试次数
         */
        String max_retry = "max_retry";
        /**
         * 重试参数值
         */
        String method_args = "method_args";
        /**
         * 方法参数签名
         */
        String method_signature = "method_signature";
        /**
         * 参数序列化协议
         */
        String protocol = "protocol";
        /**
         * 重试事件幂等key值
         */
        String retry_key = "retry_key";
        /**
         * 重试状态
         */
        String retry_status = "retry_status";
        /**
         * 重试方法目标spring bean定义
         */
        String target_bean = "target_bean";
        /**
         * 重试方法目标类型
         */
        String target_class = "target_class";
        /**
         * 重试方法名称
         */
        String target_method = "target_method";
    }

    /**
     * 对象RetryEventEntity属性字段
     */
    interface Property{
        /**
         * 主键
         */
        String id = "id";
        /**
         * 记录创建时间
         */
        String gmtCreate = "gmtCreate";
        /**
         * 记录修改时间
         */
        String gmtModified = "gmtModified";
        /**
         * 逻辑删除
         */
        String isDeleted = "isDeleted";
        /**
         * 错误信息
         */
        String errMessage = "errMessage";
        /**
         * 已经重试次数
         */
        String hasRetry = "hasRetry";
        /**
         * 最大重试次数
         */
        String maxRetry = "maxRetry";
        /**
         * 重试参数值
         */
        String methodArgs = "methodArgs";
        /**
         * 方法参数签名
         */
        String methodSignature = "methodSignature";
        /**
         * 参数序列化协议
         */
        String protocol = "protocol";
        /**
         * 重试事件幂等key值
         */
        String retryKey = "retryKey";
        /**
         * 重试状态
         */
        String retryStatus = "retryStatus";
        /**
         * 重试方法目标spring bean定义
         */
        String targetBean = "targetBean";
        /**
         * 重试方法目标类型
         */
        String targetClass = "targetClass";
        /**
         * 重试方法名称
         */
        String targetMethod = "targetMethod";
    }
}