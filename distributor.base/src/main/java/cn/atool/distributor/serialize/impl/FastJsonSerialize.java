package cn.atool.distributor.serialize.impl;

import cn.atool.distributor.serialize.IObjectSerialize;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * fastjson序列化和反序列化
 *
 * @author darui.wu
 * @create 2020/1/15 11:16 上午
 */
public class FastJsonSerialize implements IObjectSerialize {
    @Override
    public String toString(Object value) {
        return JSON.toJSONString(value);
    }

    @Override
    public <T> T toObject(String value, Class<T> type) {
        return JSON.parseObject(value, type);
    }

    @Override
    public <T> List<T> toList(String value, Class<T> type) {
        return JSON.parseArray(value, type);
    }
}
