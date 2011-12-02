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
      // ���ڴ��д���ͼ��
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      // ��ȡͼ��������
      Graphics g = image.getGraphics();

      // �趨����ɫ
      g.setColor(new Color(0xDCDCDC));
      g.fillRect(0, 0, width, height);

      // ���߿�
      g.setColor(Color.black);
      g.drawRect(0, 0, width - 1, height - 1);

      // ����֤����ʾ��ͼ����
      g.setColor(Color.black);

      String str = "0000" + value;

      g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
      g.drawString(str.substring(str.length() - 4), 8, 17);

      // ͼ����Ч
      g.dispose();

      // ���ͼ��ҳ��
      ImageIO.write(image, "JPEG", out);
   }
}
