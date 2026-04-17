package com.example.syy.service;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
    // 生成加密密钥并存储到会话
    String generateEncryptionKey(HttpServletRequest request);

    // 根据哈希值验证密钥并返回原始密钥
    String getAndValidateKey(String keyHash, HttpServletRequest request);

    // 销毁会话中的密钥（注册成功后调用）
    void destroyEncryptionKey(HttpServletRequest request);
}
