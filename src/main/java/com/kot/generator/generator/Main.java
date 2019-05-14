package com.kot.generator.generator;

import com.kot.generator.utils.DatabaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author YangYu
 */
@Slf4j
public class Main {

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://www.test.com:3306/test?serverTimezone=UTC&useSSL=false";
    public static final String USERNAME = "test";
    public static final String PASSWORD = "Test!123S";

    static final String AUTHOR = "yangyu";
    // 开启swagger实体注解
    static final boolean ENABLE_SWAGGER = false;

    private static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\";
    private static final String PACKAGE_NAME = "com.kot.kotmybatis.biz";
    private static final String[] TABLES = {"user"};


    @Test
    public void genByTables() throws Exception {
        for (String table : TABLES) {
            new MakeEntity(PACKAGE_NAME, table, FILE_PATH, DatabaseUtils.getColumnInfo(table)).makeClass();
            new MakeMapper(PACKAGE_NAME, table, FILE_PATH).makeClass();
            new MakeService(PACKAGE_NAME, table, FILE_PATH).makeClass();
        }
        System.err.println("===========执行完成=========");
    }

    @Test
    public void genAll() throws Exception {
        for (String table : DatabaseUtils.getTableNames()) {
            new MakeEntity(PACKAGE_NAME, table, FILE_PATH, DatabaseUtils.getColumnInfo(table)).makeClass();
            new MakeMapper(PACKAGE_NAME, table, FILE_PATH).makeClass();
            new MakeService(PACKAGE_NAME, table, FILE_PATH).makeClass();
        }
        System.err.println("===========执行完成=========");
    }


}
