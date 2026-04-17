package com.example.syy.utils;

import javax.servlet.http.HttpSession;
import java.util.UUID;

public class CsrfUtil {
    public static final String CSRF_TOKEN = "CSRF_TOKEN";

    // 生成 CSRF Token 并存入 Session
    public static String generateToken(HttpSession session) {
        String token = UUID.randomUUID().toString();
        session.setAttribute(CSRF_TOKEN, token);
        return token;
    }

    // 校验 Token 是否正确
    public static boolean validateToken(String token, HttpSession session) {
        if (token == null || session == null) return false;
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN);
        return token.equals(sessionToken);
    }
}
