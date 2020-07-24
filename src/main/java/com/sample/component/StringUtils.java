package com.sample.component;

public class StringUtils {
    public static String getOrDefault(String value, String defaultVal) {
        return value != null ? value : defaultVal;
    }
}
