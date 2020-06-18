package com.nt.utils;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class sign {

    //图片宽度
    private static final int WIDTH = 340;
    //图片高度
    private static final int HEIGHT = 340;


    public static String startGraphics2D(String name) throws IOException {
        // 定义图像buffer：高度，宽度，半透明
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        //绘图
        Graphics2D g = buffImg.createGraphics();
        g.setColor(Color.RED);
        //线条的宽度
        g.setStroke(new BasicStroke(5));
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //绘制圆
        //周半径
        int radius = HEIGHT/3;
        //画图所出位置
        int CENTERX = WIDTH/2;
        //画图所处位置
        int CENTERY = HEIGHT/2;

        //画圆
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
        g.draw(circle);

        if(name.length() == 2){
            g.setFont(new Font("宋体", Font.BOLD, 60));
            g.drawString(name, CENTERX-60, CENTERY+25);
        }else if(name.length() == 3){
            g.setFont(new Font("宋体", Font.BOLD, 50));
            g.drawString(name, CENTERX-75, CENTERY+20);
        }else if(name.length() == 4){
            g.setFont(new Font("宋体", Font.BOLD, 40));
            g.drawString(name, CENTERX-75, CENTERY+20);
        }else if(name.length() == 5){
            g.setFont(new Font("宋体", Font.BOLD, 40));
            g.drawString(name, CENTERX-75, CENTERY+20);
        }

        String filename = new Date().getTime()+ name + ".png";
        String filePath = AuthConstants.FILE_DIRECTORY + "image\\"+ filename;
        ImageIO.write(buffImg, "png", new File(filePath));

        return filename;
    }
}
