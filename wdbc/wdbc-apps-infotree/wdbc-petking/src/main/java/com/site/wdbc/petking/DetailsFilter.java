package com.site.wdbc.petking;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      String title = aggregate(result, "title", null);
      String content = aggregate(result, "content", "\r\n");
      String contact = aggregate(result, "contact", "\n");
      String category = aggregate(result, "category", null);
      String district = aggregate(result, "district", null);
      String date = aggregate(result, "date", null);
      String picture = aggregate(result, "picture", null);
      String qq = aggregate(result, "qq", null);

      String email = null;
      String mobilephone = null;
      String telephone = null;
      StringBuffer contactBuf = new StringBuffer(256);

      String[] parts = contact.split("\n");

      for (String part : parts) {
         if (part.startsWith("电话：")) {
            String phone = part.substring("电话：".length()).trim();

            if (phone.length() == 11 && phone.startsWith("1")) {
               mobilephone = phone;
            } else {
               telephone = phone;
            }
         } else if (part.startsWith("E-mail：")) {
            email = part.substring("E-mail：".length()).trim();
         }

         if (part.startsWith("房型：") || part.startsWith("租金：") || part.startsWith("面积：")) {
            // ignore it
         } else {
            contactBuf.append(part.trim()).append("\r\n");
         }
      }

      result.clear();
      result.addValue("title", title.startsWith("标题：") ? title.substring(3) : title);
      result.addValue("body", content);
      result.addValue("contact", contactBuf.toString());
      result.addValue("category", category.length() > 5 ? category.substring(5) : category);
      result.addValue("district", district);
      result.addValue("mobilephone", mobilephone);
      result.addValue("telephone", telephone);
      result.addValue("email", email);
      result.addValue("qq", qq);
      result.addValue("date", date);
      result.addValue("picture", picture);
      result.applyLastRow();
   }
}
