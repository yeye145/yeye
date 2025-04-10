package service.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {

    // 生成验证码文本和图片
    public static CaptchaResult generateCaptcha(int width, int height) {
        String code = generateRandomCode(4); // 生成4位随机验证码
        BufferedImage image = createImage(width, height, code);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "JPEG", baos);
            return new CaptchaResult(code, baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("生成验证码失败", e);
        }
    }

    // 验证用户输入的验证码
    public static boolean validate(String inputCode, String realCode) {
        return inputCode != null && inputCode.equalsIgnoreCase(realCode);
    }

    // 生成随机验证码文本
    private static String generateRandomCode(int length) {
        String chars = "ABCDEFGHJKMNPQRSTUVWXY3456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // 创建验证码图片
    private static BufferedImage createImage(int width, int height, String code) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 绘制背景
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            g.setColor(getRandomColor(120, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 绘制验证码文本
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(20 + random.nextInt(110),
                    20 + random.nextInt(110),
                    20 + random.nextInt(110)));
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 30);
        }

        g.dispose();
        return image;
    }

    // 生成随机颜色
    private static Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    // 封装验证码生成结果
    public static class CaptchaResult {
        private final String code;
        private final byte[] imageBytes;

        public CaptchaResult(String code, byte[] imageBytes) {
            this.code = code;
            this.imageBytes = imageBytes;
        }

        public String getCode() { return code; }
        public byte[] getImageBytes() { return imageBytes; }
    }
}