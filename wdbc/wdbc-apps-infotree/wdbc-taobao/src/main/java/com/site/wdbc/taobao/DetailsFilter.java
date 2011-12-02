package com.site.wdbc.taobao;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String descLink = (String) result.getCell(row, "desc-link");

      if (descLink == null) {
         return true;
      } else {
         if (descLink.indexOf("var^desc") > 0) {
            result.setValue(row, "desc-link", replaceBadCharacters(descLink));
            return false;
         } else {
            return true;
         }
      }
   }

   private String replaceBadCharacters(String link) {
      int len = link.length();
      StringBuffer sb = new StringBuffer(len + 16);

      for (int i = 0; i < len; i++) {
         char ch = link.charAt(i);
         switch (ch) {
         case '|':
            sb.append("%7C");
            break;
         case '^':
            sb.append("%5E");
            break;
         default:
            sb.append(ch);
         }
      }

      return sb.toString();
   }
}
