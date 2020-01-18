package cn.atool.distributor.snowflake.model;

import cn.atool.distributor.util.TimeHelper;

/**
 * @Descriotion:雪花算法64位bit分配 <pre>
 * (1) + (41) + (2~7) + (5~10)+(10~15)
 * 第一个部分，是 1 个 bit：0，这个是无意义的。
 * 第二个部分是 41 个 bit：表示的是时间戳。
 * 第三个部分是 2~7 个 bit：表示的是机房 id
 * 第四个部分是 5~10 个 bit：表示的是机器 id
 * 第五个部分是 10~15 个 bit：表示的序号，就是某个机房某台机器上这一毫秒内同时生成的 id 的序号
 * </pre>
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/1/15.
 */
public interface SnowFlakeConstant {
    /**
     * 可分配的位数, 也是时间戳位置偏移量
     */
    int REMAIN_BITS = 22;
    /**
     * 数据中心占用的位数默认值
     */
    int DEFAULT_IDC_BITS = 3;
    /**
     * 机器标识占用的位数默认值
     */
    int DEFAULT_MACHINE_BITS = 9;
    /**
     * 机器码至少保留位数
     */
    int MIN_MACHINE_BITS = 5;
    /**
     * idc至少保留位数
     */
    int MIN_IDC_BITS = 2;
    /**
     * 序列号最少保留位数
     * 至少10位, 一毫秒1024个序列号
     */
    int MIN_SEQUENCE_BITS = 10;
    /**
     * 业务类型命名允许字符串正则表达式
     */
    String TRADE_TYPE_REG = "[a-zA-Z0-9_]+";

    /**
     * 补位字符串
     */
    String[] SUPPLEMENT_STR = {"", "0", "00", "000", "0000", "00000",
            "000000", "0000000", "00000000", "000000000", "0000000000",
            "00000000000", "000000000000", "0000000000000"};
    /**
     * 确定雪花算法的起始时间点 1420041600000L
     * 一旦某个业务确定下起始时间不可更改，否则可能导致生成序号顺序错乱和重复
     */
    long START_STAMP = TimeHelper.time2long("2015-01-01 00:00:00.000");
    /**
     * 批量获取最大
     */
    int MAX_BATCH = 10000;
    /**
     * 重置方法名称
     */
    String Reset_Method = "reset";
}
