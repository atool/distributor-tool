package cn.atool.distributor.snowflake.fortest.datamap;

import cn.atool.distributor.snowflake.fortest.datamap.SnowflakeMP.Column;
import org.test4j.module.database.IDataSourceScript;

/**
 * 生成内存数据库（h2)脚本
 *
 * @author darui.wu
 * @create 2019-09-02 18:03
 */
public class DataSourceScript implements IDataSourceScript {
    @Override
    public Class[] getTableKlass() {
        return new Class[]{
                SnowflakeTableMap.class
        };
    }

    @Override
    public IndexList getIndexList() {
        return new IndexList()
                .unique(SnowflakeMP.Table_Name, Column.trade_type, Column.machine_no, Column.is_deleted)
                .unique(SnowflakeMP.Table_Name, Column.machine_ip, Column.trade_type, Column.is_deleted)
                ;
    }

    static {
        SPEC_TYPES.put("json", "text");
    }
}