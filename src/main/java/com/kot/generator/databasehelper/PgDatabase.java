package com.kot.generator.databasehelper;

import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YangYu
 */
@Slf4j
public class PgDatabase extends DatabaseAbstract {

    private static final String PGSQL = "Select a.attnum,(select description from pg_catalog.pg_description where objoid=a.attrelid and objsubid=a.attnum) as descript ,a.attname,pg_catalog.format_type(a.atttypid,a.atttypmod) as data_type from pg_catalog.pg_attribute a where 1=1 and a.attrelid=(select oid from pg_class where relname='%s' ) and a.attnum>0 and not a.attisdropped order by a.attnum;";
    private static final String PGSQL_PK = "select pg_constraint.conname as pk_name,pg_attribute.attname as colname,pg_type.typname as typename from pg_constraint inner join pg_class on pg_constraint.conrelid = pg_class.oid inner join pg_attribute on pg_attribute.attrelid = pg_class.oid and pg_attribute.attnum = pg_constraint.conkey[1] inner join pg_type on pg_type.oid = pg_attribute.atttypid where pg_class.relname = '%s' and pg_constraint.contype='p'";


    /**
     * 获取表中字段的名称、类型、注释
     */
    @Override
    public List<ColumnInfo> getColumnInfo(String tableName) {
        List<MysqlDatabase.ColumnInfo> columnInfos = new ArrayList<>();
        String tableSql = String.format(PGSQL, tableName);
        try {

            // 主键信息
            final String primaryKey = getPrimaryKey(tableName);

            PreparedStatement ps = conn.prepareStatement(tableSql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String descript = rs.getString("descript");
                String attname = rs.getString("attname");
                String dataType = rs.getString("data_type");
                columnInfos.add(new MysqlDatabase.ColumnInfo(tableName, attname, dataType, descript, attname.equals(primaryKey)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnInfos;
    }


    @Override
    public String getPrimaryKey(String table) throws SQLException {
        String pk = null;
        PreparedStatement pre = conn.prepareStatement(String.format(PGSQL_PK, table));
        ResultSet rs = pre.executeQuery();
        while (rs.next()) {
            pk = rs.getString("colname");
        }
        return pk;

    }
}
