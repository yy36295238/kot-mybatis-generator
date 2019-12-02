package com.kot.generator.databasehelper;

import com.kot.generator.generator.GeneralBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YangYu
 */
@Slf4j
public abstract class DatabaseAbstract {

    public static Connection conn = null;


    /**
     * 获取数据库连接
     */
    public static void getConnection() {
        try {
            Class.forName(GeneralBuilder.driver);
        } catch (ClassNotFoundException e) {
            log.error("can not load jdbc driver", e);
        }

        try {
            conn = DriverManager.getConnection(GeneralBuilder.url, GeneralBuilder.username, GeneralBuilder.password);
        } catch (SQLException e) {
            log.error("get connection failure", e);
        }
    }


    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        ResultSet rs;
        //获取数据库的元数据
        try {
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(GeneralBuilder.database, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    /**
     * 获取表中字段的名称、类型、注释
     */
    public abstract List<ColumnInfo> getColumnInfo(String tableName);


    public abstract String getPrimaryKey(String table) throws SQLException;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ColumnInfo {
        private String tableName;
        private String name;
        private String type;
        private String comment;
        private boolean isPrimaryKey;
    }
}
