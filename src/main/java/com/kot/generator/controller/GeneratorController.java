package com.kot.generator.controller;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;
import java.util.List;

/**
 * @Author yangyu
 * @create 2020/4/16 下午3:01
 */

@RestController
public class GeneratorController {

    public String gen() {
        return null;
    }

    public static void main(String[] args) throws JSQLParserException, ParseException {
        String sql = "CREATE TABLE `t_goods` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `good_name` varchar(255) NOT NULL COMMENT '商品名称',\n" +
                "  `num` int(50) NOT NULL DEFAULT '0' COMMENT '总库存',\n" +
                "  `sold` int(11) NOT NULL DEFAULT '0' COMMENT '已售数量',\n" +
                "  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;";

        CreateTable createTable = getCreateTable(sql);

        System.out.println("============表信息=============");
        System.out.println(createTable.getTable().getName());

        System.out.println("============索引信息=============");
        createTable.getIndexes().forEach(System.out::println);
        System.out.println("============字段信息=============");
        createTable.getColumnDefinitions().forEach(System.out::println);
    }

    public static List<ColumnDefinition> getTableNameBySql(String sql) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        List<ColumnDefinition> columnList = null;
        Statement stmt = parser.parse(new StringReader(sql));
        if (stmt instanceof CreateTable) {
            columnList = ((CreateTable) stmt).getColumnDefinitions();
        }
        return columnList;
    }

    public static CreateTable getCreateTable(String sql) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        Statement stmt = parser.parse(new StringReader(sql));
        if (stmt instanceof CreateTable) {
            return (CreateTable) stmt;
        }
        return null;
    }

}
