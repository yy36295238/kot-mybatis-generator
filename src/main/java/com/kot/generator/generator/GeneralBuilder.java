package com.kot.generator.generator;


import com.kot.generator.databasehelper.DatabaseAbstract;
import com.kot.generator.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class GeneralBuilder {

    private DatabaseAbstract databaseAbstract;

    public static String driver;
    public static String url;
    public static String database;
    public static String username;
    public static String password;
    public String author;
    /**
     * 开启swagger注解
     */
    public Boolean enableSwagger = false;
    /**
     * 生成文件路径
     */
    public String filePath;
    /**
     * 包名称
     */
    public String packages;
    public String entityPackages;
    public String mapperPackages;
    public String servicePackages;
    public String controllerPackages;
    /**
     * 需要生成的表名
     */
    private List<String> tables;

    /**
     * 生成所有表
     */
    private Boolean allTables = false;

    /**
     * 忽略前缀
     */
    private String ignorePrefix;

    public GeneralBuilder(DatabaseAbstract databaseAbstract) {
        this.databaseAbstract = databaseAbstract;
    }

    public static GeneralBuilder create(DatabaseAbstract databaseAbstract) {
        return new GeneralBuilder(databaseAbstract);
    }

    public void gen() throws Exception {
        // 解析配置
        parseConfig();

        DatabaseAbstract.getConnection();
        if (allTables) {
            this.tables = DatabaseAbstract.getTableNames();
        }

        for (String table : this.tables) {
            String entityName = CommonUtils.capitalName(table);
            if (StringUtils.isNotBlank(ignorePrefix)) {
                entityName = CommonUtils.capitalName(table.replaceFirst(ignorePrefix, ""));
            }

            final List<DatabaseAbstract.ColumnInfo> columnInfo = databaseAbstract.getColumnInfo(table);
            new MakeEntity(this, entityName, columnInfo).makeClass();
            new MakeMapper(this, entityName).makeClass();
            new MakeService(this, entityName).makeClass();
            new MakeController(this, entityName).makeClass();
            print(table);
        }
    }

    public GeneralBuilder driver(String driver) {
        this.driver = driver;
        return this;
    }

    public GeneralBuilder url(String url) {
        this.url = url;
        return this;
    }

    public GeneralBuilder database(String database) {
        this.database = database;
        return this;
    }

    public GeneralBuilder username(String username) {
        this.username = username;
        return this;
    }

    public GeneralBuilder password(String password) {
        this.password = password;
        return this;
    }

    public GeneralBuilder author(String author) {
        this.author = author;
        return this;
    }

    public GeneralBuilder enableSwagger() {
        this.enableSwagger = true;
        return this;
    }

    public GeneralBuilder filePath(String filePath) {
        this.filePath = System.getProperty("user.dir") + filePath;
        System.out.println(filePath);
        return this;
    }

    public GeneralBuilder defaultPackages(String packages) {
        this.packages = packages;
        return this;
    }

    public GeneralBuilder entityPackages(String entityPackages) {
        this.entityPackages = entityPackages;
        return this;
    }

    public GeneralBuilder mapperPackages(String mapperPackages) {
        this.mapperPackages = mapperPackages;
        return this;
    }

    public GeneralBuilder servicePackages(String servicePackages) {
        this.servicePackages = servicePackages;
        return this;
    }

    public GeneralBuilder controllerPackages(String controllerPackages) {
        this.controllerPackages = controllerPackages;
        return this;
    }

    public GeneralBuilder tables(String... tables) {
        this.tables = Arrays.asList(tables);
        return this;
    }

    public GeneralBuilder allTables() {
        this.allTables = true;
        return this;
    }

    public GeneralBuilder ignorePrefix(String prefix) {
        this.ignorePrefix = prefix;
        return this;
    }

    private void print(String msg) {
        System.err.println();
        System.err.println("**************************************");
        System.err.println("** " + msg + " table general success");
        System.err.println("**************************************");
    }

    private void parseConfig() {
        if (isBlank(driver)) {
            if (url.contains("mysql")) {
                GeneralBuilder.driver = "com.mysql.cj.jdbc.Driver";
            } else if (driver.contains("postgresql")) {
                GeneralBuilder.driver = "org.postgresql.Driver";
            } else {
                throw new RuntimeException("未找到数据库驱动[driver]");
            }
        }
        if (isBlank(database)) {
            GeneralBuilder.database = getDatabase();
        }
    }

    private String getDatabase() {
        return url.substring(url.lastIndexOf("/") + 1, url.contains("?") ? url.indexOf("?") : url.length());
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }

}

