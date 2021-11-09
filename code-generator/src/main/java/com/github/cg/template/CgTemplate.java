package com.github.cg.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板类
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class CgTemplate {

    //b
    private String templatePath;
    //c
    private List<File> templateRootDirs = new ArrayList();
    //d
    private String stylePath;

    public CgTemplate(String path) {
        log.debug("----templatePath-----------------" + path);
        log.debug("----stylePath-----------------" + this.stylePath);
        this.templatePath = path;
    }

    //a
    private void add(File var1) {
        this.templateRootDirs = Arrays.asList(var1);
    }

    //a
    private void add(File... var1) {
        this.templateRootDirs = Arrays.asList(var1);
    }

    //a
    public String getStylePath() {
        return this.stylePath;
    }

    //a
    public void setStylePath(String path) {
        this.stylePath = path;
    }

    //b
    public List<File> getDirs() {
        String var1 = this.getClass().getResource(this.templatePath).getFile();
        var1 = var1.replaceAll("%20", " ");
        log.debug("-------classpath-------" + var1);
        if (var1.indexOf("/BOOT-INF/classes!") != -1) {
            var1 = System.getProperty("user.dir") + File.separator + "config/cg/code-template/"
                    .replace("/", File.separator);
            log.debug("---JAR--config--classpath-------" + var1);
        }

        this.add(new File(var1));
        return this.templateRootDirs;
    }

    //a
    public void add(List<File> var1) {
        this.templateRootDirs = var1;
    }

    @Override
    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("{\"templateRootDirs\":\"");
        var1.append(this.templateRootDirs);
        var1.append("\",\"stylePath\":\"");
        var1.append(this.stylePath);
        var1.append("\"} ");
        return var1.toString();
    }
}
