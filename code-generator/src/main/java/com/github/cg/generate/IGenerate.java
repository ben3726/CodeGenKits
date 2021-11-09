package com.github.cg.generate;

import java.util.List;
import java.util.Map;

/**
 * 代码生成接口
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
public interface IGenerate {

    Map<String, Object> loadTemplateData() throws Exception;

    List<String> generateCodeFile(String var1) throws Exception;

    List<String> generateCodeFile(String var1, String var2, String var3) throws Exception;
}
