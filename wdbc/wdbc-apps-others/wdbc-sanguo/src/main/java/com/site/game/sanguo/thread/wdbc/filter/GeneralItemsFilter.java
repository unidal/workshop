package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class GeneralItemsFilter extends DefaultWdbcFilter {
   private MessageFormat m_idFormat = new MessageFormat("{0}&goodid={1}\\'');");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String id = (String) result.getCell(row, "id");

      if (id == null) {
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

         return false;
      }
   }
}
