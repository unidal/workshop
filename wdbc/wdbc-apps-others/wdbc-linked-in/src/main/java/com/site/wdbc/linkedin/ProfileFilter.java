package com.site.wdbc.linkedin;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ProfileFilter extends DefaultWdbcFilter {

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String profile = (String) result.getCell(row, "profile");

      if ("View public profile".equals(profile)) {
         return false;
      }

      return true;
   }
}
