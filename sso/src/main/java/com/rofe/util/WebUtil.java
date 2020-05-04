package com.rofe.util;

import fai.comm.util.Str;

public class WebUtil {
    public static String TOKEN_PREFIX = "token_";
    public static String VERTY_PREFIX = "verity_";

    public static String getToken(String sid){
        if(Str.isEmpty(sid)){
            return "";
        }
        return TOKEN_PREFIX + sid;
    }
}
