package com.site.wdbc.linkedin;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ResultSetFilter extends DefaultWdbcFilter {
   private static MessageFormat s_format1 = new MessageFormat("{0}&key={1}&{2}");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String link = (String) result.getCell(row, "link");
      String id = null;

      try {
         Object[] parts = s_format1.parse(link);

         id = (String) parts[1];
         result.setValue(row, "id", id);
      } catch (ParseException e) {
         // ignore it
         return true;
      }

      return false;
   }
}
