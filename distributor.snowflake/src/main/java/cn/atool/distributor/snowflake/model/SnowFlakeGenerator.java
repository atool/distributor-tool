package cn.atool.distributor.snowflake.model;

import cn.atool.distributor.util.StringHelper;

import static cn.atool.distributor.snowflake.model.SnowFlakeConstant.*;
import static java.lang.Character.MAX_RADIX;

/**
 * 雪花算法
 *
 * @author darui.wu
 * @create 2019-09-17 15:54Ò
 */
public class SnowFlakeGenerator {
    /**
     * 最大序列值：一次计算出，避免重复计算
     */
    private final int maxSequenceValue;
    /**
     * 数据中心+机器根据各自偏移后的占位数
     */
    private final long workId;
    /**
     * 当前序列号
     */
    private long sequence = 0L;
    /**
     * 上次最新时间戳
     */
    private long lastStamp = -1L;

    /**
     * 分配雪花算法保留位数
     */
    public SnowFlakeGenerator(SnowFlakeProp prop) {
        if (!prop.isInit()) {
            throw new RuntimeException("the SnowFlakeProp should be init.");
        }
        this.maxSequenceValue = prop.getMaxSequence();
        this.workId = prop.getWorkId();
    }

    /**
     * 获取下一个雪花ID值
     *
     * @return
     */
    public long nextId() {
        return this.generate();
    }

    /**
     * 获取下一个雪花ID值，并转成固定13位的字符串
     * 字符串只包含小写字符
     *
     * @return
     */
    public String nextId13Char() {
        long id = this.generate();
        String code = Long.toString(id, MAX_RADIX);
        return SUPPLEMENT_STR[13 - code.length()] + code;
    }

    /**
     * 获取下一个雪花ID值，并转成固定13位的字符串
     * 字符串大小写混合
     *
     * @return
     */
    public String nextId11Char() {
        long id = this.generate();
        String code = StringHelper.toString(id);
        return SUPPLEMENT_STR[11 - code.length()] + code;
    }

    /**
     * 批量获取ID
     *
     * @param size 获取大小，最多1万个
     * @return
     */
    public long[] nextId(int size) {
        this.validateBatchSize(size);
        long[] ids = new long[size];
        for (int i = 0; i < size; i++) {
            ids[i] = nextId();
        }
        return ids;
    }

    public String[] nextId13Char(int size) {
        this.validateBatchSize(size);
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = nextId13Char();
        }
        return ids;
    }

    public String[] nextId11Char(int size) {
        this.validateBatchSize(size);
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = nextId11Char();
        }
        return ids;
    }

    private void validateBatchSize(int size) {
        if (size <= 0 || size > MAX_BATCH) {
            String message = String.format("Size can't be greater than %d or less than 0", MAX_BATCH);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 产生下一个ID
     */
    private synchronized long generate() {
        long currentStamp = currentTimeMillis();
        if (currentStamp < lastStamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastStamp - currentStamp));
        }
        this.nextSequence(currentStamp);
        return (this.lastStamp - START_STAMP) << REMAIN_BITS | this.workId | sequence & Long.MAX_VALUE;
    }

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取这一毫秒新的序列号
     * 新的毫秒，序列从0开始
     * 否则序列自增，如果达到最大序列号，自循环到下一毫秒
     *
     * @param currentStamp
     */
    private void nextSequence(long currentStamp) {
        if (currentStamp == lastStamp) {
            this.sequence = (this.sequence + 1) & this.maxSequenceValue;
            if (this.sequence == 0L) {
                this.lastStamp = nextMs(lastStamp);
            }
        } else {
            this.sequence = 0L;
            this.lastStamp = currentStamp;
        }
    }

    /**
     * 阻塞到下一个毫秒
     */
    private long nextMs(long timeStamp) {
        long current = currentTimeMillis();
        while (current <= timeStamp) {
            current = currentTimeMillis();
        }
        return current;
    }
}
