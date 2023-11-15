package com.czandlh.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ImageVerificationCode {

    private int weight = 100;
    private int height = 40;

    private String text;

    private Random r = new Random();

    private String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

    private String[] fontNames = {"Georgia"};

    /**
     * 获取随机的颜色
     *
     * @return
     */
    private Color randomColor() {
        int r = this.r.nextInt(255);
        int g = this.r.nextInt(225);
        int b = this.r.nextInt(225);
        return new Color(r, g, b);
    }

    /**
     * 获取随机字体
     *
     * @return
     */
    private Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);
        int size = r.nextInt(10) + 24;
        return new Font(fontName, style, size);
    }

    /**
     * 获取随机字符
     *
     * @return
     */
    private char randomChar() {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }

    /**
     * 画干扰线
     *
     * @param image
     */
    private void drawLine(BufferedImage image) {
        int num = r.nextInt(10);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(weight);
            int y1 = r.nextInt(height);
            int x2 = r.nextInt(weight);
            int y2 = r.nextInt(height);
            graphics2D.setColor(randomColor());
            graphics2D.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 创建图片
     *
     * @return
     */
    private BufferedImage createImage() {
        BufferedImage bufferedImage = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setColor(new Color(255, 255, r.nextInt(245) + 10));
        graphics2D.fillRect(0, 0, weight, height);
        return bufferedImage;
    }

    /**
     * 验证码图片的方法
     *
     * @return
     */
    public BufferedImage getImage() {
        BufferedImage image = createImage();
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            stringBuilder.append(s);
            float x = i * 1.0F * weight / 4;
            graphics2D.setFont(randomFont());
            graphics2D.setColor(randomColor());
            graphics2D.drawString(s, x, height - 5);
        }
        this.text = stringBuilder.toString();
        drawLine(image);
        return image;
    }

    public String getText() {
        return text;
    }

    public static void output(BufferedImage image, OutputStream out) throws IOException {
        ImageIO.write(image, "jpeg", out);
    }
}
