package com.gabriel.hentaichat.util;

/**
 * Created by gabriel on 2017/3/10.
 */

public class StringUtil {
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
