package com.github.cg.generate.impl;

import com.github.cg.pojo.ColumnVo;
import com.github.cg.pojo.TableVo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import com.github.cg.config.Config;
import com.github.cg.database.DbReadTableUtil;
import com.github.cg.generate.IGenerate;
import com.github.cg.template.CgTemplate;
import com.github.cg.util.NonceUtils;

/**
 * 单表代码生成
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class CodeGenerateOne extends CodeGenerate implements IGenerate {

    private TableVo tableVo;
    private List<ColumnVo> columns;
    private List<ColumnVo> originalColumns;

    public CodeGenerateOne(TableVo tableVo) {
        this.tableVo = tableVo;
    }

    public CodeGenerateOne(TableVo tableVo, List<ColumnVo> columns, List<ColumnVo> originalColumns) {
        this.tableVo = tableVo;
        this.columns = columns;
        this.originalColumns = originalColumns;
    }

    @Override
    public Map<String, Object> loadTemplateData() throws Exception {
        HashMap var1 = new HashMap();
        var1.put("bussiPackage", Config.bussiPackage);
        var1.put("entityPackage", this.tableVo.getEntityPackage());
        var1.put("entityName", this.tableVo.getEntityName());
        var1.put("tableName", this.tableVo.getTableName());
        var1.put("primaryKeyField", Config.dbTableId);
        if (this.tableVo.getFieldRequiredNum() == null) {
            this.tableVo.setFieldRequiredNum(StringUtils.isNotEmpty(Config.n) ? Integer
                    .parseInt(Config.n) : -1);
        }

        if (this.tableVo.getSearchFieldNum() == null) {
            this.tableVo
                    .setSearchFieldNum(StringUtils.isNotEmpty(Config.pageSearchFiledNum) ? Integer
                            .parseInt(Config.pageSearchFiledNum) : -1);
        }

        if (this.tableVo.getFieldRowNum() == null) {
            this.tableVo.setFieldRowNum(Integer.parseInt(Config.fieldRowNum));
        }

        var1.put("tableVo", this.tableVo);

        try {
            if (this.columns == null || this.columns.size() == 0) {
                this.columns = DbReadTableUtil.a(this.tableVo.getTableName());
            }

            var1.put("columns", this.columns);
            if (this.originalColumns == null || this.originalColumns.size() == 0) {
                this.originalColumns = DbReadTableUtil.b(this.tableVo.getTableName());
            }

            var1.put("originalColumns", this.originalColumns);
            Iterator var2 = this.originalColumns.iterator();

            while (var2.hasNext()) {
                ColumnVo var3 = (ColumnVo) var2.next();
                if (var3.getFieldName().toLowerCase().equals(Config.dbTableId.toLowerCase())) {
                    var1.put("primaryKeyPolicy", var3.getFieldType());
                }
            }
        } catch (Exception var4) {
            throw var4;
        }

        long var5 = NonceUtils.c() + NonceUtils.g();
        var1.put("serialVersionUID", String.valueOf(var5));
        log.info("load template data: " + var1.toString());
        return var1;
    }

    @Override
    public List<String> generateCodeFile(String stylePath) throws Exception {
        log.debug("----Code----Generation----[单表模型:" + this.tableVo.getTableName() + "]------- 生成中。。。");
        String var2 = Config.projectPath;
        Map var3 = this.loadTemplateData();
        String var4 = Config.templatePath;
        if (generate(var4, "/").equals("cg/code-template")) {
            var4 = "/" + generate(var4, "/") + "/one";
            Config.setTemplatePath(var4);
        }

        CgTemplate var5 = new CgTemplate(
                var4);
        var5.setStylePath(stylePath);
        this.generate(var5, var2, var3);
        log.info(" ----- generate  code  success =======> 表名：" + this.tableVo.getTableName() + " ");
        return this.generatedFileList;
    }

    @Override
    public List<String> generateCodeFile(String projectPath, String templatePath, String stylePath) throws Exception {
        if (projectPath != null && !"".equals(projectPath)) {
            Config.setProjectPath(projectPath);
        }

        if (templatePath != null && !"".equals(templatePath)) {
            Config.setTemplatePath(templatePath);
        }

        this.generateCodeFile(stylePath);
        return this.generatedFileList;
    }

    public static void main(String[] args) {
        TableVo var1 = new TableVo();
        var1.setTableName("demo");
        var1.setPrimaryKeyPolicy("uuid");
        var1.setEntityPackage("test");
        var1.setEntityName("CgDemo");
        var1.setFtlDescription("cg 测试demo");

        try {
            (new CodeGenerateOne(var1)).generateCodeFile((String) null);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
