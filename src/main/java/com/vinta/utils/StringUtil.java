package com.vinta.utils;

public class StringUtil {

    public static boolean isEmpty(String string){
        return string == null || string.isEmpty();
    }
    public static boolean hasContent(String string){
        return !isEmpty(string);
    }
}
