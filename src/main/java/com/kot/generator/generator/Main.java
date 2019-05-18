package com.kot.generator.generator;

import org.junit.Test;

/**
 * @author YangYu
 */
public class Main {

    public static GeneralBuilder builder;

    @Test
    public void generalBuilder() throws Exception {
        builder = GeneralBuilder.create()
                .driver("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://www.test.com:3306/test?serverTimezone=UTC&useSSL=false")
                .username("test")
                .password("Test!123S")
                .author("yangyu")
                .filePath("\\src\\main\\java\\")
                .packages("com.kot.kotmybatis.biz")
                // 表名
//                .tables("user")
                // 全部表
                .allTables()
                // 开启swagger注解
                .enableSwagger();
        // 执行生成
        builder.gen();

    }

}
