package cn.atool.distributor.snowflake.builder.database;

import cn.atool.distributor.snowflake.builder.BaseSnowFlakeBuilder;
import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.List;


/**
 * 使用zookeeper来分配集群中的机器序号
 *
 * @author darui.wu
 * @create 2019-09-18 13:37
 */
@Slf4j
public class DbSnowFlakeBuilder extends BaseSnowFlakeBuilder {
    private DataSource dataSource;

    public DbSnowFlakeBuilder(DataSource dataSource, List<SnowFlakeProp> props) {
        this.dataSource = dataSource;
        super.setSnowFlakeProps(props);
    }

    @Override
    protected void initMachineNo() {
        SnowFlakeDao snowFlakeDao = new SnowFlakeDao(dataSource);
        super.getMachineNos().putAll(snowFlakeDao.findExistMachineNo(super.getTradeTypes(), super.getMachineIp()));
        super.getSnowFlakeProps().stream()
                .forEach(prop -> this.applyMachineNo(snowFlakeDao, prop));
        snowFlakeDao.updateHeartByIp(super.getMachineIp(), super.getMachineNos().keySet());
    }

    /**
     * 新申请机器号
     *
     * @param prop
     * @return
     */
    private void applyMachineNo(SnowFlakeDao snowFlakeDao, SnowFlakeProp prop) {
        if (super.getMachineNos().containsKey(prop.getTradeType())) {
            return;
        }
        String localIP = super.getMachineIp();
        String tradeType = prop.getTradeType();
        List<Integer> machineNos = snowFlakeDao.findTradeMachineNos(tradeType);
        for (int machineNo = 0; machineNo <= prop.getMaxMachineNo(); machineNo++) {
            if (machineNos.contains(machineNo)) {
                continue;
            }
            try {
                snowFlakeDao.createMachineNo(tradeType, localIP, machineNo);
                super.getMachineNos().put(prop.getTradeType(), machineNo);
                return;
            } catch (Exception e) {
                log.warn("applyMachineNo[tradeType={}, localIp={}, machineNo={}] fail.", tradeType, localIP, machineNo);
            }
        }
        throw new RuntimeException(String.format("applyMachineNo[tradeType=%s, localIp=%s] fail.", tradeType, localIP));
    }
}
