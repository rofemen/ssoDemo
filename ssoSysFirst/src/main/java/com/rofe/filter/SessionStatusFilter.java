package com.rofe.filter;

import com.rofe.util.GlobalData;
import com.rofe.util.MyNotLoginException;
import com.rofe.util.SsoVerifyUtil;
import fai.comm.util.Encoder;
import fai.comm.util.Str;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@Slf4j
@WebFilter(filterName = "sessionStatusFilter", urlPatterns = "/*")
public class SessionStatusFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(filterConfig.getFilterName() + " init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        log.info("sessionStatusFilter");
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession session = req.getSession();
            String originHost = req.getHeader("Host");
            String targetUrl = req.getRequestURL().toString();
            Boolean isLogin = (Boolean)session.getAttribute("isLogin");
            if(isLogin == null){
                isLogin = false;
            }
            boolean hitNoFilterUrl = false;
            for(String noFilterUrl : GlobalData.unFilterUrlList){
                if(targetUrl.indexOf(noFilterUrl) != -1){
                    hitNoFilterUrl = true;
                    break;
                }
            }
            String token = req.getParameter("token");
            boolean tokenVaild = false;
            if(!isLogin && !Str.isEmpty(token) && SsoVerifyUtil.tokenIsVaild(token, session.getId(), originHost)){
                tokenVaild = true;
                session.setAttribute("isLogin", true);
            }
            if(!isLogin && !hitNoFilterUrl && !tokenVaild){
                String ssoCenterUrl = "http://127.0.0.1:8080/sso-center-login?loginUrl=" + Encoder.encodeUrl("http://" + originHost + "/login.html") +
                        "&returnUrl=" + Encoder.encodeUrl(targetUrl);
                res.sendRedirect(ssoCenterUrl);
                return;
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("error!", e);
        }
    }
    @Override
    public void destroy() {
        log.info("SessionStatusFilter destroy");
    }
}