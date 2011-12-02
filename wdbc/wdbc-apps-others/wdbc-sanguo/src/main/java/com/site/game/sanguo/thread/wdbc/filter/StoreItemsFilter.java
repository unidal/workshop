package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class StoreItemsFilter extends DefaultWdbcFilter {
   private MessageFormat m_idFormat = new MessageFormat(
            "{0}({1}, {2}, \\''{3}\\'' ,\\''{4}\\'', \\''{5}\\'', 4, \\''{6}\\'',\\''{7}\\'',\\''1\\'');");

   private String getType(String type) {
      if (type == null) {
         return null;
      } else if (type.contains("武力")) {
         return "武力";
      } else if (type.contains("统御")) {
         return "统御";
      } else if (type.contains("智力")) {
         return "智力";
      } else if (type.contains("政治")) {
         return "政治";
      } else {
         return null;
      }
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String id = (String) result.getCell(row, "id");

      if (id == null) {
         return true;
      } else {
         try {
            Object[] parts = m_idFormat.parse(id);

            if (parts.length > 5) {
               result.setValue(row, "id", parts[1]);
               result.setValue(row, "points", parts[2]);
               result.setValue(row, "name", parts[3]);
               result.setValue(row, "description", parts[4]);
               result.setValue(row, "type", getType((String) parts[5]));

               if (((String) parts[6]).length() != 0) {
                  result.setValue(row, "count", (String) parts[6]);
               } else {
                  result.setValue(row, "count", "0");
               }
            }
         } catch (ParseException e) {
            return true;
         }

         return false;
      }
   }
}
