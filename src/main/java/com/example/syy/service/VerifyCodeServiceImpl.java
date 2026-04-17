package com.example.syy.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Override
    public boolean validateCode(HttpServletRequest request, String inputCode) {
        // 1. 获取Session中的验证码
        String sessionCode = (String) request.getSession().getAttribute("verifyCode");
        // 2. 检查验证码是否为空或过期
        if (sessionCode == null) {
            return false;
        }
        // 3. 比对验证码（忽略大小写）
        boolean valid = sessionCode.equalsIgnoreCase(inputCode);
        // 4. 验证后移除验证码，防止重复使用
        request.getSession().removeAttribute("verifyCode");
        return valid;
    }
}
