package com.zhu.rimxia.biz.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class VerifyCode {
    private final  static  int w = 70;
    private final  static int h = 35;
    private final  static Random r = new Random();
    // {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"}
    private final  static String[] fontNames = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
    private final  static String codes = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    private final  static Color bgColor = new Color(255, 255, 255);

    private static  Color randomColor() {
        int red = r.nextInt(150);
        int green = r.nextInt(150);
        int blue = r.nextInt(150);
        return new Color(red, green, blue);
    }

    private  static Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);
        int size = r.nextInt(5) + 24;
        return new Font(fontName, style, size);//指定字体名称、样式和点大小，创建一个新 Font。
    }

    //画干扰的线条
    private  static void drawLine(BufferedImage image) {
        int num = 3;//画三条
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(w);
            int y1 = r.nextInt(h);
            int x2 = r.nextInt(w);
            int y2 = r.nextInt(h);
            g2.setStroke(new BasicStroke(1.5F));
            g2.setColor(Color.BLUE);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    private  static char randomChar() {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }

    private  static BufferedImage createImage() {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(VerifyCode.bgColor);
        g2.fillRect(0, 0, w, h);
        return image;
    }

    public static  String getText(){
        return (new StringBuilder()
                .append(randomChar())
                .append(randomChar())
                .append(randomChar())
                .append(randomChar())).toString();
    }
    public static BufferedImage getImage(String text) {
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        // 向图片中画4个字符
        for (int i = 0; i < 4; i++) {
            float x = i * 1.0F * w / 4;
            g2.setFont(randomFont());
            g2.setColor(randomColor());
            /**
             * 首字符的基线位于用户空间的 (x, h-5) 位置处
             * 原点在左上角,X轴递增的方向是从左向右;Y轴是从上到下
             * 在提供的坐标位于基线上最左边字符的情况下，可以从右到左呈现字形
             * h-5表示y轴方向,向上偏移了5
             */
            g2.drawString(text.charAt(i)+" ", x, h - 5);
        }
        drawLine(image);
        return image;
    }

    public static void output(BufferedImage image, OutputStream out)
            throws IOException {
        ImageIO.write(image, "JPEG", out);
    }


}