package com.github.cg.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 比较工具类
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class CompareUtils {

    //a
    public static List<String> a = new ArrayList();
    //b
    public static List<String> b = new ArrayList();

    //a
    public CompareUtils() {
    }

    //a
    public static List<File> compare(File var0) throws IOException {
        ArrayList var1 = new ArrayList();
        compare(var0, (List) var1);
        Collections.sort(var1, new Comparator<File>() {
            @Override
            public int compare(File var1, File var2) {
                return var1.getAbsolutePath().compareTo(var2.getAbsolutePath());
            }
        });
        return var1;
    }

    //a
    public static void compare(File var0, List<File> var1) throws IOException {
        log.debug("---------dir------------path: " + var0.getPath() + " -- isHidden --: " + var0.isHidden()
                + " -- isDirectory --: " + var0.isDirectory());
        if (!var0.isHidden() && var0.isDirectory() && !d(var0)) {
            File[] var2 = var0.listFiles();

            for (int var3 = 0; var3 < var2.length; ++var3) {
                compare(var2[var3], var1);
            }
        } else if (!e(var0) && !d(var0)) {
            var1.add(var0);
        }

    }

    //a
    public static String compare(File var0, File var1) {
        if (var0.equals(var1)) {
            return "";
        } else {
            return var0.getParentFile() == null ? var1.getAbsolutePath().substring(var0.getAbsolutePath().length())
                    : var1.getAbsolutePath().substring(var0.getAbsolutePath().length() + 1);
        }
    }

    //b
    public static boolean b(File var0) {
        return var0.isDirectory() ? false : compare(var0.getName());
    }

    //a
    public static boolean compare(String var0) {
        return !StringUtils.isBlank(b(var0));
    }

    //b
    public static String b(String var0) {
        if (var0 == null) {
            return null;
        } else {
            int var1 = var0.indexOf(".");
            return var1 == -1 ? "" : var0.substring(var1 + 1);
        }
    }

    //c
    public static File c(String var0) {
        if (var0 == null) {
            throw new IllegalArgumentException("file must be not null");
        } else {
            File var1 = new File(var0);
            c(var1);
            return var1;
        }
    }

    //c
    public static void c(File var0) {
        if (var0.getParentFile() != null) {
            var0.getParentFile().mkdirs();
        }

    }

    //d
    private static boolean d(File var0) {
        for (int var1 = 0; var1 < a.size(); ++var1) {
            if (var0.getName().equals(a.get(var1))) {
                return true;
            }
        }
        return false;
    }

    //e
    private static boolean e(File var0) {
        for (int var1 = 0; var1 < b.size(); ++var1) {
            if (var0.getName().endsWith((String) b.get(var1))) {
                return true;
            }
        }

        return false;
    }

    static {
        a.add(".svn");
        a.add("CVS");
        a.add(".cvsignore");
        a.add(".copyarea.db");
        a.add("SCCS");
        a.add("vssver.scc");
        a.add(".DS_Store");
        a.add(".git");
        a.add(".gitignore");
        b.add(".ftl");
    }
}
