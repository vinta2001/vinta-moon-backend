package com.vinta.utils;

public class StringUtil {

    public static final boolean isEmpty(String string){
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }
    public static final boolean hasContent(String string){
        return !isEmpty(string);
    }
}
