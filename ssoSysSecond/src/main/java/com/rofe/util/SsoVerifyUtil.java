package com.rofe.util;

import fai.comm.util.Param;
import fai.comm.util.Str;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SsoVerifyUtil {
    public static boolean tokenIsVaild(String token, String sessionId, String origin) throws IOException {
        if(Str.isEmpty(token)){
            return false;
        }
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpPost post = new HttpPost("http://127.0.0.1:8080/checkToken");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sessionId", sessionId));
        params.add(new BasicNameValuePair("origin", origin));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
                "UTF-8");
        post.setEntity(entity);
        HttpResponse cliResponse = httpClient.execute(post);
        String cliResult = EntityUtils.toString(cliResponse.getEntity(),"UTF-8");
        if(!Str.isEmpty(cliResult)){
            Param checkTokenResult = Param.parseParam(cliResult);
            boolean result = checkTokenResult.getBoolean("success", false);
            return result;
        }
        return false;
    }
}
