package com.rofe.controller;

import com.rofe.util.GlobalData;
import com.rofe.util.RedisUtil;
import com.rofe.util.WebUtil;
import fai.comm.util.Param;
import fai.comm.util.Str;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
public class SsoAuthController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/sso-center-login")
    public void login(HttpServletRequest request,HttpServletResponse response, @RequestParam(value = "returnUrl")String returnUrl) throws Exception{
        String requestHost = request.getRemoteHost();
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if(isLogin == null){
            isLogin = false;
        }
        String sessionId = session.getId();

        if(isLogin){
            if(returnUrl.indexOf("?") == -1){
                returnUrl += "?token=" + session.getAttribute("token");
            }else{
                returnUrl += "&token=" + session.getAttribute("token");
            }
            response.sendRedirect(returnUrl);
        }
    }

    @RequestMapping("/sso-center-loginAction")
    public String loginAction(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Param rtParam = new Param();
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String token = WebUtil.getToken(sessionId);
        redisUtil.set(WebUtil.VERTY_PREFIX + token, true, 60 * 3);
        session.setAttribute("token", token);
        session.setAttribute("isLogin", true);
        String returnUrl = GlobalData.hostMap.get(sessionId);
        if(returnUrl == null || "".equals(returnUrl)){
            returnUrl = "/";
        }
        rtParam.setBoolean("success", true).setString("returnUrl", returnUrl).setString("token", token);
        return rtParam.toJson();
    }

    @RequestMapping("/checkToken")
    public String checkToken(@RequestParam("token") String token, @RequestParam("sessionId")String sessionId, @RequestParam("origin") String origin) throws Exception{
        Param rtResult = new Param();
        if(Str.isEmpty(token) || Str.isEmpty(sessionId) || Str.isEmpty(origin)){
            rtResult.setBoolean("success", false).setString("msg", "非法参数");
            return rtResult.toJson();
        }
        Boolean hasToken = (Boolean)redisUtil.get(WebUtil.VERTY_PREFIX + token);
        if(hasToken == null || !hasToken){
            rtResult.setBoolean("success", false).setString("msg", "未登录");
            return rtResult.toJson();
        }
        Map<String, String> registMap = (Map)redisUtil.get(token);
        if(registMap == null){
            registMap = new HashMap<String, String>();
        }
        registMap.put(origin, sessionId);
        redisUtil.set(token, registMap, 60 * 3);
        rtResult.setBoolean("success", true).setString("msg", "校验成功");
        return rtResult.toJson();
    }

    @RequestMapping("/test")
    public String test() throws Exception{
        redisUtil.set("rofeya","rofeya");
        return "";
    }


}
