package com.site.wdbc.jctrans.cargoinfo;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String title = (String) result.getCell(row, "title");
      String date = (String) result.getCell(row, "date");

      if (title == null || date == null) {
         return true;
      } else {
         return false;
      }
   }
}
