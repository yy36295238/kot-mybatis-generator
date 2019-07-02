package com.kot.generator.generator;


import com.kot.generator.utils.CommonUtils;
import com.kot.generator.databasehelper.DatabaseAbstract;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class GeneralBuilder {

    private DatabaseAbstract databaseAbstract;

    public static String driver;
    public static String url;
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

}

