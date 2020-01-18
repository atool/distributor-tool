package cn.atool.distributor.idempotent.fortest.datamap;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName IdempotentMP
 * @Description IdempotentMP
 *
 * @author generate code
 */
public interface IdempotentMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.id, Column.id);
            this.put(Property.gmtCreate, Column.gmt_create);
            this.put(Property.gmtModified, Column.gmt_modified);
            this.put(Property.isDeleted, Column.is_deleted);
            this.put(Property.expireTime, Column.expire_time);
            this.put(Property.idemKey, Column.idem_key);
            this.put(Property.idemStatus, Column.idem_status);
            this.put(Property.idemType, Column.idem_type);
            this.put(Property.idemValue, Column.idem_value);
            this.put(Property.protocol, Column.protocol);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "distributor_idempotent";
    /**
    * 实体名称
    */
    String Entity_NAME = "IdempotentEntity";

    /**
     * 表atool_idempotent字段定义
     */
    interface Column{
        /**
         * 
         */
        String id = "id";
        /**
         * 幂等创建时间
         */
        String gmt_create = "gmt_create";
        /**
         * 修改时间
         */
        String gmt_modified = "gmt_modified";
        /**
         * 逻辑删除兼删除时间戳
         */
        String is_deleted = "is_deleted";
        /**
         * 超时时间或过期清理时间（单位秒）, 默认365天
         */
        String expire_time = "expire_time";
        /**
         * 幂等键值
         */
        String idem_key = "idem_key";
        /**
         * 幂等执行状态
         */
        String idem_status = "idem_status";
        /**
         * 幂等类型
         */
        String idem_type = "idem_type";
        /**
         * 幂等结果值
         */
        String idem_value = "idem_value";
        /**
         * idem_value序列化协议
         */
        String protocol = "protocol";
    }

    /**
     * 对象IdempotentEntity属性字段
     */
    interface Property{
        /**
         * 
         */
        String id = "id";
        /**
         * 幂等创建时间
         */
        String gmtCreate = "gmtCreate";
        /**
         * 修改时间
         */
        String gmtModified = "gmtModified";
        /**
         * 逻辑删除兼删除时间戳
         */
        String isDeleted = "isDeleted";
        /**
         * 超时时间或过期清理时间（单位秒）, 默认365天
         */
        String expireTime = "expireTime";
        /**
         * 幂等键值
         */
        String idemKey = "idemKey";
        /**
         * 幂等执行状态
         */
        String idemStatus = "idemStatus";
        /**
         * 幂等类型
         */
        String idemType = "idemType";
        /**
         * 幂等结果值
         */
        String idemValue = "idemValue";
        /**
         * idem_value序列化协议
         */
        String protocol = "protocol";
    }
}