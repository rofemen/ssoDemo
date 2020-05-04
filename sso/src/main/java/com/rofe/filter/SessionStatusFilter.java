package com.rofe.filter;


import com.rofe.util.GlobalData;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
            String []  allowDomain = {"http://127.0.0.1:8081","http://127.0.0.1:8082"};
            Set<String> allowedOrigins= new HashSet<String>(Arrays.asList(allowDomain));
            String originHeader = req.getHeader("Origin");
            if(allowedOrigins.contains(originHeader)){
                res.addHeader("Access-Control-Allow-Credentials", "true");
                res.addHeader("Access-Control-Allow-Origin", originHeader);
                res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,X-TOKEN");
            }
            HttpSession session = req.getSession();
            String sessionId = session.getId();
            Boolean isLogin = (Boolean)session.getAttribute("isLogin");
            if(isLogin == null){
                isLogin = false;
            }
            String targetUrl = req.getRequestURL().toString();
            String loginUrl = request.getParameter("loginUrl");
            String returnUrl = request.getParameter("returnUrl");
            boolean hitNoFilterUrl = false;
            for(String noFilterUrl : GlobalData.unFilterUrlList){
                if(targetUrl.indexOf(noFilterUrl) != -1){
                    hitNoFilterUrl = true;
                    break;
                }
            }
            //子系统跳转过来的没登录请求就会走这个逻辑
            if(!isLogin && !hitNoFilterUrl){
                GlobalData.hostMap.put(sessionId, returnUrl);
                res.sendRedirect( loginUrl);
                return;
            }
            log.info(GlobalData.hostMap.toString());
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("error!", e);
        }
    }
    @Override
    public void destroy() {
    }
}