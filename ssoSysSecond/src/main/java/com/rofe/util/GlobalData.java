package com.rofe.util;

import java.util.ArrayList;

public class GlobalData {
    public static ArrayList<String> unFilterUrlList = new ArrayList<String>();
    static {
        unFilterUrlList.add("noLogin.html");
        unFilterUrlList.add("login.html");
        unFilterUrlList.add("favicon.ico");
        unFilterUrlList.add("recvSsoToken");
    }

}
