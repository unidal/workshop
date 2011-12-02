package com.site.wdbc.taobao;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class NextPageFilter extends DefaultWdbcFilter {

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String img = (String) result.getCell(row, "img");
      String link = (String) result.getCell(row, "link");

      if (link == null || img == null) {
         return true;
      } else {
         if (img.indexOf("next_page") > 0) {
            return false;
         } else {
            return true;
         }
      }
   }
}
