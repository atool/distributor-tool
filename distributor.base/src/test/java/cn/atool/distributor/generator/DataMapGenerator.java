package cn.atool.distributor.generator;

import cn.org.atool.fluent.mybatis.generator.MybatisGenerator;
import cn.org.atool.fluent.mybatis.generator.TableConvertor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DataMapGenerator {
    private static String url = "jdbc:mysql://localhost:3306/atool?characterEncoding=utf8";

    @Disabled
    @Test
    public void generate() {
        String outdir = System.getProperty("user.dir") + "/target/generator/";
        new MybatisGenerator("cn")
                .setOutputDir(outdir, outdir, outdir)
                .setEntitySetChain(true)
                .setDataSource(url, "root", "password")
                .generate(new TableConvertor("distributor_")
                        .addTable("distributor_retry_event")
                        .addTable("distributor_idempotent")
                        .allTable(table -> table.setLogicDeletedColumn("is_deleted")
                                .setGmtCreateColumn("gmt_create")
                                .setGmtModifiedColumn("gmt_modified"))
                );
    }
}