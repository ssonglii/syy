package com.example.syy.utils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    public static void addSecureCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

        // 手动设置 SameSite 属性
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Secure; SameSite=Strict", name, value, "/");
        response.addHeader("Set-Cookie", cookieHeader);
    }
}