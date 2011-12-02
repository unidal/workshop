package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ResourceListFilter extends DefaultWdbcFilter {
   private MessageFormat m_idFormat = new MessageFormat("{0}&rid={1}\\'')");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String id = (String) result.getCell(row, "id");
      String type = (String) result.getCell(row, "type");

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

         String[] parts = type.split(" ");
         
         result.setValue(row, "type", parts[0]);
         
         return false;
      }
   }
}
