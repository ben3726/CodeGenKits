package com.github.cg.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class Config {

    private static final String DB_PATH = "cg/cg_database";
    private static final String CFG_PATH = "cg/cg_config";
    private static ResourceBundle DB_INFO = getProperty(DB_PATH);
    private static ResourceBundle CFG_INFO;
    //a
    public static String dbType;
    //b
    public static String diverName;
    //c
    public static String url;
    //d
    public static String username;
    //e
    public static String password;
    //f
    public static String databaseName;
    //g
    public static String projectPath;
    //h
    public static String bussiPackage;
    //i
    public static String sourceRootPackage;
    public static String resourcesRootPackage;
    //j
    public static String webRootPackage;
    //k
    public static String templatePath;
    //l
    public static boolean dbFiledConvert;
    //m
    public static String dbTableId;
    //n
    public static String n;
    //o
    public static String pageSearchFiledNum;
    //p
    public static String pageFilterFields;
    //q
    public static String fieldRowNum;

    public Config() {
    }

    private static ResourceBundle getProperty(String fileName) {
        PropertyResourceBundle propertyResourceBundle = null;
        BufferedInputStream bufferedInputStream = null;
        String configPath =
                System.getProperty("user.dir") + File.separator + "config" + File.separator + fileName + ".properties";

        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(configPath));
            propertyResourceBundle = new PropertyResourceBundle(bufferedInputStream);
            bufferedInputStream.close();
            if (propertyResourceBundle != null) {
                log.debug("JAR方式部署，通过config目录读取配置：" + configPath);
            }
        } catch (IOException var13) {
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return propertyResourceBundle;
    }

    // a
    public static final String getDiverName() {
        return DB_INFO.getString("diver_name");
    }

    // b
    public static final String getUrl() {
        return DB_INFO.getString("url");
    }

    //c
    public static final String getUsername() {
        return DB_INFO.getString("username");
    }

    //d
    public static final String getPassword() {
        return DB_INFO.getString("password");
    }

    //e
    public static final String getDatabaseName() {
        return DB_INFO.getString("database_name");
    }

    //f
    public static final boolean isDbFiledConvert() {
        String dbFiledConvert = CFG_INFO.getString("db_filed_convert");
        return !"false".equals(dbFiledConvert);
    }

    //o
    private static String getBusinessPackage() {
        return CFG_INFO.getString("bussi_package");
    }

    //p
    private static String getTemplatePath() {
        return CFG_INFO.getString("templatepath");
    }

    //g
    public static final String getSourceRootPackage() {
        return CFG_INFO.getString("source_root_package");
    }

    public static final String getResourcesRootPackage() {
        return CFG_INFO.getString("resources_root_package");
    }

    //h
    public static final String getWebRootPackage() {
        return CFG_INFO.getString("webroot_package");
    }

    //i
    public static final String getDbTableId() {
        return CFG_INFO.getString("db_table_id");
    }

    //j
    public static final String getPageFilterFields() {
        return CFG_INFO.getString("page_filter_fields");
    }

    //k
    public static final String getPageSearchFiledNum() {
        return CFG_INFO.getString("page_search_filed_num");
    }

    //l
    public static final String getPageFieldRequiredNum() {
        return CFG_INFO.getString("page_field_required_num");
    }

    //m
    public static String getProjectPath() {
        String path = CFG_INFO.getString("project_path");
        if (path != null && !"".equals(path)) {
            projectPath = path;
        }

        return projectPath;
    }

    //a
    public static void setProjectPath(String path) {
        projectPath = path;
    }

    //b
    public static void setTemplatePath(String path) {
        templatePath = path;
    }

    static {
        if (DB_INFO == null) {
            log.debug("通过class目录加载配置文件:{}", DB_PATH);
            DB_INFO = ResourceBundle.getBundle(DB_PATH);
        }

        CFG_INFO = getProperty(CFG_PATH);
        if (CFG_INFO == null) {
            log.debug("通过class目录加载配置文件:{}", CFG_PATH);
            CFG_INFO = ResourceBundle.getBundle(CFG_PATH);
        }

        dbType = "mysql";
        diverName = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/cg?useUnicode=true&characterEncoding=UTF-8";
        username = "root";
        password = "123456";
        databaseName = "cg";
        projectPath = "c:/workspace/cg";
        bussiPackage = "com.cg";
        sourceRootPackage = "src";
        resourcesRootPackage = "res";
        webRootPackage = "WebRoot";
        templatePath = "/cg/code-template/";
        dbFiledConvert = true;
        n = "4";
        pageSearchFiledNum = "3";
        fieldRowNum = "1";
        diverName = getDiverName();
        url = getUrl();
        username = getUsername();
        password = getPassword();
        databaseName = getDatabaseName();
        sourceRootPackage = getSourceRootPackage();
        resourcesRootPackage = getResourcesRootPackage();
        webRootPackage = getWebRootPackage();
        bussiPackage = getBusinessPackage();
        templatePath = getTemplatePath();
        projectPath = getProjectPath();
        dbTableId = getDbTableId();
        dbFiledConvert = isDbFiledConvert();
        pageFilterFields = getPageFilterFields();
        pageSearchFiledNum = getPageSearchFiledNum();
        if (url.indexOf("mysql") < 0 && url.indexOf("MYSQL") < 0) {
            if (url.indexOf("oracle") < 0 && url.indexOf("ORACLE") < 0) {
                if (url.indexOf("postgresql") < 0 && url.indexOf("POSTGRESQL") < 0) {
                    if (url.indexOf("sqlserver") >= 0 || url.indexOf("sqlserver") >= 0) {
                        dbType = "sqlserver";
                    }
                } else {
                    dbType = "postgresql";
                }
            } else {
                dbType = "oracle";
            }
        } else {
            dbType = "mysql";
        }

        sourceRootPackage = sourceRootPackage.replace(".", "/");
        webRootPackage = webRootPackage.replace(".", "/");
    }
}
