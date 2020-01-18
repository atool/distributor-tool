package cn.atool.distributor.idempotent.fortest.datamap;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.atool.distributor.idempotent.fortest.datamap.IdempotentMP.Column;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @author generate code
 * @ClassName IdempotentTableMap
 * @Description IdempotentTableMap
 */
@TableName(IdempotentMP.Table_Name)
public class IdempotentTableMap extends DataMap<IdempotentTableMap> {
    /**
     * 设置atool_idempotent对象id字段值
     */
    @ColumnDef(type = "bigint(20)", primary = PrimaryType.AutoIncrease)
    public transient final KeyValue<IdempotentTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置atool_idempotent对象gmt_create字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<IdempotentTableMap> gmt_create = new KeyValue(this, Column.gmt_create);
    /**
     * 设置atool_idempotent对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<IdempotentTableMap> gmt_modified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置atool_idempotent对象is_deleted字段值
     */
    @ColumnDef(type = "bigint(20)")
    public transient final KeyValue<IdempotentTableMap> is_deleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置atool_idempotent对象expire_time字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<IdempotentTableMap> expire_time = new KeyValue(this, Column.expire_time);
    /**
     * 设置atool_idempotent对象idem_key字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<IdempotentTableMap> idem_key = new KeyValue(this, Column.idem_key);
    /**
     * 设置atool_idempotent对象idem_status字段值
     */
    @ColumnDef(type = "varchar(10)")
    public transient final KeyValue<IdempotentTableMap> idem_status = new KeyValue(this, Column.idem_status);
    /**
     * 设置atool_idempotent对象idem_type字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<IdempotentTableMap> idem_type = new KeyValue(this, Column.idem_type);
    /**
     * 设置atool_idempotent对象idem_value字段值
     */
    @ColumnDef(type = "varchar(4000)")
    public transient final KeyValue<IdempotentTableMap> idem_value = new KeyValue(this, Column.idem_value);
    /**
     * 设置atool_idempotent对象protocol字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<IdempotentTableMap> protocol = new KeyValue(this, Column.protocol);

    public IdempotentTableMap() {
        super();
    }

    public IdempotentTableMap(int size) {
        super(size);
    }

    /**
     * 创建IdempotentTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     */
    public IdempotentTableMap init() {
        this.id.autoIncrease();
        this.gmt_create.values(new Date());
        this.gmt_modified.values(new Date());
        return this;
    }

    public IdempotentTableMap with(Consumer<IdempotentTableMap> init) {
        init.accept(this);
        return this;
    }

    public static IdempotentTableMap create() {
        return new IdempotentTableMap(1);
    }

    public static IdempotentTableMap create(int size) {
        return new IdempotentTableMap(size);
    }

    public static class Factory {
        public IdempotentTableMap create() {
            return IdempotentTableMap.create();
        }

        public IdempotentTableMap create(int size) {
            return IdempotentTableMap.create(size);
        }

        public IdempotentTableMap createWithInit() {
            return IdempotentTableMap.create(1).init();
        }

        public IdempotentTableMap createWithInit(int size) {
            return IdempotentTableMap.create(size).init();
        }
    }
}