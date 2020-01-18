package cn.atool.distributor.snowflake.builder.zookeeper;

import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author darui.wu
 * @create 2020/1/17 12:53 下午
 */
public class ZkClient {
    private CuratorFramework framework;

    private String rootPath;

    ZkClient(String zkUrl, int sessionTimeout, int connectionTimeout, String rootPath) {
        if (rootPath == null) {
            throw new RuntimeException("the rootPath can't be null.");
        } else if (!rootPath.startsWith("/")) {
            throw new RuntimeException("the rootPath should be start with character /");
        } else if (!rootPath.endsWith("/")) {
            throw new RuntimeException("the rootPath should be end with character /");
        }
        this.framework = CuratorFrameworkFactory.builder()
                .connectString(zkUrl)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.rootPath = rootPath;
    }

    /**
     * 批量申请机器号
     *
     * @param consumer
     * @throws Exception
     */
    public void applyMachineNos(Consumer<ZkClient> consumer) throws Exception {
        this.framework.start();
        try {
            consumer.accept(this);
        } finally {
            this.framework.close();
        }
    }

    /**
     * 创建业务类型雪花算法机器分配zk路径父节点
     *
     * @param tradeType
     * @throws Exception
     */
    public void createParentPathIfNoExists(String tradeType) {
        String parentPath = rootPath + tradeType + SEQ_NODE_PATH;
        try {
            Stat stat = this.framework.checkExists().forPath(parentPath);
            if (stat == null) {
                try {
                    this.framework.create().creatingParentsIfNeeded().forPath(parentPath);
                } catch (NodeExistsException e) {
                    // 如果已经存在，忽略
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("createParentPathIfNoExists[" + parentPath + "] error:" + e.getMessage(), e);
        }
    }

    /**
     * 根据业务和IP查找已分配的机器号
     *
     * @param tradeType
     * @param machineIp
     * @return
     */
    public int findExistMachineNoBy(String tradeType, String machineIp) throws NoNodeException {
        String path = String.format("%s%s%s/%s", rootPath, tradeType, SEQ_NODE_PATH, machineIp);
        try {
            String seq = new String(this.framework.getData().forPath(path));
            return Integer.parseInt(seq);
        } catch (NoNodeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(String.format("findExistMachineNoBy[path=%s] error:%s", path, e.getMessage()), e);
        }
    }

    /**
     * 如果对应的ip地址没有注册sequenceNo，新建sequenceNo
     *
     * @param tradeType
     * @return
     * @throws Exception
     */
    public int createMachineNoInTransaction(String tradeType, String machineIp) {
        String parentPath = this.rootPath + tradeType + SEQ_NODE_PATH;
        String machineIpPath = parentPath + "/" + machineIp;

        try {
            List<String> list = this.framework.getChildren().forPath(parentPath);
            for (int index = 0; index <= SnowFlakeProp.maxMachineNo(); index++) {
                String indexStr = String.valueOf(index);
                if (list.contains(indexStr)) {
                    continue;
                }
                String machineNoPath = parentPath + "/" + indexStr;
                CuratorOp createMachineIp = this.framework.transactionOp().create().withMode(CreateMode.PERSISTENT)
                        .forPath(machineIpPath, String.valueOf(index).getBytes());
                CuratorOp createMachineNo = this.framework.transactionOp().create().withMode(CreateMode.PERSISTENT)
                        .forPath(machineNoPath, machineIp.getBytes());
                try {
                    this.framework.transaction().forOperations(createMachineIp, createMachineNo);
                    return index;
                } catch (NodeExistsException e1) {
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("createMachineNoInTransaction[tradeType=%s, machineIp=%s] error:%s", tradeType, machineIp, e.getMessage()), e);
        }
        throw new RuntimeException("createMachineNoInTransaction fail.");
    }

    private final static String SEQ_NODE_PATH = "/seq";
}
