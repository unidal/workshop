package com.site.wdbc.zhantai;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   @Override
   public void doFilter(WdbcResult result) {
      String title = aggregate(result, "title", null);
      String body = aggregate(result, "body", "\r\n");
      String contact = aggregate(result, "contact", "\n");
      String category1 = aggregate(result, "category1", null);
      String category2 = aggregate(result, "category2", null);
      String district1 = aggregate(result, "district1", null);
      String district2 = aggregate(result, "district2", null);

      String email = null;
      String mobilephone = null;
      String telephone = null;
      String date = null;
      StringBuffer contactBuf = new StringBuffer(256);

      String[] parts = contact.split("\n");

      for (String part : parts) {
         if (part.startsWith("手机/电话:")) {
            String phone = part.substring("手机/电话:".length()).trim();

            if (phone.startsWith("1")) {
               mobilephone = phone;
            } else {
               telephone = phone;
            }
            
            contactBuf.append(part.trim());
         } else if (part.startsWith("发布时间:")) {
            date = part.substring("发布时间:".length()).trim();
         } else if (part.indexOf('@') > 0) {
            email = part.trim();
            
            contactBuf.append("联系EMail: ").append(part.trim()).append("\r\n");
         }
      }

      result.clear();
      result.addValue("title", title);
      result.addValue("body", body.replace('>', ' '));
      result.addValue("contact", contactBuf.toString());
      result.addValue("category", category1 + " > " + category2);
      result.addValue("district", district1 + " : " + district2);
      result.addValue("mobilephone", mobilephone);
      result.addValue("telephone", telephone);
      result.addValue("email", email);
      result.addValue("date", date);
      result.applyLastRow();
   }
}
