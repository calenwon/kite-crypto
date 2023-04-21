package com.kite.utils;

/**
 * String Tools
 *
 * @author Calendar
 */
public class StringUtil {

    public static final String EMPTY = "";

    public static boolean isEmpty(String s) {
        return s == null || EMPTY.equals(s);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if(s1 == null || s2 == null) {
            return false;
        }

        return s1.equalsIgnoreCase(s2);
    }
}
