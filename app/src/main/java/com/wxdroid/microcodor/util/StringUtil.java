package com.wxdroid.microcodor.util;

/**
 * Created by like on 16/11/16.
 */

public class StringUtil {
    /**
     * 过滤特殊字符
     * @param str
     * @return
     */
    public static String fiterSpecialCharacter(String str){
        if(str != null){
            return str.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
        }

        return null;
    }

    /**
     * 过滤所有空格，制表符等
     * @param str
     * @return
     */
    public static String fiterEmptyCharacter(String str){
        if(str != null){
            return str.replaceAll("[\\s*|\t|\r|\n]", "");
        }

        return null;
    }


    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
