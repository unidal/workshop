package com.site.wdbc.whycools;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      StringBuffer contentBuf = new StringBuffer(1024);
      StringBuffer contactBuf = new StringBuffer(256);
      String email = aggregate(result, "email", "@");
      String mobilephone = null;
      String telephone = null;
      String qq = null;
      String msn = null;

      int rows = result.getRowSize();

      for (int row = 0; row < rows; row++) {
         String content1 = (String) result.getCell(row, "content1");
         String content2 = (String) result.getCell(row, "content2");

         if (content2 == null || content2.length() == 0) {
            continue;
         }

         if ("电话:".equals(content1)) {
            String phone = content2;

            if (phone.length() == 11 && phone.startsWith("1")) {
               mobilephone = phone;
            } else {
               telephone = phone;
            }

            contactBuf.append(content1).append(' ').append(content2).append("\r\n");
         } else if ("QQ:".equals(content1)) {
            qq = content2;

            contactBuf.append(content1).append(' ').append(content2).append("\r\n");
         } else if ("MSN:".equals(content1)) {
            msn = content2;

            contactBuf.append(content1).append(' ').append(content2).append("\r\n");
         } else if ("联系方式:".equals(content1)) {
            if (content2.indexOf('@') > 0) {
               email = content2;
            }

            contactBuf.append(content1).append(' ').append(content2).append("\r\n");
         }

         contentBuf.append(content1).append(' ').append(content2).append("\r\n");
      }

      result.clear();
      result.addValue("body", contentBuf.toString());
      result.addValue("contact", contactBuf.toString());
      result.addValue("category", "");
      result.addValue("district", "");
      result.addValue("mobilephone", mobilephone);
      result.addValue("telephone", telephone);
      result.addValue("email", email);
      result.addValue("qq", qq);
      result.addValue("msn", msn);
      result.applyLastRow();
   }
}
