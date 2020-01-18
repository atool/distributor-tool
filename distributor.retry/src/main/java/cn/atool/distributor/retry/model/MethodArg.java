package cn.atool.distributor.retry.model;

import cn.atool.distributor.serialize.SerializeFactory;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author darui.wu
 * @create 2020/1/15 3:45 下午
 */
@Data
@Accessors(chain = true)
public class MethodArg {
    /**
     * 参数类型
     */
    private String type;
    /**
     * 参数值
     */
    private String value;
    /**
     * 参数值反序列化后的对象实例
     */
    private transient Object valueInstance;

    /**
     * 转换为java对象
     *
     * @param protocol
     * @return
     * @throws Exception
     */
    public Object toObject(String protocol) throws Exception {
        if (valueInstance == null) {
            Class type = Class.forName(this.getType());
            this.valueInstance = SerializeFactory.protocol(protocol).toObject(this.value, type);
        }
        return valueInstance;
    }
}
