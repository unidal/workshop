package com.site.wdbc.wuba;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   @Override
   public void doFilter(WdbcResult result) {
      int rows = result.getRowSize();
      StringBuffer titleBuf = new StringBuffer();
      StringBuffer contentBuf = new StringBuffer(2048);
      StringBuffer contactBuf = new StringBuffer(1024);
      String picture = null;
      String category = null;
      String district = null;
      String mobilephone = null;
      String telephone = null;
      String msn = null;
      String qq = null;
      String email = null;

      for (int row = 0; row < rows; row++) {
         String title = (String) result.getCell(row, "title");
         String content1 = (String) result.getCell(row, "content1");
         String content2 = (String) result.getCell(row, "content2");
         String contact = (String) result.getCell(row, "contact");
         String header = (String) result.getCell(row, "header");
         String pic = (String) result.getCell(row, "picture");

         if (title != null && title.length() > 0) {
            titleBuf.append(title);
         }

         if (content1 != null && content1.length() > 0) {
            contentBuf.append(content1).append("\r\n");

            if (content2 != null && content2.length() > 0) {
               contentBuf.append("\r\n");
            }
         }

         if (content2 != null && content2.length() > 0) {
            contentBuf.append(content2);
         }

         if (contact != null && contact.length() > 0) {
            if (contact.startsWith("联系：")) {
               String phone = contact.substring("联系：".length());

               if (phone.startsWith("1")) {
                  mobilephone = phone;
               } else {
                  telephone = phone;
               }
            } else if (contact.startsWith("邮件：")) {
               email = contact.substring("邮件：".length());
            } else if (contact.startsWith("QQ/MSN：")) {
               String im = contact.substring("QQ/MSN：".length());

               if (im.indexOf('@') > 0) {
                  msn = im;
               } else {
                  qq = im;
               }
            }

            contactBuf.append(contact).append("\r\n");
         }

         if (header != null && header.length() > 0) {
            int pos1 = header.indexOf("类别: ");
            int pos2 = header.indexOf("位置: ");

            if (pos1 >= 0 && pos2 > 0) {
               category = header.substring("类别: ".length(), pos2).trim();
               district = header.substring(pos2 + "位置: ".length()).trim();
            }
         }

         if (pic != null && pic.length() > 0) {
            picture = pic;
         }
      }

      result.clear();
      result.addValue("title", titleBuf.toString());
      result.addValue("body", contentBuf.toString());
      result.addValue("contact", contactBuf.toString());
      result.addValue("category", category);
      result.addValue("district", district);
      result.addValue("picture", picture);
      result.addValue("mobilephone", mobilephone);
      result.addValue("telephone", telephone);
      result.addValue("msn", msn);
      result.addValue("qq", qq);
      result.addValue("email", email);
      result.applyLastRow();
   }
}
