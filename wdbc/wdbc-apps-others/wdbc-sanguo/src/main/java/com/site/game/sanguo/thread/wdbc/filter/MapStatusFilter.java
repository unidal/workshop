package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class MapStatusFilter extends DefaultWdbcFilter {
   private MessageFormat m_format = new MessageFormat(
         "MM_mapOver(this, ''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'')");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String data = (String) result.getCell(row, "emperor");

      if (data == null) {
         return true;
      } else {
         try {
            Object[] parts = m_format.parse(data);

            if (parts.length == 6) {
               if ("".equals(parts[0]) || "".equals(parts[1])) {
                  return true;
               }

               result.setValue(row, "emperor", parts[0]);
               result.setValue(row, "population", parts[1]);
               result.setValue(row, "alliance", parts[2]);
               result.setValue(row, "x", parts[3]);
               result.setValue(row, "y", parts[4]);
               result.setValue(row, "state", parts[5]);

               return false;
            }
         } catch (ParseException e) {
            // ignore it
         }

         return true;
      }
   }
}
