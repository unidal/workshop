package com.site.wdbc.baixing;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   @Override
   public void doFilter(WdbcResult result) {
      super.doFilter(result);

      aggregate(result, "\r\n");
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String desc = (String) result.getCell(row, "desc");

      if (desc == null || desc.length() == 0) {
         return true;
      } else {
         if (desc.equals("注册或登陆用户可以提问！")) {
            return true;
         } else if (desc.contains("该信息暂时没有")) {
            return true;
         } else if (desc.startsWith("ver. r")) {
            return true;
         }

         return false;
      }
   }
}
