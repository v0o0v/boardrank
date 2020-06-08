package net.boardrank.boardgame.service;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtilService {

    public static BufferedImage resizeByWidth(BufferedImage img, int width) {
        if(img.getWidth() <= width) return img;

        double ratio = (double) width / img.getWidth();
        double height = ratio * img.getHeight();
        return resize(img, (int) height, width);
    }

    public static BufferedImage resizeByHeight(BufferedImage img, int height) {
        if(img.getHeight() <= height) return img;

        double ratio = (double) height / img.getHeight();
        double width = ratio * img.getWidth();
        return resize(img, height, (int) width);
    }

    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_FAST);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

}
