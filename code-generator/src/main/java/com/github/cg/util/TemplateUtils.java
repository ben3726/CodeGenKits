package com.github.cg.util;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板工具类
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class TemplateUtils {

    //a
    public static Configuration a(List<File> fileList, String encoding, String templateRootDirs) throws IOException {
        Configuration config = new Configuration();
        log.debug(" FileTemplateLoader[] size " + fileList.size());
        log.debug(" templateRootDirs templateName " + templateRootDirs);
        FileTemplateLoader[] loaders = new FileTemplateLoader[fileList.size()];

        for (int i = 0; i < fileList.size(); ++i) {
            File var6 = (File) fileList.get(i);
            //log.debug(" FileTemplateLoader " + var6.getAbsolutePath());
            loaders[i] = new FileTemplateLoader(var6);
        }

        MultiTemplateLoader templateLoader = new MultiTemplateLoader(loaders);
        config.setTemplateLoader(templateLoader);
        config.setNumberFormat("###############");
        config.setBooleanFormat("true,false");
        config.setDefaultEncoding(encoding);
        return config;
    }

    //a
    public static List<String> a(String str, String var1) {
        String[] var2 = strToArray(str, "\\/");
        ArrayList var3 = new ArrayList();
        var3.add(var1);
        var3.add(File.separator + var1);
        String temp = "";
        for (int i = 0; i < var2.length; ++i) {
            temp = temp + File.separator + var2[i];
            var3.add(temp + File.separator + var1);
        }
        return var3;
    }

    //b
    public static String[] strToArray(String str, String delim) {
        if (str == null) {
            return new String[0];
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(str, delim);
            ArrayList list = new ArrayList();
            while (stringTokenizer.hasMoreElements()) {
                Object obj = stringTokenizer.nextElement();
                list.add(obj.toString());
            }
            return (String[]) list.toArray(new String[list.size()]);
        }
    }

    //a
    public static String a(String str, Map<String, Object> map, Configuration config) {
        StringWriter stringWriter = new StringWriter();
        try {
            Template template = new Template("templateString...", new StringReader(str), config);
            template.process(map, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new IllegalStateException("cannot process templateString:" + str + " cause:" + e, e);
        }
    }

    //a
    public static void a(Template template, Map<String, Object> map, File file, String charsetName)
            throws IOException, TemplateException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
        map.put("Format", new SimpleFormat());
        template.process(map, bufferedWriter);
        bufferedWriter.close();
    }
}
