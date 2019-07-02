package com.kot.generator.databasehelper;

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
public class MysqlDatabase extends DatabaseAbstract {

    private static final String MYSQL = "select * from %s where 1=1";

    /**
     * 获取表中字段的名称、类型、注释
     */
    @Override
    public List<ColumnInfo> getColumnInfo(String tableName) {
        List<ColumnInfo> columnInfos = new ArrayList<>();
        String tableSql = String.format(MYSQL, tableName);
        try {

            // 主键信息
            final String primaryKey = getPrimaryKey(tableName);

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

    @Override
    public String getPrimaryKey(String table) {

        String sql = "SHOW CREATE TABLE " + table;
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
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


}
