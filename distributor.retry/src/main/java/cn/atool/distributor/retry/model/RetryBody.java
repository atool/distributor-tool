package cn.atool.distributor.retry.model;

import cn.atool.distributor.retry.service.database.RetrySql;
import cn.atool.distributor.retry.exception.RetryException;
import cn.atool.distributor.serialize.SerializeFactory;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 重试方法参数
 *
 * @author darui.wu
 * @create 2020/1/8 3:12 下午
 */
@Data
@Accessors(chain = true)
public class RetryBody {
    private transient long id;
    /**
     * 重试事件分类标识
     */
    private transient String retryCategory;
    /**
     * 重试事件幂等键值
     */
    private transient String retryKey;
    /**
     * 重试方法签名
     */
    private transient String signature;
    /**
     * 参数序列化协议
     */
    private transient String protocol;
    /**
     * json序列化后的参数列表
     */
    private transient String argsStr;
    /**
     * 重试参数列表
     */
    private List<MethodArg> args;
    /**
     * 聚合数量
     */
    private long summary;

    public RetryBody() {
    }

    private RetryBody(String protocol) {
        this.protocol = protocol;
    }

    public RetryBody(Map<String, Object> map) {
        this((String) map.get(RetrySql.Field_Protocol));
        this.setArgs((String) map.get(RetrySql.Field_Method_Args))
                .setId((Long) map.get(RetrySql.Field_Id))
                .setRetryCategory((String) map.get(RetrySql.Field_Retry_Category))
                .setRetryKey((String) map.get(RetrySql.Field_Retry_Key))
                .setSignature((String) map.get(RetrySql.Field_Method_Signature));
    }

    public RetryBody(String category, String retryKey, List<MethodArg> args, String protocol) {
        this.retryCategory = category;
        this.retryKey = retryKey;
        this.args = args;
        this.signature = this.signature();
        this.protocol = protocol;
        this.argsStr = SerializeFactory.protocol(protocol).toString(args);
    }

    /**
     * 拼装方法签名
     *
     * @return
     */
    private String signature() {
        StringBuilder buff = new StringBuilder();
        boolean first = true;
        for (MethodArg arg : this.args) {
            buff.append(first ? "(" : ",");
            first = false;
            buff.append(arg.getType());
        }
        return buff.append(")").toString();
    }

    /**
     * 将参数列表转换为实例数组
     *
     * @return
     */
    public Object[] toObjects() {
        try {
            Object[] values = new Object[args.size()];
            for (int index = 0; index < args.size(); index++) {
                values[index] = args.get(index).toObject(this.protocol);
            }
            return values;
        } catch (Exception e) {
            throw new RetryException(e);
        }
    }

    /**
     * 重试方法入参反序列化
     *
     * @param args
     * @return
     */
    public RetryBody setArgs(String args) {
        this.args = SerializeFactory.protocol(protocol).toList(args, MethodArg.class);
        this.argsStr = args;
        return this;
    }
}
