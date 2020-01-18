package cn.atool.distributor.idempotent.model;

import cn.atool.distributor.serialize.SerializeFactory;
import lombok.Getter;

/**
 * 幂等结果对象
 *
 * @author darui.wu
 * @create 2020/1/15 10:35 上午
 */
public class IdemValue {
    /**
     * 反序列化幂等结果值, 可以为null
     */
    @Getter
    private Object value;

    public IdemValue(String protocol, String value, Class type) {
        this.value = SerializeFactory.protocol(protocol).toObject(value, type);
    }
}
