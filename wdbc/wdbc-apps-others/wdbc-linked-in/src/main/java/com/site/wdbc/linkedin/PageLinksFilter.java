package com.site.wdbc.linkedin;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class PageLinksFilter extends DefaultWdbcFilter {
   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      try {
         String num = (String) result.getCell(row, "num");

         Integer.parseInt(num);
         return false;
      } catch (Exception e) {
         // It's NOT a number, remove it
         return true;
      }
   }

}
