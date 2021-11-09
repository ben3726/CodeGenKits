package com.github.cg.util;

import org.apache.commons.lang.StringUtils;

/**
 * 共通工具类
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
public class CommonUtils {

    public CommonUtils() {
    }

    public static String confirm(String var0) {
        if (!"YES".equals(var0) && !"yes".equals(var0) && !"y".equals(var0) && !"Y".equals(var0) && !"f".equals(var0)) {
            return !"NO".equals(var0) && !"N".equals(var0) && !"no".equals(var0) && !"n".equals(var0) && !"t".equals(var0)
                    ? null : "N";
        } else {
            return "Y";
        }
    }

    public static String getValue(String var0) {
        return StringUtils.isBlank(var0) ? "" : var0;
    }

    public static String toString(String var0) {
        return "'" + var0 + "'";
    }
}
