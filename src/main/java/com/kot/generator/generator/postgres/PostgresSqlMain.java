package com.kot.generator.generator.postgres;

import com.kot.generator.generator.GeneralBuilder;
import com.kot.generator.utils.PgDatabase;
import org.junit.Test;

/**
 * @author YangYu
 */
public class PostgresSqlMain {

    public static GeneralBuilder builder;

    @Test
    public void generalBuilder() throws Exception {
        builder = GeneralBuilder.create(new PgDatabase())
                .driver("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/postgres?serverTimezone=UTC&useSSL=false")
                .username("postgres")
                .password("yy123456")
                .author("yangyu")
                .filePath("\\src\\main\\java\\")
                // 默认包路径
                .defaultPackages("com.kot.kotmybatis.pg.biz")
//                .entityPackages("com.kot.kotmybatis.biz")
//                .mapperPackages("com.kot.kotmybatis.biz")
//                .servicePackages("com.kot.kotmybatis.biz")
                // 忽略前缀
                .ignorePrefix("t_")
                // 表名
                .tables("t_user");
                // 全部表
//                .allTables()
                // 开启swagger注解
//                .enableSwagger();
        // 执行生成
        builder.gen();

    }

}
