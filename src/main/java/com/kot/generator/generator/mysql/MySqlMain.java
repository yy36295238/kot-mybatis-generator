package com.kot.generator.generator.mysql;

import com.kot.generator.generator.GeneralBuilder;
import com.kot.generator.utils.MysqlDatabase;
import org.junit.Test;

/**
 * @author YangYu
 */
public class MySqlMain {

    public static GeneralBuilder builder;

    @Test
    public void generalBuilder() throws Exception {
        builder = GeneralBuilder.create(new MysqlDatabase())
                .driver("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://www.test.com:3306/test?serverTimezone=UTC&useSSL=false")
                .username("test")
                .password("123456")
                .author("yangyu")
                .filePath("\\src\\main\\java\\")
                // 默认包路径
                .defaultPackages("com.kot.kotmybatis.biz.mysql.biz")
//                .entityPackages("com.kot.kotmybatis.biz.mysql.biz")
//                .mapperPackages("com.kot.kotmybatis.biz.mysql.biz")
//                .servicePackages("com.kot.kotmybatis.biz.mysql.biz")
                // 忽略前缀
                .ignorePrefix("t_")
                // 表名
                .tables("t_user");
                // 全部表
//              .allTables()
                // 开启swagger注解
//              .enableSwagger();

        // 执行生成
        builder.gen();

    }

}
