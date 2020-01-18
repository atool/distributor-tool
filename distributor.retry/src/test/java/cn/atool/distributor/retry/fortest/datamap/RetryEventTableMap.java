package cn.atool.distributor.retry.fortest.datamap;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @author generate code
 * @ClassName RetryEventTableMap
 * @Description RetryEventTableMap
 */
@TableName(RetryEventMP.Table_Name)
public class RetryEventTableMap extends DataMap<RetryEventTableMap> {
    /**
     * 设置atool_retry_event对象id字段值
     */
    @ColumnDef(type = "bigint(20)", primary = PrimaryType.AutoIncrease)
    public transient final KeyValue<RetryEventTableMap> id = new KeyValue(this, RetryEventMP.Column.id);
    /**
     * 设置atool_retry_event对象gmt_create字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<RetryEventTableMap> gmt_create = new KeyValue(this, RetryEventMP.Column.gmt_create);
    /**
     * 设置atool_retry_event对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<RetryEventTableMap> gmt_modified = new KeyValue(this, RetryEventMP.Column.gmt_modified);
    /**
     * 设置atool_retry_event对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(4)")
    public transient final KeyValue<RetryEventTableMap> is_deleted = new KeyValue(this, RetryEventMP.Column.is_deleted);
    /**
     * 设置atool_retry_event对象err_message字段值
     */
    @ColumnDef(type = "varchar(2000)")
    public transient final KeyValue<RetryEventTableMap> err_message = new KeyValue(this, RetryEventMP.Column.err_message);
    /**
     * 设置atool_retry_event对象has_retry字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<RetryEventTableMap> has_retry = new KeyValue(this, RetryEventMP.Column.has_retry);
    /**
     * 设置atool_retry_event对象max_retry字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<RetryEventTableMap> max_retry = new KeyValue(this, RetryEventMP.Column.max_retry);
    /**
     * 设置atool_retry_event对象method_args字段值
     */
    @ColumnDef(type = "text")
    public transient final KeyValue<RetryEventTableMap> method_args = new KeyValue(this, RetryEventMP.Column.method_args);
    /**
     * 设置atool_retry_event对象method_signature字段值
     */
    @ColumnDef(type = "varchar(1000)")
    public transient final KeyValue<RetryEventTableMap> method_signature = new KeyValue(this, RetryEventMP.Column.method_signature);
    /**
     * 设置atool_retry_event对象protocol字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<RetryEventTableMap> protocol = new KeyValue(this, RetryEventMP.Column.protocol);
    /**
     * 设置atool_retry_event对象retry_key字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<RetryEventTableMap> retry_key = new KeyValue(this, RetryEventMP.Column.retry_key);
    /**
     * 设置atool_retry_event对象retry_status字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<RetryEventTableMap> retry_status = new KeyValue(this, RetryEventMP.Column.retry_status);
    /**
     * 设置atool_retry_event对象target_bean字段值
     */
    @ColumnDef(type = "varchar(50)")
    public transient final KeyValue<RetryEventTableMap> target_bean = new KeyValue(this, RetryEventMP.Column.target_bean);
    /**
     * 设置atool_retry_event对象target_class字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<RetryEventTableMap> target_class = new KeyValue(this, RetryEventMP.Column.target_class);
    /**
     * 设置atool_retry_event对象target_method字段值
     */
    @ColumnDef(type = "varchar(50)")
    public transient final KeyValue<RetryEventTableMap> target_method = new KeyValue(this, RetryEventMP.Column.target_method);

    public RetryEventTableMap() {
        super();
    }

    public RetryEventTableMap(int size) {
        super(size);
    }

    /**
     * 创建RetryEventTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
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