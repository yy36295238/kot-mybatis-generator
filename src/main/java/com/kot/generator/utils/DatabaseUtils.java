package com.kot.generator.utils;

import com.kot.generator.generator.GeneralBuilder;
import com.kot.generator.generator.Main;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YangYu
 */
@Slf4j
public class DatabaseUtils {

    private static GeneralBuilder builder = Main.builder;

    private static final String SQL = "select * from %s where 1=1";

    /**
     * 获取数据库连接
     */
    private static Connection getConnection() {
        try {
            Class.forName(Main.builder.driver);
        } catch (ClassNotFoundException e) {
            log.error("can not load jdbc driver", e);
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Main.builder.url, Main.builder.username, Main.builder.password);
        } catch (SQLException e) {
            log.error("get connection failure", e);
        }
        return conn;
    }


    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs;
        //获取数据库的元数据
        try {
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
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
    public static List<ColumnInfo> getColumnInfo(String tableName) {
        List<ColumnInfo> columnInfos = new ArrayList<>();
        Connection conn = getConnection();
        String tableSql = String.format(SQL, tableName);
        try {

            // 主键信息
            final String primaryKey = getPrimaryKey(conn, tableName);

            PreparedStatement ps = conn.prepareStatement(tableSql);
            final ResultSetMetaData rsMetaData = ps.getMetaData();
            int i = 1;

            ResultSet rs = ps.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                final String columnName = rsMetaData.getColumnName(i);
                columnInfos.add(new ColumnInfo(tableName, columnName, rsMetaData.getColumnTypeName(i), rs.getString("Comment"), columnName.equals(primaryKey)));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnInfos;
    }

    public static String getPrimaryKey(Connection con, String table) {

        String sql = "SHOW CREATE TABLE " + table;
        try {
            PreparedStatement pre = con.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                //正则匹配数据
                Pattern pattern = Pattern.compile("PRIMARY KEY \\(\\`(.*)\\`\\)");
                Matcher matcher = pattern.matcher(rs.getString(2));
                matcher.find();
                String data = matcher.group();
                //过滤对于字符
                data = data.replaceAll("\\`|PRIMARY KEY \\(|\\)", "");
                return data;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
