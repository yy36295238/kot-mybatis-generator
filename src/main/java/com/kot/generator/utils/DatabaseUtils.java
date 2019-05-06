package com.kot.generator.utils;

import com.kot.generator.generator.Main;
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
public class DatabaseUtils {

    private static final String DRIVER = Main.DRIVER;
    private static final String URL = Main.URL;
    private static final String USERNAME = Main.USERNAME;
    private static final String PASSWORD = Main.PASSWORD;

    private static final String SQL = "select * from %s where 1=1";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     */
    private static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            log.error("get connection failure", e);
        }
        return conn;
    }


    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() throws Exception {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs;
        //获取数据库的元数据
        DatabaseMetaData db = conn.getMetaData();
        //从元数据中获取到所有的表名
        rs = db.getTables(null, null, null, new String[]{"TABLE"});
        while (rs.next()) {
            tableNames.add(rs.getString(3));
        }
        return tableNames;
    }

    /**
     * 获取表中字段的名称、类型、注释
     */
    public static List<ColumnInfo> getColumnInfo(String tableName) throws Exception {
        List<ColumnInfo> columnInfos = new ArrayList<>();
        Connection conn = getConnection();
        String tableSql = String.format(SQL, tableName);
        PreparedStatement ps = conn.prepareStatement(tableSql);
        final ResultSetMetaData metaData = ps.getMetaData();
        int i = 1;

        ResultSet rs = ps.executeQuery("show full columns from " + tableName);
        while (rs.next()) {
            columnInfos.add(new ColumnInfo(metaData.getColumnName(i), metaData.getColumnTypeName(i), rs.getString("Comment")));
            i++;
        }
        return columnInfos;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ColumnInfo {
        private String name;
        private String type;
        private String comment;
    }
}
