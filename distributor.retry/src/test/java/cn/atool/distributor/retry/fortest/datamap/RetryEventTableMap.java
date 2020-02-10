package cn.atool.distributor.retry.fortest.datamap;

import cn.atool.distributor.retry.fortest.datamap.RetryEventMP.Column;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @ClassName RetryEventTableMap
 * @Description RetryEventTableMap
 *
 * @author generate code
 */
@TableName(RetryEventMP.Table_Name)
public class RetryEventTableMap extends DataMap<RetryEventTableMap> {
    /**
     * 设置distributor_retry_event对象id字段值
     */
    @ColumnDef(type = "bigint(20)", primary = PrimaryType.AutoIncrease)
    public transient final KeyValue<RetryEventTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置distributor_retry_event对象gmt_create字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<RetryEventTableMap> gmt_create = new KeyValue(this, Column.gmt_create);
    /**
     * 设置distributor_retry_event对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<RetryEventTableMap> gmt_modified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置distributor_retry_event对象is_deleted字段值
     */
    @ColumnDef(type = "bigint(20)")
    public transient final KeyValue<RetryEventTableMap> is_deleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置distributor_retry_event对象err_message字段值
     */
    @ColumnDef(type = "varchar(2000)")
    public transient final KeyValue<RetryEventTableMap> err_message = new KeyValue(this, Column.err_message);
    /**
     * 设置distributor_retry_event对象has_retry字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<RetryEventTableMap> has_retry = new KeyValue(this, Column.has_retry);
    /**
     * 设置distributor_retry_event对象max_retry字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<RetryEventTableMap> max_retry = new KeyValue(this, Column.max_retry);
    /**
     * 设置distributor_retry_event对象method_args字段值
     */
    @ColumnDef(type = "text")
    public transient final KeyValue<RetryEventTableMap> method_args = new KeyValue(this, Column.method_args);
    /**
     * 设置distributor_retry_event对象method_signature字段值
     */
    @ColumnDef(type = "varchar(1000)")
    public transient final KeyValue<RetryEventTableMap> method_signature = new KeyValue(this, Column.method_signature);
    /**
     * 设置distributor_retry_event对象protocol字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<RetryEventTableMap> protocol = new KeyValue(this, Column.protocol);
    /**
     * 设置distributor_retry_event对象retry_category字段值
     */
    @ColumnDef(type = "varchar(50)")
    public transient final KeyValue<RetryEventTableMap> retry_category = new KeyValue(this, Column.retry_category);
    /**
     * 设置distributor_retry_event对象retry_key字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<RetryEventTableMap> retry_key = new KeyValue(this, Column.retry_key);
    /**
     * 设置distributor_retry_event对象retry_status字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<RetryEventTableMap> retry_status = new KeyValue(this, Column.retry_status);

    public RetryEventTableMap() {
        super();
    }

    public RetryEventTableMap(int size) {
        super(size);
    }

    /**
     * 创建RetryEventTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public RetryEventTableMap init() {
        this.id.autoIncrease();
        this.gmt_create.values(new Date());
        this.gmt_modified.values(new Date());
        return this;
    }

    public RetryEventTableMap with(Consumer<RetryEventTableMap> init) {
        init.accept(this);
        return this;
    }

    public static RetryEventTableMap create() {
        return new RetryEventTableMap(1);
    }

    public static RetryEventTableMap create(int size) {
        return new RetryEventTableMap(size);
    }

    public static class Factory {
        public RetryEventTableMap create() {
            return RetryEventTableMap.create();
        }

        public RetryEventTableMap create(int size) {
            return RetryEventTableMap.create(size);
        }

        public RetryEventTableMap createWithInit() {
            return RetryEventTableMap.create(1).init();
        }

        public RetryEventTableMap createWithInit(int size) {
            return RetryEventTableMap.create(size).init();
        }
    }
}