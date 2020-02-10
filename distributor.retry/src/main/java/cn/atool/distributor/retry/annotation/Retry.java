package cn.atool.distributor.retry.annotation;

import cn.atool.distributor.retry.model.RetryConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cn.atool.distributor.serialize.SerializeProtocol.FAST_JSON;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/1/8.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    /**
     * 重试事件分类
     *
     * @return
     */
    String category();

    /**
     * 重试事件key值构造 ongl表达式
     * 如果没有指定，使用参数的md5值
     *
     * @return
     */
    String key() default "";

    /**
     * 异步重试，消息持久化保存bean
     *
     * @return
     */
    String persistence() default RetryConstant.Retry_Db_Persistence_Bean;

    /**
     * 重试参数序列化协议
     *
     * @return
     */
    String protocol() default FAST_JSON;

    /**
     * 本地最大重试次数, 默认本地不重试
     *
     * @return
     */
    int localMaxRetry() default 0;

    /**
     * 异步最大重试次数, 设置为0表示不用异步重试
     *
     * @return
     */
    int asyncMaxRetry() default 3;

    /**
     * 需要重试的异常, 为空表示所有异常都需要重试
     *
     * @return
     */
    Class<Throwable>[] includes() default {};

    /**
     * 不需要重试的异常, 为空表示所有异常都需要重试
     *
     * @return
     */
    Class<Throwable>[] excludes() default {};
}
