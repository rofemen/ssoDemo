package com.rofe.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalData {
    public static ConcurrentHashMap<String, String> hostMap = new ConcurrentHashMap<String, String>();

    public static ArrayList<String> unFilterUrlList = new ArrayList<String>();
    static {
        unFilterUrlList.add("sso-center-loginAction");
        unFilterUrlList.add("sso-center-logoutAction");
        unFilterUrlList.add("checkToken");
    }
}
