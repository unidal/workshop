package com.site.game.sanguo.thread.wdbc.filter;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class CourtListFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      super.doFilter(result);

      if (result.getRowSize() == 2) {
         result.removeRow(0);
      }
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String hasButton = (String) result.getCell(row, "hasButton");
      String date = (String) result.getCell(row, "date");

      if (date == null) {
         return true;
      } else {
         result.setValue(row, "hasButton", hasButton != null && hasButton.contains("emperor.state_court"));

         return false;
      }
   }
}
