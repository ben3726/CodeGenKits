package com.github.cg.database;

import com.github.cg.config.Config;
import com.github.cg.util.ConvertUtils;
import com.github.cg.pojo.ColumnVo;
import com.github.cg.util.CommonUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 数据库表工具类
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class DbReadTableUtil {

    private static Connection b;
    private static Statement c;

    public DbReadTableUtil() {
    }

    public static void main(String[] args) throws SQLException {
        try {
            List var1 = a("demo");
            Iterator var2 = var1.iterator();

            while (var2.hasNext()) {
                ColumnVo var3 = (ColumnVo) var2.next();
                log.info(var3.getFieldName());
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        new DbReadTableUtil();
        log.info(ArrayUtils.toString(a()));
    }

    //a
    public static List<String> a() throws SQLException {
        String var1 = null;
        ArrayList var2 = new ArrayList(0);

        try {
            Class.forName(Config.diverName);
            b = DriverManager
                    .getConnection(Config.url, Config.username,
                            Config.password);
            c = b.createStatement(1005, 1007);
            if ("mysql".equals(Config.dbType)) {
                var1 = MessageFormat
                        .format("select distinct table_name from information_schema.columns where table_schema = {0}",
                                CommonUtils.toString(Config.databaseName));
            }

            if (Config.dbType.equals("oracle")) {
                var1 = " select distinct colstable.table_name as  table_name from user_tab_cols colstable order by colstable.table_name";
            }

            if (Config.dbType.equals("postgresql")) {
                var1 = "select tablename from pg_tables where schemaname='public'";
            }

            if (Config.dbType.equals("sqlserver")) {
                var1 = "select distinct c.name as  table_name from sys.objects c where c.type = 'U' ";
            }

            log.debug("--------------sql-------------" + var1);
            ResultSet var0 = c.executeQuery(var1);

            while (var0.next()) {
                String var3 = var0.getString(1);
                var2.add(var3);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                    c = null;
                    System.gc();
                }

                if (b != null) {
                    b.close();
                    b = null;
                    System.gc();
                }
            } catch (SQLException var11) {
                throw var11;
            }

        }

        return var2;
    }

    //a
    public static List<ColumnVo> a(String var0) throws Exception {
        String var2 = null;
        ArrayList var3 = new ArrayList();

        ColumnVo var6;
        try {
            Class.forName(Config.diverName);
            b = DriverManager.getConnection(Config.url, Config.username,
                    Config.password);
            c = b.createStatement(1005, 1007);
            if (Config.dbType.equals("mysql")) {
                var2 = MessageFormat
                        .format("select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1} order by ORDINAL_POSITION",
                                CommonUtils.toString(var0),
                                CommonUtils.toString(Config.databaseName));
            }

            if (Config.dbType.equals("oracle")) {
                var2 = MessageFormat
                        .format(" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}",
                                CommonUtils.toString(var0.toUpperCase()));
            }

            if (Config.dbType.equals("postgresql")) {
                var2 = MessageFormat
                        .format("select icm.column_name as field,icm.udt_name as type,fieldtxt.descript as comment, icm.numeric_precision_radix as column_precision ,icm.numeric_scale as column_scale ,icm.character_maximum_length as Char_Length,icm.is_nullable as attnotnull from information_schema.columns icm, (SELECT A.attnum,( SELECT description FROM pg_catalog.pg_description WHERE objoid = A.attrelid AND objsubid = A.attnum ) AS descript,A.attname FROM\tpg_catalog.pg_attribute A WHERE A.attrelid = ( SELECT oid FROM pg_class WHERE relname = {0} ) AND A.attnum > 0 AND NOT A.attisdropped  ORDER BY\tA.attnum ) fieldtxt where icm.table_name={1} and fieldtxt.attname = icm.column_name",
                                CommonUtils.toString(var0),
                                CommonUtils.toString(var0));
            }

            if (Config.dbType.equals("sqlserver")) {
                var2 = MessageFormat
                        .format("select distinct cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as NVARCHAR(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable,column_id   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join (select top 1 * from sys.objects where type = '''U''' and name ={0}  order by name) c on a.object_id=c.object_id left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0} order by a.column_id",
                                CommonUtils.toString(var0));
            }

            log.debug("--------------sql-------------" + var2);
            ResultSet var1 = c.executeQuery(var2);
            var1.last();
            int var4 = var1.getRow();
            if (var4 <= 0) {
                throw new Exception("该表不存在或者表中没有字段");
            }

            var6 = new ColumnVo();
            if (Config.dbFiledConvert) {
                var6.setFieldName(e(var1.getString(1).toLowerCase()));
            } else {
                var6.setFieldName(var1.getString(1).toLowerCase());
            }

            var6.setFieldDbName(var1.getString(1).toUpperCase());
            var6.setFieldType(e(var1.getString(2).toLowerCase()));
            var6.setFieldDbType(e(var1.getString(2).toLowerCase()));
            var6.setPrecision(var1.getString(4));
            var6.setScale(var1.getString(5));
            var6.setCharmaxLength(var1.getString(6));
            var6.setNullable(CommonUtils.confirm(var1.getString(7)));
            a(var6);
            var6.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? var6.getFieldName() : var1.getString(3));
            log.debug("columnt.getFieldName() -------------" + var6.getFieldName());
            String[] var7 = new String[0];
            if (Config.pageFilterFields != null) {
                var7 = Config.pageFilterFields.toLowerCase().split(",");
            }

            if (!Config.dbTableId.equals(var6.getFieldName())
                    && !ConvertUtils.contain(var6.getFieldDbName().toLowerCase(), var7)) {
                var3.add(var6);
            }

            while (var1.previous()) {
                ColumnVo var8 = new ColumnVo();
                if (Config.dbFiledConvert) {
                    var8.setFieldName(e(var1.getString(1).toLowerCase()));
                } else {
                    var8.setFieldName(var1.getString(1).toLowerCase());
                }

                var8.setFieldDbName(var1.getString(1).toUpperCase());
                log.debug("columnt.getFieldName() -------------" + var8.getFieldName());
                if (!Config.dbTableId.equals(var8.getFieldName())
                        && !ConvertUtils.contain(var8.getFieldDbName().toLowerCase(), var7)) {
                    var8.setFieldType(e(var1.getString(2).toLowerCase()));
                    var8.setFieldDbType(e(var1.getString(2).toLowerCase()));
                    log.debug("-----po.setFieldType------------" + var8.getFieldType());
                    var8.setPrecision(var1.getString(4));
                    var8.setScale(var1.getString(5));
                    var8.setCharmaxLength(var1.getString(6));
                    var8.setNullable(CommonUtils.confirm(var1.getString(7)));
                    a(var8);
                    var8.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? var8.getFieldName() : var1.getString(3));
                    var3.add(var8);
                }
            }

            log.debug("读取表成功");
        } catch (ClassNotFoundException var17) {
            throw var17;
        } catch (SQLException var18) {
            throw var18;
        } finally {
            try {
                if (c != null) {
                    c.close();
                    c = null;
                    System.gc();
                }

                if (b != null) {
                    b.close();
                    b = null;
                    System.gc();
                }
            } catch (SQLException var16) {
                throw var16;
            }

        }

        ArrayList var20 = new ArrayList();

        for (int var5 = var3.size() - 1; var5 >= 0; --var5) {
            var6 = (ColumnVo) var3.get(var5);
            var20.add(var6);
        }

        return var20;
    }

    //b
    public static List<ColumnVo> b(String var0) throws Exception {
        ResultSet var1 = null;
        String var2 = null;
        ArrayList var3 = new ArrayList();

        ColumnVo var6;
        try {
            Class.forName(Config.diverName);
            b = DriverManager
                    .getConnection(Config.url, Config.username,
                            Config.password);
            c = b.createStatement(1005, 1007);
            if (Config.dbType.equals("mysql")) {
                var2 = MessageFormat
                        .format("select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1} order by ORDINAL_POSITION",
                                CommonUtils.toString(var0),
                                CommonUtils.toString(Config.databaseName));
            }

            if (Config.dbType.equals("oracle")) {
                var2 = MessageFormat
                        .format(" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}",
                                CommonUtils.toString(var0.toUpperCase()));
            }

            if (Config.dbType.equals("postgresql")) {
                var2 = MessageFormat
                        .format("select icm.column_name as field,icm.udt_name as type,fieldtxt.descript as comment, icm.numeric_precision_radix as column_precision ,icm.numeric_scale as column_scale ,icm.character_maximum_length as Char_Length,icm.is_nullable as attnotnull from information_schema.columns icm, (SELECT A.attnum,( SELECT description FROM pg_catalog.pg_description WHERE objoid = A.attrelid AND objsubid = A.attnum ) AS descript,A.attname FROM\tpg_catalog.pg_attribute A WHERE A.attrelid = ( SELECT oid FROM pg_class WHERE relname = {0} ) AND A.attnum > 0 AND NOT A.attisdropped  ORDER BY\tA.attnum ) fieldtxt where icm.table_name={1} and fieldtxt.attname = icm.column_name",
                                CommonUtils.toString(var0),
                                CommonUtils.toString(var0));
            }

            if (Config.dbType.equals("sqlserver")) {
                var2 = MessageFormat
                        .format("select distinct cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as NVARCHAR(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable,column_id   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join (select top 1 * from sys.objects where type = '''U''' and name ={0}  order by name) c on a.object_id=c.object_id left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0} order by a.column_id",
                                CommonUtils.toString(var0));
            }

            var1 = c.executeQuery(var2);
            var1.last();
            int var4 = var1.getRow();
            if (var4 <= 0) {
                throw new Exception("该表不存在或者表中没有字段");
            }

            var6 = new ColumnVo();
            if (Config.dbFiledConvert) {
                var6.setFieldName(e(var1.getString(1).toLowerCase()));
            } else {
                var6.setFieldName(var1.getString(1).toLowerCase());
            }

            var6.setFieldDbName(var1.getString(1).toUpperCase());
            var6.setPrecision(CommonUtils.getValue(var1.getString(4)));
            var6.setScale(CommonUtils.getValue(var1.getString(5)));
            var6.setCharmaxLength(CommonUtils.getValue(var1.getString(6)));
            var6.setNullable(CommonUtils.confirm(var1.getString(7)));
            var6.setFieldType(a(var1.getString(2).toLowerCase(), var6.getPrecision(), var6.getScale()));
            var6.setFieldDbType(e(var1.getString(2).toLowerCase()));
            a(var6);
            var6.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? var6.getFieldName() : var1.getString(3));
            log.debug("columnt.getFieldName() -------------" + var6.getFieldName());
            var3.add(var6);

            while (true) {
                if (!var1.previous()) {
                    log.debug("读取表成功");
                    break;
                }

                ColumnVo var7 = new ColumnVo();
                if (Config.dbFiledConvert) {
                    var7.setFieldName(e(var1.getString(1).toLowerCase()));
                } else {
                    var7.setFieldName(var1.getString(1).toLowerCase());
                }

                var7.setFieldDbName(var1.getString(1).toUpperCase());
                var7.setPrecision(CommonUtils.getValue(var1.getString(4)));
                var7.setScale(CommonUtils.getValue(var1.getString(5)));
                var7.setCharmaxLength(CommonUtils.getValue(var1.getString(6)));
                var7.setNullable(CommonUtils.confirm(var1.getString(7)));
                var7.setFieldType(a(var1.getString(2).toLowerCase(), var7.getPrecision(), var7.getScale()));
                var7.setFieldDbType(e(var1.getString(2).toLowerCase()));
                a(var7);
                var7.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? var7.getFieldName() : var1.getString(3));
                var3.add(var7);
            }
        } catch (ClassNotFoundException var16) {
            throw var16;
        } catch (SQLException var17) {
            throw var17;
        } finally {
            try {
                if (c != null) {
                    c.close();
                    c = null;
                    System.gc();
                }

                if (b != null) {
                    b.close();
                    b = null;
                    System.gc();
                }
            } catch (SQLException var15) {
                throw var15;
            }

        }

        ArrayList var19 = new ArrayList();

        for (int var5 = var3.size() - 1; var5 >= 0; --var5) {
            var6 = (ColumnVo) var3.get(var5);
            var19.add(var6);
        }

        return var19;
    }

    //c
    public static boolean c(String var0) {
        String var2 = null;

        try {
            log.debug("数据库驱动: " + Config.diverName);
            Class.forName(Config.diverName);
            b = DriverManager.getConnection(Config.url, Config.username, Config.password);
            c = b.createStatement(1005, 1007);
            if (Config.dbType.equals("mysql")) {
                var2 = "select column_name,data_type,column_comment,0,0 from information_schema.columns where table_name = '"
                        + var0 + "' and table_schema = '" + Config.databaseName + "'";
            }

            if (Config.dbType.equals("oracle")) {
                var2 = "select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = '"
                        + var0.toUpperCase() + "'";
            }

            if (Config.dbType.equals("postgresql")) {
                var2 = MessageFormat
                        .format("select icm.column_name as field,icm.udt_name as type,fieldtxt.descript as comment, icm.numeric_precision_radix as column_precision ,icm.numeric_scale as column_scale ,icm.character_maximum_length as Char_Length,icm.is_nullable as attnotnull from information_schema.columns icm, (SELECT A.attnum,( SELECT description FROM pg_catalog.pg_description WHERE objoid = A.attrelid AND objsubid = A.attnum ) AS descript,A.attname FROM\tpg_catalog.pg_attribute A WHERE A.attrelid = ( SELECT oid FROM pg_class WHERE relname = {0} ) AND A.attnum > 0 AND NOT A.attisdropped  ORDER BY\tA.attnum ) fieldtxt where icm.table_name={1} and fieldtxt.attname = icm.column_name",
                                CommonUtils.toString(var0),
                                CommonUtils.toString(var0));
            }

            if (Config.dbType.equals("sqlserver")) {
                var2 = MessageFormat
                        .format("select distinct cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as NVARCHAR(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable,column_id   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join (select top 1 * from sys.objects where type = '''U''' and name ={0}  order by name) c on a.object_id=c.object_id left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0} order by a.column_id",
                                CommonUtils.toString(var0));
            }

            ResultSet var1 = c.executeQuery(var2);
            var1.last();
            int var3 = var1.getRow();
            return var3 > 0;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    //e
    private static String e(String var0) {
        String[] var1 = var0.split("_");
        var0 = "";
        int var2 = 0;

        for (int var3 = var1.length; var2 < var3; ++var2) {
            if (var2 > 0) {
                String var4 = var1[var2].toLowerCase();
                var4 = var4.substring(0, 1).toUpperCase() + var4.substring(1, var4.length());
                var0 = var0 + var4;
            } else {
                var0 = var0 + var1[var2].toLowerCase();
            }
        }

        return var0;
    }

    //d
    public static String d(String var0) {
        String[] var1 = var0.split("_");
        var0 = "";
        int var2 = 0;

        for (int var3 = var1.length; var2 < var3; ++var2) {
            if (var2 > 0) {
                String var4 = var1[var2].toLowerCase();
                var4 = var4.substring(0, 1).toUpperCase() + var4.substring(1, var4.length());
                var0 = var0 + var4;
            } else {
                var0 = var0 + var1[var2].toLowerCase();
            }
        }

        var0 = var0.substring(0, 1).toUpperCase() + var0.substring(1);
        return var0;
    }

    //a
    private static void a(ColumnVo var0) {
        String var1 = var0.getFieldType();
        String var2 = var0.getScale();
        var0.setClassType("inputxt");
        if ("N".equals(var0.getNullable())) {
            var0.setOptionType("*");
        }

        if (!"datetime".equals(var1) && !var1.contains("time")) {
            if ("date".equals(var1)) {
                var0.setClassType("easyui-datebox");
            } else if (var1.contains("int")) {
                var0.setOptionType("n");
            } else if ("number".equals(var1)) {
                if (StringUtils.isNotBlank(var2) && Integer.parseInt(var2) > 0) {
                    var0.setOptionType("d");
                }
            } else if (!"float".equals(var1) && !"double".equals(var1) && !"decimal".equals(var1)) {
                if ("numeric".equals(var1)) {
                    var0.setOptionType("d");
                }
            } else {
                var0.setOptionType("d");
            }
        } else {
            var0.setClassType("easyui-datetimebox");
        }

    }

    //a
    private static String a(String var0, String var1, String var2) {
        if (var0.contains("char")) {
            var0 = "java.lang.String";
        } else if (var0.contains("int")) {
            var0 = "java.lang.Integer";
        } else if (var0.contains("float")) {
            var0 = "java.lang.Float";
        } else if (var0.contains("double")) {
            var0 = "java.lang.Double";
        } else if (var0.contains("number")) {
            if (StringUtils.isNotBlank(var2) && Integer.parseInt(var2) > 0) {
                var0 = "java.math.BigDecimal";
            } else if (StringUtils.isNotBlank(var1) && Integer.parseInt(var1) > 10) {
                var0 = "java.lang.Long";
            } else {
                var0 = "java.lang.Integer";
            }
        } else if (var0.contains("decimal")) {
            var0 = "java.math.BigDecimal";
        } else if (var0.contains("date")) {
            var0 = "java.util.Date";
        } else if (var0.contains("time")) {
            var0 = "java.util.Date";
        } else if (var0.contains("blob")) {
            var0 = "byte[]";
        } else if (var0.contains("clob")) {
            var0 = "java.sql.Clob";
        } else if (var0.contains("numeric")) {
            var0 = "java.math.BigDecimal";
        } else {
            var0 = "java.lang.Object";
        }
        return var0;
    }
}
