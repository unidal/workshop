package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class GeneralDetailFilter extends DefaultWdbcFilter {
   private MessageFormat m_format = new MessageFormat("{0}goods/{1}.jpg");

   @Override
   public void doFilter(WdbcResult result) {
      aggregate(result, " ");

      super.doFilter(result);
   }

   private String getItemId(String link) {
      try {
         return (String) m_format.parse(link)[1];
      } catch (Exception e) {
         // ignore it
      }

      return null;
   }

   private String getValue(String value) {
      try {
         return (String) value.split("/")[0];
      } catch (Exception e) {
         // ignore it
      }

      return null;
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String name = (String) result.getCell(row, "name");

      if (name == null || name.length() == 0) {
         return true;
      } else {
         String[] parts = name.split(" ");

         result.setValue(row, "name", parts[0]);
         result.setValue(row, "exp", parts[1]);
         result.setValue(row, "village", parts[parts.length - 1]);

         result.setValue(row, "tili", getValue((String) result.getCell(row, "tili")));
         result.setValue(row, "wuli", getValue((String) result.getCell(row, "wuli")));
         result.setValue(row, "tongyu", getValue((String) result.getCell(row, "tongyu")));
         result.setValue(row, "zhili", getValue((String) result.getCell(row, "zhili")));
         result.setValue(row, "zhenzhi", getValue((String) result.getCell(row, "zhenzhi")));
         result.setValue(row, "baowu", getItemId((String) result.getCell(row, "baowu")));
         result.setValue(row, "zuoqi", getItemId((String) result.getCell(row, "zuoqi")));
         result.setValue(row, "juanzhuo", getItemId((String) result.getCell(row, "juanzhuo")));

         return false;
      }
   }
}
