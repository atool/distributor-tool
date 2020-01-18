package cn.atool.distributor.serialize;

import java.util.List;

/**
 * @Descriotion: 对象序列化处理定义
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/1/15.
 */
public interface IObjectSerialize {
    /**
     * 序列化成字符串
     *
     * @param value
     * @return
     */
    String toString(Object value);

    /**
     * 反序列成对象
     *
     * @param value
     * @param type
     * @param <T>
     * @return
     */
    <T> T toObject(String value, Class<T> type);

    /**
     * 反序列化成列表
     *
     * @param value
     * @param type
     * @param <T>
     * @return
     */
    <T> List<T> toList(String value, Class<T> type);
}
