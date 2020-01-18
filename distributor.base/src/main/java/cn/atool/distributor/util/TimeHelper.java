package cn.atool.distributor.util;

import java.text.SimpleDateFormat;

/**
 * @author darui.wu
 * @create 2020/1/15 7:06 下午
 */
public class TimeHelper {
    /**
     * 时间字符串（"yyyy-MM-dd HH:mm:ss.SSS"）转换为毫秒
     *
     * @param timeStr
     * @return
     */
    public static long time2long(String timeStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(timeStr).getTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
