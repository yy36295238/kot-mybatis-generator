package com.kot.generator.generator;


import com.kot.generator.utils.CommonUtils;
import com.kot.generator.utils.DatabaseUtils;

import java.util.Arrays;
import java.util.List;

public class GeneralBuilder {


    public String driver;
    public String url;
    public String username;
    public String password;
    public String author;
    /**
     * 开启swagger注解
     */
    public Boolean enableSwagger;
    /**
     * 生成文件路径
     */
    public String filePath;
    /**
     * 包名称
     */
    public String packages;
    /**
     * 需要生成的表名
     */
    private List<String> tables;

    /**
     * 生成所有表
     */
    public Boolean allTables;


    public GeneralBuilder build() {
        return this;
    }

    public void gen() throws Exception {
        if (allTables) {
            this.tables = DatabaseUtils.getTableNames();
        }
        for (String table : this.tables) {
            table = CommonUtils.capitalName(table);
            new MakeEntity(this, table).makeClass();
            new MakeMapper(this, table).makeClass();
            new MakeService(this, table).makeClass();
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

    public GeneralBuilder packages(String packages) {
        this.packages = packages;
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

    private void print(String msg) {
        System.out.println();
        System.out.println("*********************************");
        System.out.println("** " + msg + " execute success");
        System.out.println("*********************************");
    }

}

