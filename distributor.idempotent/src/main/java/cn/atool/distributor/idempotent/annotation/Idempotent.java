package cn.atool.distributor.idempotent.annotation;

import cn.atool.distributor.idempotent.model.IdemConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cn.atool.distributor.serialize.SerializeProtocol.FAST_JSON;

/**
 * @Descriotion: 幂等注解，定义在需要幂等方法上
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Idempotent {
    /**
     * 幂等类型，必须保证业务长时间的一致性，一旦重命名，需要同步更新幂等表
     * 默认使用方法名称
     *
     * @return
     */
    String type() default "";

    /**
     * 幂等键值构成，支持el表达式
     *
     * @return
     */
    String key();

    /**
     * 幂等持久化处理bean，必须实现接口 {@code IdempotentPersistence}
     * 默认采用数据库形式持久化
     *
     * @return
     */
    String persistence() default IdemConstant.Idem_Db_Persistence_Bean;

    /**
     * 幂等处理超时时长(单位秒)，默认一年（365天）
     * 对于严格的幂等事件，不能简单认为超时可以重试的业务，需要将时间设为无限长(比如一年），等待人工介入处理
     *
     * @return
     */
    int timeout() default IdemConstant.A_Year;

    /**
     * 幂等信息保留时长(单位秒)，默认365天
     *
     * @return
     */
    int retainTime() default IdemConstant.A_Year;

    /**
     * 序列化协议
     *
     * @return
     */
    String protocol() default FAST_JSON;
}
