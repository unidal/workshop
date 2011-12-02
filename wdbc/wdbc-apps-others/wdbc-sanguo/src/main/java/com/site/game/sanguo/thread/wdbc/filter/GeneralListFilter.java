package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class GeneralListFilter extends DefaultWdbcFilter {
   private MessageFormat m_idFormat = new MessageFormat("{0}&gid={1}\\'');");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String id = (String) result.getCell(row, "id");
      String status = (String) result.getCell(row, "status");

      if (id == null || status == null || !status.equals("´ýÃü")) {
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
