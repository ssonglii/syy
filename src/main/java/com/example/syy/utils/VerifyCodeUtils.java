package com.example.syy.utils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeUtils {
    // 验证码长度
    private static final int VERIFY_CODE_LENGTH = 4;
    // 验证码字符集
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < VERIFY_CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            code.append(CHAR_SET.charAt(index));
        }
        return code.toString();
    }

    public static BufferedImage generateImage(String code) {
        int width = 120, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 边框
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width - 1, height - 1);

        // 随机干扰线
        Random random = new Random();
        g.setColor(Color.BLUE);
        for (int i = 0; i < 4; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 验证码字符
        g.setColor(new Color(20, 140, 220));
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < code.length(); i++) {
            g.drawString(code.charAt(i) + "", 25 * i + 5, 28);
        }

        g.dispose();
        return image;
    }
}