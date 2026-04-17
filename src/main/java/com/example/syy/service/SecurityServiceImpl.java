package com.example.syy.service;

import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final String SESSION_KEY = "encryptionKey";
    @Override
    public String generateEncryptionKey(HttpServletRequest request) {
        // 生成32字节(256位)的随机密钥
        String key = UUID.randomUUID().toString().replace("-", "");
        // 存储到当前会话
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_KEY, key);

        return key;
    }
    @Override
    public String getAndValidateKey(String keyHash, HttpServletRequest request) {
        // 从会话获取存储的密钥
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        String storedKey = (String) session.getAttribute(SESSION_KEY);
        if (storedKey == null) return null;
        // 计算存储密钥的哈希值并比较
        String calculatedHash = calculateSHA256(storedKey);
        if (calculatedHash.equals(keyHash)) {
            return storedKey;
        }
        return null;
    }
    // 销毁会话中的密钥
    @Override
    public void destroyEncryptionKey(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(SESSION_KEY);
        }
    }
    // 使用SHA-256计算哈希值
    private String calculateSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }
}