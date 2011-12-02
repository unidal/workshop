package com.site.wdbc.sosoko;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      String title = aggregate(result, "title", null);
      String content = aggregate(result, "content", "\r\n");
      String properties = aggregate(result, "properties", "\r\n");
      String contact = aggregate(result, "contact", "\n");
      String category = aggregate(result, "category", null);
      String district = aggregate(result, "district", null);
      String date = aggregate(result, "date", null);

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
         } else if (part.startsWith("其他联系方式：")) {
            String phone = part.substring("其他联系方式：".length()).trim();

            if (phone.length() == 11 && phone.startsWith("1")) {
               mobilephone = phone;
            } else {
               telephone = phone;
            }
         }

         if (part.startsWith("房型：") || part.startsWith("租金：") || part.startsWith("面积：")) {
            // ignore it
         } else {
            contactBuf.append(part.trim()).append("\r\n");
         }
      }

      result.clear();
      result.addValue("title", title);
      result.addValue("body", content + "\r\n" + properties);
      result.addValue("contact", contactBuf.toString());
      result.addValue("category", category.length() > 5 ? category.substring(5) : category);
      result.addValue("district", district.length() > 5 ? district.substring(5) : district);
      result.addValue("mobilephone", mobilephone);
      result.addValue("telephone", telephone);
      result.addValue("email", email);
      result.addValue("date", date.length() > 5 ? date.substring(5) : date);
      result.applyLastRow();
   }
}
