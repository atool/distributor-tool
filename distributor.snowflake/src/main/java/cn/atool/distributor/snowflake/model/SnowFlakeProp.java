package cn.atool.distributor.snowflake.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static cn.atool.distributor.snowflake.model.SnowFlakeConstant.*;

/**
 * 雪花算法属性值
 *
 * @author darui.wu
 * @create 2019-09-17 15:57
 */

@Accessors(chain = true)
public class SnowFlakeProp {
    @Setter
    @Getter
    private String tradeType;
    /**
     * 数据中心保留位数，默认3位，8个数据中心
     */
    @Setter
    private int idcBits = DEFAULT_IDC_BITS;
    /**
     * 机器序列保留位数， 默认9位，允许集群512台机器
     */
    @Setter
    private int machineBits = DEFAULT_MACHINE_BITS;
    /**
     * 数据中心 + 机器序列 占位符
     */
    @Getter
    private long workId;
    /**
     * 最大序列号
     */
    @Getter
    private int maxSequence;

    @Getter
    private boolean init = false;
    /**
     * 雪花算法时间戳开始时间点位，默认2015-01-01 00:00:00.000
     */
    @Getter
    @Setter
    private long startStamp = START_STAMP;

    public SnowFlakeProp() {
    }

    public SnowFlakeProp(String tradeType) {
        this.tradeType = tradeType;
    }


    public SnowFlakeProp init(int idcNo, int machineNo) throws Exception {
        if (this.tradeType == null || this.tradeType.isEmpty()) {
            throw new RuntimeException("the tradeType should be specified.");
        }
        if (!this.tradeType.matches(TRADE_TYPE_REG)) {
            throw new RuntimeException("the tradeType must be match regular expression:" + TRADE_TYPE_REG);
        }
        if (idcBits < MIN_IDC_BITS) {
            throw new IllegalArgumentException("at least 4 data centers need to be maintained, so the idcBits at least 2 bits");
        }
        if (machineBits < MIN_MACHINE_BITS) {
            throw new IllegalArgumentException("within a single cluster requires at least 32 machines, so the machineBis at least 5 bits");
        }
        if ((idcBits + machineBits) > (REMAIN_BITS - MIN_SEQUENCE_BITS)) {
            throw new IllegalArgumentException("the bits of idc and machine should not exceed 12 bits");
        }

        int maxIdcNo = ~(-1 << this.idcBits);
        if (idcNo > maxIdcNo) {
            throw new RuntimeException(String.format("The serial number[%d] assigned to the idc[tradeType=%s] exceeds the maximum value[%d]", idcNo, this.tradeType, maxIdcNo));
        }
        int maxMachineNo = ~(-1 << this.machineBits);
        if (machineNo > maxMachineNo) {
            throw new RuntimeException(String.format("The serial number[%d] assigned to the machine[tradeType=%s] exceeds the maximum value[%d]", machineNo, this.tradeType, maxMachineNo));
        }
        this.workId = idcNo << (REMAIN_BITS - this.idcBits) | machineNo << (REMAIN_BITS - this.idcBits - this.machineBits);
        this.maxSequence = ~(-1 << (REMAIN_BITS - this.idcBits - this.machineBits));
        this.init = true;
        return this;
    }

    public String findTradeType() {
        return this.tradeType;
    }

    /**
     * 机器序列号的最大可能数
     *
     * @return
     */
    public static int maxMachineNo() {
        return ~(-1 << (REMAIN_BITS - MIN_IDC_BITS - MIN_SEQUENCE_BITS));
    }

    public int getMaxMachineNo() {
        return ~(-1 << this.machineBits);
    }
}
