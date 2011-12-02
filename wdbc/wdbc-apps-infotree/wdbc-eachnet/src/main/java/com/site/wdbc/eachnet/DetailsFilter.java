package com.site.wdbc.eachnet;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   private static String START_TEXT = "var describeURL='";

   private static String END_TEXT = "';";

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String descLink = (String) result.getCell(row, "desc-link");

      if (descLink == null) {
         return true;
      } else {
         if (descLink.indexOf(START_TEXT) > 0) {
            result.setValue(row, "desc-link", retrieveDescriptionLink(descLink));
            return false;
         } else {
            return true;
         }
      }
   }

   private String retrieveDescriptionLink(String text) {
      int start = text.indexOf(START_TEXT) + START_TEXT.length();
      int end = text.indexOf(END_TEXT, start);

      if (start > 0 && end > 0) {
         return text.substring(start, end);
      } else {
         return "";
      }
   }
}
