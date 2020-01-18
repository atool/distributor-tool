package cn.atool.distributor.snowflake.builder;

import cn.atool.distributor.snowflake.model.SnowFlakeGenerator;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import cn.atool.distributor.util.HostHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 雪花算法idc&machine分配构造器基类
 *
 * @author darui.wu
 * @create 2019-09-20 17:19
 */
public abstract class BaseSnowFlakeBuilder implements SnowFlakeBuilder {
    @Getter
    @Setter
    private List<SnowFlakeProp> snowFlakeProps = new ArrayList<>();
    /**
     * 本机所处的数据中心序号
     */
    @Setter
    private int idcNo = 0;

    @Getter
    private String machineIp = HostHelper.getIpAddress();

    @Getter
    private Map<String, Integer> machineNos = new HashMap<>();

    private Map<String, SnowFlakeGenerator> instances = new HashMap<String, SnowFlakeGenerator>();

    public void setSnowFlakeProps(List<SnowFlakeProp> snowFlakeProps) {
        this.snowFlakeProps = new ArrayList<>();
        for (SnowFlakeProp prop : snowFlakeProps) {
            this.snowFlakeProps.add(prop);
        }
    }

    /**
     * 如果用spring初始化, 本方法为init-method方法
     *
     * @throws Exception
     */
    @Override
    public void reset() throws Exception {
        if (this.snowFlakeProps == null || this.snowFlakeProps.size() == 0) {
            throw new RuntimeException("the property of snowflake can't bu null.");
        }
        this.machineNos = new HashMap<>();
        this.instances = new HashMap<>();
        this.initMachineNo();
        for (SnowFlakeProp prop : snowFlakeProps) {
            prop.init(this.idcNo, this.findMachineNo(prop.getTradeType()));
            instances.put(prop.findTradeType(), new SnowFlakeGenerator(prop));
        }
    }

    /**
     * 根据配置，初始化分配机器序列号
     *
     * @throws Exception
     */
    protected abstract void initMachineNo() throws Exception;

    /**
     * 返回本机在集群中的序列号
     *
     * @param tradeType 指定的业务类型
     *                  比如订单号， 账单号等
     * @return
     */
    protected final int findMachineNo(String tradeType) {
        if (this.machineNos.containsKey(tradeType)) {
            return this.machineNos.get(tradeType);
        } else {
            throw new RuntimeException("Can't get snowflake machine serial number for group: " + tradeType);
        }
    }

    protected List<String> getTradeTypes() {
        return snowFlakeProps.stream()
                .map(SnowFlakeProp::getTradeType)
                .collect(toList());
    }


    @Override
    public SnowFlakeGenerator findGenerator(String tradeType) {
        if (instances.containsKey(tradeType)) {
            return instances.get(tradeType);
        }
        throw new RuntimeException("not found snowflake generator for tradeType:" + tradeType);
    }

    @Override
    public void addSnowFlakeProperty(SnowFlakeProp snowFlakeProp) {
        this.snowFlakeProps.add(snowFlakeProp);
    }
}
