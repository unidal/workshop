package com.site.wdbc.koubei;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter {
   @SuppressWarnings("unused")
   private Configuration m_configuration;

   @SuppressWarnings("unused")
   private int m_minHours;

   @SuppressWarnings("unused")
   private int m_maxDays;

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String title = (String) result.getCell(row, "title");
      String link = (String) result.getCell(row, "link");

      if (link == null || title == null) {
         return true;
      } else {
         String address = (String) result.getCell(row, "address");

         result.setValue(row, "address", address.substring(0, address.length() - 5));
         return false;
      }
   }

   public void setMinHours(int minHours) {
      m_minHours = minHours;
   }

   public void setMaxDays(int maxDays) {
      m_maxDays = maxDays;
   }

}
