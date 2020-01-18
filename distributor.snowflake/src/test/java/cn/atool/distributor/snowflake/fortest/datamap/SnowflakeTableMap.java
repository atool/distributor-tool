package cn.atool.distributor.snowflake.fortest.datamap;

import cn.atool.distributor.snowflake.fortest.datamap.SnowflakeMP.Column;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @author generate code
 * @ClassName SnowflakeTableMap
 * @Description SnowflakeTableMap
 */
@TableName(SnowflakeMP.Table_Name)
public class SnowflakeTableMap extends DataMap<SnowflakeTableMap> {
    /**
     * 设置distributor_snowflake对象id字段值
     */
    @ColumnDef(type = "bigint(20)", primary = PrimaryType.AutoIncrease)
    public transient final KeyValue<SnowflakeTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置distributor_snowflake对象gmt_create字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<SnowflakeTableMap> gmt_create = new KeyValue(this, Column.gmt_create);
    /**
     * 设置distributor_snowflake对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<SnowflakeTableMap> gmt_modified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置distributor_snowflake对象is_deleted字段值
     */
    @ColumnDef(type = "bigint(20)")
    public transient final KeyValue<SnowflakeTableMap> is_deleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置distributor_snowflake对象machine_ip字段值
     */
    @ColumnDef(type = "varchar(15)")
    public transient final KeyValue<SnowflakeTableMap> machine_ip = new KeyValue(this, Column.machine_ip);
    /**
     * 设置distributor_snowflake对象machine_no字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<SnowflakeTableMap> machine_no = new KeyValue(this, Column.machine_no);
    /**
     * 设置distributor_snowflake对象trade_type字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<SnowflakeTableMap> trade_type = new KeyValue(this, Column.trade_type);

    public SnowflakeTableMap() {
        super();
    }

    public SnowflakeTableMap(int size) {
        super(size);
    }

    /**
     * 创建SnowflakeTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     */
    public SnowflakeTableMap init() {
        this.id.autoIncrease();
        this.gmt_create.values(new Date());
        this.gmt_modified.values(new Date());
        return this;
    }

    public SnowflakeTableMap with(Consumer<SnowflakeTableMap> init) {
        init.accept(this);
        return this;
    }

    public static SnowflakeTableMap create() {
        return new SnowflakeTableMap(1);
    }

    public static SnowflakeTableMap create(int size) {
        return new SnowflakeTableMap(size);
    }

    public static class Factory {
        public SnowflakeTableMap create() {
            return SnowflakeTableMap.create();
        }

        public SnowflakeTableMap create(int size) {
            return SnowflakeTableMap.create(size);
        }

        public SnowflakeTableMap createWithInit() {
            return SnowflakeTableMap.create(1).init();
        }

        public SnowflakeTableMap createWithInit(int size) {
            return SnowflakeTableMap.create(size).init();
        }
    }
}