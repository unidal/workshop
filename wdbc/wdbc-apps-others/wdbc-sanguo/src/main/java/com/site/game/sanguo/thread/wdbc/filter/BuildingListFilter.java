package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class BuildingListFilter extends DefaultWdbcFilter {
   private MessageFormat m_idFormat = new MessageFormat("{0}&bid={1}'')");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String id = (String) result.getCell(row, "id");
      String type = ((String) result.getCell(row, "type")).trim();

      if (id == null || type == null) {
         return true;
      } else {
         try {
            Object[] parts = m_idFormat.parse(id);

            if (parts.length == 2) {
               result.setValue(row, "id", parts[1]);
            }
         } catch (ParseException e) {
            return true;
         }

         int pos = type.indexOf(' ');

         if (pos > 0) {
            result.setValue(row, "type", type.substring(0, pos));
            result.setValue(row, "level", type.substring(pos + 1).trim());
         } else {
            result.setValue(row, "type", type);
         }

         return false;
      }
   }
}
