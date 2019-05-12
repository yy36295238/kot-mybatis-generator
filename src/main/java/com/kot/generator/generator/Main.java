package com.kot.generator.generator;

import com.kot.generator.utils.DatabaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

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

    private static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\";
    private static final String PACKAGE_NAME = "com.kot.kotmybatis";
    private static final String[] TABLES = {"user"};


    public static void main(String[] args) throws Exception {
        StopWatch sw = new StopWatch("生成文件");
        sw.start();
        for (String table : TABLES) {
            new MakeEntity(PACKAGE_NAME, table, FILE_PATH, DatabaseUtils.getColumnInfo(table)).makeClass();
            new MakeMapper(PACKAGE_NAME, table, FILE_PATH).makeClass();
            new MakeService(PACKAGE_NAME, table, FILE_PATH).makeClass();
        }
        sw.stop();
        log.info(sw.prettyPrint());
    }


}
