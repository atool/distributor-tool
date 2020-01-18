package cn.atool.distributor.fortest;

import lombok.Data;

/**
 *
 * @author darui.wu
 * @create 2020/1/15 2:35 下午
 */
@Data
public class ForTestClazz {
    private String name;

    private long number;

    public ForTestClazz(String name, long number) {
        this.name = name;
        this.number = number;
    }
}
