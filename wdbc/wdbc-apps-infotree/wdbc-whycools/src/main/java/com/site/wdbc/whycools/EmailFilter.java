package com.site.wdbc.whycools;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class EmailFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      String email = aggregate(result, "email", "@");

      result.clear();
      result.addValue("email", email);
      result.applyLastRow();
   }
}
