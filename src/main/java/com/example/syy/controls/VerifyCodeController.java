package com.example.syy.controls;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import com.example.syy.utils.VerifyCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerifyCodeController {
        // 验证码字符集
        private static final String CODE_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final int WIDTH = 120;
        private static final int HEIGHT = 40;

        @GetMapping("/generateVerifyCode")
        public void generateVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
            // 1. 生成随机验证码
            String verifyCode = generateCode(4);
            // 2. 将验证码存入Session
            request.getSession().setAttribute("verifyCode", verifyCode);
            // 3. 生成验证码图片
            BufferedImage image = createImage(verifyCode);
            // 4. 设置响应头
            response.setContentType("image/png");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            // 5. 输出图片
            ImageIO.write(image, "PNG", response.getOutputStream());
        }
        // 生成随机验证码
        private String generateCode(int length) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(CODE_CHARS.length());
                sb.append(CODE_CHARS.charAt(index));
            }
            return sb.toString();
        }
        // 创建验证码图片
        private BufferedImage createImage(String code) {
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();

            // 设置背景色
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // 设置边框
            g.setColor(Color.GRAY);
            g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

            // 添加干扰线
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int x1 = random.nextInt(WIDTH);
                int y1 = random.nextInt(HEIGHT);
                int x2 = random.nextInt(WIDTH);
                int y2 = random.nextInt(HEIGHT);
                g.setColor(getRandomColor());
                g.drawLine(x1, y1, x2, y2);
            }

            // 添加噪点
            for (int i = 0; i < 100; i++) {
                int x = random.nextInt(WIDTH);
                int y = random.nextInt(HEIGHT);
                g.setColor(getRandomColor());
                g.drawRect(x, y, 1, 1);
            }

            // 添加验证码文本
            g.setFont(new Font("Arial", Font.BOLD, 24));
            for (int i = 0; i < code.length(); i++) {
                g.setColor(getRandomColor());
                g.drawString(code.charAt(i) + "", 25 * i + 15, 28);
            }

            g.dispose();
            return image;
        }
        // 获取随机颜色
        private Color getRandomColor() {
            Random random = new Random();
            int r = random.nextInt(200);
            int g = random.nextInt(200);
            int b = random.nextInt(200);
            return new Color(r, g, b);
        }
    }