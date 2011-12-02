package com.site.wdbc.linkedin;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class JobsFilter extends DefaultWdbcFilter {
   private MessageFormat m_yearFormat = new MessageFormat("{0} ¨C {1} ({2}){3}");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String flag = (String) result.getCell(row, "flag");

      if (!"overviewpos".equals(flag)) {
         return true;
      }

      String year = (String) result.getCell(row, "year");

      if (year != null) {
         if (year.trim().equals("Currently holds this position")) {
            result.setValue(row, "from", null);
            result.setValue(row, "to", "present");

            return false;
         } else {
            try {
               Object[] parts = m_yearFormat.parse(year);

               result.setValue(row, "from", parts[0]);
               result.setValue(row, "to", parts[1]);

               return false;
            } catch (ParseException e) {
               // Not a year format, remove it
            }
         }
      }

      return true;
   }
}
