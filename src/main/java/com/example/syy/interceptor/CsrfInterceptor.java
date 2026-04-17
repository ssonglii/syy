package com.example.syy.interceptor;

import com.example.syy.utils.CsrfUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CsrfInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        // 只拦截 POST、DELETE、PUT、PATCH 等修改类请求
        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("DELETE")) {
            // 从请求头或参数中取 CSRF Token
            String csrfToken = request.getHeader("X-CSRF-TOKEN");
            if (csrfToken == null) {
                csrfToken = request.getParameter("csrfToken");
            }

            // 校验失败
            if (!CsrfUtil.validateToken(csrfToken, request.getSession())) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"success\":false,\"message\":\"CSRF 校验失败，请求非法\"}");
                return false;
            }
        }
        return true;
    }
}