package cn.atool.distributor.idempotent.model;

import cn.atool.distributor.idempotent.annotation.Idempotent;
import lombok.Data;

/**
 * @author darui.wu
 * @create 2019/12/19 4:51 下午
 */
@Data
public class IdemBody {
    /**
     * 幂等业务类型
     */
    private String type;
    /**
     * 业务幂等键
     */
    private String key;
    /**
     * 被幂等方法返回值类型
     */
    private Class valueType;
    /**
     * 序列化协议
     */
    private String protocol;
    /**
     * 幂等处理超时时长
     */
    private int timeout;
    /**
     * 幂等结果保留时长
     */
    private int retainTime;

    public IdemBody(Idempotent idempotent) {
        this.protocol = idempotent.protocol();
        this.timeout = idempotent.timeout();
        this.retainTime = idempotent.retainTime();
    }
}
