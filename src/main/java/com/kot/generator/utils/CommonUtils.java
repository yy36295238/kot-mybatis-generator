package com.kot.generator.utils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author YangYu
 */
public class CommonUtils {

    /**
     * 下划线转驼峰
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     */
    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * 单词首字母大写 UserName
     */
    public static String capitalName(String word) {
        return captureName(camelCaseName(word));
    }

    /**
     * 首字母小写
     */
    public static String lowerName(String name) {
        char[] cs = name.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }

    public static Class<?> changeType(String type) {

        if ("DATE".equals(type) || "DATETIME".equals(type) || "TIMESTAMP".equals(type)) {
            return Date.class;
        }
        if ("TINYINT".equals(type) || "SMALLINT".equals(type) || "INT".equals(type)) {
            return Integer.class;
        }
        if ("BIGINT".equals(type) || "BIGINT UNSIGNED".equals(type)) {
            return Long.class;
        }
        if ("FLOAT".equals(type)) {
            return Float.class;
        }
        if ("DOUBLE".equals(type)) {
            return Double.class;
        }
        if ("DECIMAL".equals(type) || "NUMERIC".equals(type)) {
            return BigDecimal.class;
        }
        if ("BIT".equals(type) || "BOOLEAN".equals(type)) {
            return Boolean.class;
        }
        if ("CHAR".equals(type) || "VARCHAR".equals(type) || "TEXT".equals(type)) {
            return String.class;
        }
        throw new RuntimeException("未知数据类型 = " + type);
    }
}
