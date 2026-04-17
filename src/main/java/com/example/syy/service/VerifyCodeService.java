package com.example.syy.service;

import javax.servlet.http.HttpServletRequest;

public interface VerifyCodeService {
    // 验证验证码是否正确
    boolean validateCode(HttpServletRequest request, String inputCode);
}