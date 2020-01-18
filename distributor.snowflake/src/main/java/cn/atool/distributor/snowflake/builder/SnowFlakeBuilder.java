package cn.atool.distributor.snowflake.builder;

import cn.atool.distributor.snowflake.model.SnowFlakeGenerator;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;

/**
 * @Descriotion: 雪花算法构建器
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019-09-19.
 */
public interface SnowFlakeBuilder {

    /**
     * 返回指定业务类型的雪花算法实例
     *
     * @param tradeType
     * @return
     */
    SnowFlakeGenerator findGenerator(String tradeType);

    /**
     * 增加业务雪花算法配置
     *
     * @param snowFlakeProp
     */
    void addSnowFlakeProperty(SnowFlakeProp snowFlakeProp);

    /**
     * 重置雪花算法机器号分配, 一般用于spring初始化
     * 等同于 InitializingBean#afterPropertiesSet() 方法
     *
     * @throws Exception
     */
    void reset() throws Exception;
}
