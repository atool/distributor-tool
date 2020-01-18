package cn.atool.distributor.serialize;

import cn.atool.distributor.serialize.impl.FastJsonSerialize;

import java.util.HashMap;
import java.util.Map;

import static cn.atool.distributor.serialize.SerializeProtocol.FAST_JSON;


/**
 * @author darui.wu
 * @create 2020/1/15 11:14 上午
 */
public class SerializeFactory {

    public static final Map<String, IObjectSerialize> INSTANCES = new HashMap<>();

    /**
     * 注册序列化协议
     */
    static {
        register(FAST_JSON, new FastJsonSerialize());
    }


    public static void register(String protocol, IObjectSerialize serialize) {
        INSTANCES.put(protocol, serialize);
    }

    /**
     * 返回默认协议
     *
     * @return
     */
    public static IObjectSerialize defaultProtocol() {
        return INSTANCES.get(FAST_JSON);
    }

    /**
     * 返回指定协议
     *
     * @param protocol
     * @return
     */
    public static IObjectSerialize protocol(String protocol) {
        if (!INSTANCES.containsKey(protocol)) {
            throw new RuntimeException("not found protocol:" + protocol);
        }
        return INSTANCES.get(protocol);
    }
}
