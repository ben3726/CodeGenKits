package com.github.cg;

import com.github.cg.generate.impl.CodeGenerateOne;
import com.github.cg.pojo.TableVo;

/**
 * 本地入口
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
public class CgOneUtil {

    public CgOneUtil() {
    }

    public static void main(String[] args) {
        TableVo var1 = new TableVo();
        var1.setEntityName("demo");
        var1.setEntityPackage("test");
        var1.setFtlDescription("测试表");
        var1.setTableName("demo");

        try {
            String var2 = "E:\\111";
            String var3 = "/cg/code-template/";
            (new CodeGenerateOne(var1)).generateCodeFile(var2, var3, "one");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
