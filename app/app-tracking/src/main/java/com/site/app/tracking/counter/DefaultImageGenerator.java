package com.site.app.tracking.counter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class DefaultImageGenerator implements ImageGenerator {
   private int width = 60;

   private int height = 20;

   public void generate(OutputStream out, int value) throws IOException {
      // 在内存中创建图象
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      // 获取图形上下文
      Graphics g = image.getGraphics();

      // 设定背景色
      g.setColor(new Color(0xDCDCDC));
      g.fillRect(0, 0, width, height);

      // 画边框
      g.setColor(Color.black);
      g.drawRect(0, 0, width - 1, height - 1);

      // 将认证码显示到图象中
      g.setColor(Color.black);

      String str = "0000" + value;

      g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
      g.drawString(str.substring(str.length() - 4), 8, 17);

      // 图象生效
      g.dispose();

      // 输出图象到页面
      ImageIO.write(image, "JPEG", out);
   }
}
