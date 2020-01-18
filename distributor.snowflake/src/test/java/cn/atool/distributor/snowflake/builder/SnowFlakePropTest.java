package cn.atool.distributor.snowflake.builder;


import cn.atool.distributor.snowflake.model.SnowFlakeProp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019-09-25.
 */
public class SnowFlakePropTest extends Test4J {

    @Test
    public void validateTradeType() throws Exception {
        try {
            new SnowFlakeProp().init(1, 1);
            want.fail();
        } catch (Exception e) {
            want.string(e.getMessage()).contains("the tradeType should be specified");
        }
    }

    @ParameterizedTest
    @MethodSource("data4validateTradeType2")
    public void validateTradeType2(String tradeType) throws Exception {
        try {
            new SnowFlakeProp().setTradeType(tradeType).init(1, 1);
            want.fail();
        } catch (Exception e) {
            want.string(e.getMessage()).contains("match regular expression:[a-zA-Z0-9_]+");
        }
    }

    public static DataProvider data4validateTradeType2() {
        return new DataProvider()
                .data(" ")
                .data("123#ddd")
                .data("adf ")
                ;
    }

    @ParameterizedTest
    @MethodSource("data4validateIdcAndMachine")
    public void validateIdcAndMachine(int idc, int machine, String err) {
        try {
            new SnowFlakeProp().setTradeType("default").setIdcBits(idc).setMachineBits(machine).init(1, 1);
            want.fail();
        } catch (Exception e) {
            want.string(e.getMessage()).contains(err);
        }
    }

    public static DataProvider data4validateIdcAndMachine() {
        return new DataProvider()
                .data(0, 12, "the idcBits at least 2 bits")
                .data(1, 12, "the idcBits at least 2 bits")
                .data(12, 0, "so the machineBis at least 5 bits")
                .data(3, 1, "so the machineBis at least 5 bits")
                .data(3, 2, "so the machineBis at least 5 bits")
                .data(3, 3, "so the machineBis at least 5 bits")
                .data(3, 4, "so the machineBis at least 5 bits")
                .data(3, 10, "should not exceed 12 bits")
                .data(8, 5, "should not exceed 12 bits")
                ;
    }

    @Test
    public void testMaxMachineNo() {
        int max = SnowFlakeProp.maxMachineNo();
        want.number(max).eq(1024 - 1);
    }
}