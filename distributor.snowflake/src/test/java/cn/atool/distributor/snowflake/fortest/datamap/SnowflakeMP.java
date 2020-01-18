package cn.atool.distributor.snowflake.fortest.datamap;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SnowflakeMP
 * @Description SnowflakeMP
 *
 * @author generate code
 */
public interface SnowflakeMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.id, Column.id);
            this.put(Property.gmtCreate, Column.gmt_create);
            this.put(Property.gmtModified, Column.gmt_modified);
            this.put(Property.isDeleted, Column.is_deleted);
            this.put(Property.machineIp, Column.machine_ip);
            this.put(Property.machineNo, Column.machine_no);
            this.put(Property.tradeType, Column.trade_type);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "distributor_snowflake";
    /**
    * 实体名称
    */
    String Entity_NAME = "SnowflakeEntity";

    /**
     * 表distributor_snowflake字段定义
     */
    interface Column{
        /**
         * 
         */
        String id = "id";
        /**
         * 
         */
        String gmt_create = "gmt_create";
        /**
         * 
         */
        String gmt_modified = "gmt_modified";
        /**
         * 
         */
        String is_deleted = "is_deleted";
        /**
         * 
         */
        String machine_ip = "machine_ip";
        /**
         * 
         */
        String machine_no = "machine_no";
        /**
         * 
         */
        String trade_type = "trade_type";
    }

    /**
     * 对象SnowflakeEntity属性字段
     */
    interface Property{
        /**
         * 
         */
        String id = "id";
        /**
         * 
         */
        String gmtCreate = "gmtCreate";
        /**
         * 
         */
        String gmtModified = "gmtModified";
        /**
         * 
         */
        String isDeleted = "isDeleted";
        /**
         * 
         */
        String machineIp = "machineIp";
        /**
         * 
         */
        String machineNo = "machineNo";
        /**
         * 
         */
        String tradeType = "tradeType";
    }
}