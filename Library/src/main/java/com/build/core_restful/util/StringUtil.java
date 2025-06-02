package com.build.core_restful.util;

public class StringUtil {
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String) {
            return ((String) obj).isEmpty();
        }
        return obj.toString().isEmpty();
    }
}
