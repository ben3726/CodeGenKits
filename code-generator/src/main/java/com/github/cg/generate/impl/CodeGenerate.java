package com.github.cg.generate.impl;

import com.github.cg.config.Config;
import com.github.cg.util.CompareUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.github.cg.template.CgTemplate;
import com.github.cg.util.TemplateUtils;

/**
 * 代码生成
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class CodeGenerate {

    //c
    protected static String encoding = "UTF-8";
    //d
    protected List<String> generatedFileList = new ArrayList();

    //a
    protected void generate(CgTemplate var1, String var2, Map<String, Object> var3)
            throws Exception {
        log.debug("--------generate----projectPath--------" + var2);

        for (int var4 = 0; var4 < var1.getDirs().size(); ++var4) {
            File var5 = (File) var1.getDirs().get(var4);
            this.generate(var2, var5, var3, var1);
        }

    }

    //a
    protected void generate(String var1, File var2, Map<String, Object> var3,
            CgTemplate var4)
            throws Exception {
        if (var2 == null) {
            throw new IllegalStateException("'templateRootDir' must be not null");
        } else {
            log.info(
                    "  load template from templateRootDir = '" + var2.getAbsolutePath() + "',stylePath ='" + var4.getStylePath()
                            + "',  out GenerateRootDir:" + Config.projectPath);
            List var5 = CompareUtils.compare(var2);
            log.debug("----srcFiles----size-----------" + var5.size());
            log.debug("----srcFiles----list------------" + var5.toString());

            for (int var6 = 0; var6 < var5.size(); ++var6) {
                File var7 = (File) var5.get(var6);
                this.generate(var1, var2, var3, var7, var4);
            }

        }
    }

    //a
    protected void generate(String var1, File var2, Map<String, Object> var3, File var4,
            CgTemplate var5) throws Exception {
        log.debug("-------templateRootDir--" + var2.getPath());
        log.debug("-------srcFile--" + var4.getPath());
        String var6 = CompareUtils.compare(var2, var4);

        try {
            log.debug("-------templateFile--" + var6);
            String var7 = generate(var3, var6, var5);
            log.debug("-------outputFilepath--" + var7);
            String var8;
            if (var7.startsWith("java")) {
                var8 = var1 + File.separator + Config.sourceRootPackage.replace(".", File.separator);
                var7 = var7.substring("java".length());
                var7 = var8 + var7;
                log.debug("-------java----outputFilepath--" + var7);
                this.generate(var6, var7, var3, var5);
            } else if (var7.startsWith("webapp")) {
                var8 = var1 + File.separator + Config.webRootPackage.replace(".", File.separator);
                var7 = var7.substring("webapp".length());
                var7 = var8 + var7;
                log.debug("-------webapp---outputFilepath---" + var7);
                this.generate(var6, var7, var3, var5);
            } else if (var7.startsWith("resources")) {
                var8 = var1 + File.separator + Config.resourcesRootPackage.replace(".", File.separator);
                var7 = var7.substring("resources".length());
                var7 = var8 + var7;
                log.debug("-------resources---outputFilepath---" + var7);
                this.generate(var6, var7, var3, var5);
            }
        } catch (Exception var10) {
            log.error(var10.toString(), var10);
        }

    }

    //a
    protected void generate(String var1, String var2, Map<String, Object> var3, CgTemplate var4) throws Exception {
        if (var2.endsWith("i")) {
            var2 = var2.substring(0, var2.length() - 1);
        }

        Template var5 = this.generate(var1, var4);
        var5.setOutputEncoding(encoding);
        File var6 = CompareUtils.c(var2);
        log.info("[generate]\t template:" + var1 + " ==> " + var2);
        TemplateUtils.a(var5, var3, var6, encoding);
        if (!this.generate(var6)) {
            this.generatedFileList.add("生成成功：" + var2);
        }

        if (this.generate(var6)) {
            this.generate(var6, "#segment#");
        }

    }

    //a
    protected Template generate(String var1, CgTemplate var2) throws IOException {
        return TemplateUtils.a(var2.getDirs(), encoding, var1).getTemplate(var1);
    }

    //a
    protected boolean generate(File var1) {
        return var1.getName().startsWith("[1-n]");
    }

    //a
    protected void generate(File var1, String var2) {
        InputStreamReader var3 = null;
        BufferedReader var4 = null;
        ArrayList var5 = new ArrayList();
        boolean var20 = false;

        int var28;
        label341:
        {
            label342:
            {
                try {
                    var20 = true;
                    var3 = new InputStreamReader(new FileInputStream(var1), "UTF-8");
                    var4 = new BufferedReader(var3);
                    boolean var7 = false;
                    OutputStreamWriter var8 = null;

                    while (true) {
                        String var6;
                        while ((var6 = var4.readLine()) != null) {
                            if (var6.trim().length() > 0 && var6.startsWith(var2)) {
                                String var9 = var6.substring(var2.length());
                                String var10 = var1.getParentFile().getAbsolutePath();
                                var9 = var10 + File.separator + var9;
                                log.info("[generate]\t split file:" + var1.getAbsolutePath() + " ==> " + var9);
                                var8 = new OutputStreamWriter(new FileOutputStream(var9), "UTF-8");
                                var5.add(var8);
                                this.generatedFileList.add("生成成功：" + var9);
                                var7 = true;
                            } else if (var7) {
                                var8.append(var6 + "\r\n");
                            }
                        }

                        for (int var29 = 0; var29 < var5.size(); ++var29) {
                            ((Writer) var5.get(var29)).close();
                        }

                        var4.close();
                        var3.close();
                        log.debug("[generate]\t delete file:" + var1.getAbsolutePath());
                        b(var1);
                        var20 = false;
                        break label341;
                    }
                } catch (FileNotFoundException var25) {
                    var25.printStackTrace();
                    var20 = false;
                    break label342;
                } catch (IOException var26) {
                    var26.printStackTrace();
                    var20 = false;
                } finally {
                    if (var20) {
                        try {
                            if (var4 != null) {
                                var4.close();
                            }

                            if (var3 != null) {
                                var3.close();
                            }

                            if (var5.size() > 0) {
                                for (int var12 = 0; var12 < var5.size(); ++var12) {
                                    if (var5.get(var12) != null) {
                                        ((Writer) var5.get(var12)).close();
                                    }
                                }
                            }
                        } catch (IOException var21) {
                            var21.printStackTrace();
                        }
                    }
                }

                try {
                    if (var4 != null) {
                        var4.close();
                    }

                    if (var3 != null) {
                        var3.close();
                    }

                    if (var5.size() > 0) {
                        for (var28 = 0; var28 < var5.size(); ++var28) {
                            if (var5.get(var28) != null) {
                                ((Writer) var5.get(var28)).close();
                            }
                        }
                    }
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
                return;
            }

            try {
                if (var4 != null) {
                    var4.close();
                }

                if (var3 != null) {
                    var3.close();
                }

                if (var5.size() > 0) {
                    for (var28 = 0; var28 < var5.size(); ++var28) {
                        if (var5.get(var28) != null) {
                            ((Writer) var5.get(var28)).close();
                        }
                    }
                }
            } catch (IOException var23) {
                var23.printStackTrace();
            }
            return;
        }

        try {
            if (var4 != null) {
                var4.close();
            }
            if (var3 != null) {
                var3.close();
            }
            if (var5.size() > 0) {
                for (var28 = 0; var28 < var5.size(); ++var28) {
                    if (var5.get(var28) != null) {
                        ((Writer) var5.get(var28)).close();
                    }
                }
            }
        } catch (IOException var24) {
            var24.printStackTrace();
        }
    }

    //a
    protected static String generate(Map<String, Object> var0, String var1,
            CgTemplate var2)
            throws Exception {
        String var3 = var1;
        boolean var4 = true;
        int var9;
        if ((var9 = var1.indexOf(64)) != -1) {
            var3 = var1.substring(0, var9);
            String var5 = var1.substring(var9 + 1);
            Object var6 = var0.get(var5);
            if (var6 == null) {
                log.error("[not-generate] WARN: test expression is null by key:[" + var5 + "] on template:[" + var1 + "]");
                return null;
            }

            if (!"true".equals(String.valueOf(var6))) {
                log.error("[not-generate]\t test expression '@" + var5 + "' is false,template:" + var1);
                return null;
            }
        }

        Configuration var10 = TemplateUtils.a(var2.getDirs(), encoding, "/");
        var3 = TemplateUtils.a(var3, var0, var10);
        String var11 = var2.getStylePath();
        if (var11 != null && var11 != "") {
            var3 = var3.substring(var11.length() + 1);
        }

        String var7 = var3.substring(var3.lastIndexOf("."));
        String var8 = var3.substring(0, var3.lastIndexOf(".")).replace(".", File.separator);
        var3 = var8 + var7;
        return var3;
    }

    //b
    protected static boolean b(File var0) {
        boolean var1 = false;
        for (int var2 = 0; !var1 && var2++ < 10; var1 = var0.delete()) {
            System.gc();
        }
        return var1;
    }

    //a
    protected static String generate(String var0, String var1) {
        boolean var2 = true;
        boolean var3 = true;
        do {
            int var4 = var0.indexOf(var1) == 0 ? 1 : 0;
            int var5 = var0.lastIndexOf(var1) + 1 == var0.length() ? var0.lastIndexOf(var1) : var0.length();
            var0 = var0.substring(var4, var5);
            var2 = var0.indexOf(var1) == 0;
            var3 = var0.lastIndexOf(var1) + 1 == var0.length();
        } while (var2 || var3);
        return var0;
    }
}
