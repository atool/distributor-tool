package cn.atool.distributor.idempotent.model;

/**
 * @author darui.wu
 * @create 2020/1/15 5:48 下午
 */
public interface IdemConstant {
    /**
     * 数据库形式持久化bean名称
     */
    String Idem_Db_Persistence_Bean = "idemDbPersistence";

    /**
     * 一年的秒数
     */
    int A_Year = 365 * 24 * 60 * 60;
}
