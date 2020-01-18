package cn.atool.distributor.snowflake.builder.database;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/1/16.
 */
public interface SnowFlakeSql {
    String SNOW_FLAKE_TABLE = "distributor_snowflake";

    String SELECT_TRADE_TYPE_AND_MACHINE_NO = "SELECT trade_type, machine_no " +
            String.format("FROM %s ", SNOW_FLAKE_TABLE) +
            "WHERE is_deleted = 0 " +
            "AND machine_ip = ?";

    String SELECT_MACHINE_NOS_BY_TRADE = "SELECT machine_no " +
            String.format("FROM %s ", SNOW_FLAKE_TABLE) +
            "WHERE is_deleted =0 " +
            "AND trade_type = ?";

    String CREATE_TRADE_MACHINE_NO = String.format("insert into %s", SNOW_FLAKE_TABLE) +
            "(trade_type, machine_ip, machine_no, gmt_create, gmt_modified, is_deleted) " +
            "values(?, ?, ?, now(), now(), 0)";

    String UPDATE_MODIFIED = String.format("update %s ", SNOW_FLAKE_TABLE) +
            "set gmt_modified=now() " +
            "where is_deleted=0 " +
            "and machine_ip=? " +
            "and trade_type in (%s)";

    String Field_Trade_Type = "trade_type";

    String Field_Machine_No = "machine_no";
}
