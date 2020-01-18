package cn.atool.distributor.snowflake.builder.zookeeper;

import cn.atool.distributor.snowflake.builder.BaseSnowFlakeBuilder;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException.NoNodeException;

/**
 * 使用zookeeper来分配集群中的机器序号
 *
 * @author darui.wu
 * @create 2019-09-18 13:37
 */
@Slf4j
public class ZkSnowFlakeBuilder extends BaseSnowFlakeBuilder {
    @Setter
    private String zkConnection;

    @Setter
    private int sessionTimeout = 3000;

    @Setter
    private int connectionTimeout = 5000;

    @Setter
    private String rootPath = "/cn/atool/distributor/snowflake/";

    @Override
    protected void initMachineNo() throws Exception {
        new ZkClient(this.zkConnection, this.sessionTimeout, this.connectionTimeout, rootPath)
                .applyMachineNos(client -> {
                    super.getSnowFlakeProps()
                            .forEach(prop -> this.applyMachineNo(client, prop));
                });
    }

    /**
     * 先查找是否已经根据业务和机器分配机器号
     * 如果不存在，则新分配一个机器号
     *
     * @param prop
     * @throws Exception
     */
    private void applyMachineNo(ZkClient client, SnowFlakeProp prop) {
        String tradeType = prop.getTradeType();
        String localIp = super.getMachineIp();
        client.createParentPathIfNoExists(tradeType);
        Integer machineNo;
        try {
            machineNo = client.findExistMachineNoBy(tradeType, localIp);
        } catch (NoNodeException e) {
            machineNo = client.createMachineNoInTransaction(tradeType, localIp);
        }
        if (machineNo == null) {
            throw new RuntimeException(String.format("apply SnowFlake MachineNo[tradeType=%s, machineIp=%s] error.", tradeType, localIp));
        } else {
            super.getMachineNos().put(tradeType, machineNo);
        }
    }
}
