package com.site.wdbc.whois;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   private String m_prefix = "Expiration Date: ";

   @Override
   public void doFilter(WdbcResult result) {
      String content = aggregate(result, "content", " ");
      int pos1 = content.indexOf(m_prefix);
      boolean found = false;

      result.clear();

      if (pos1 > 0) {
         int pos2 = content.indexOf("\n", pos1 + m_prefix.length());

         if (pos2 > 0) {
            String expiration = content.substring(pos1 + m_prefix.length(), pos2).trim();

            result.addValue("expiration", expiration);
            found = true;
         }
      }

      if (!found) {
         result.addValue("expiration", "");
      }

      result.applyLastRow();
   }
}
