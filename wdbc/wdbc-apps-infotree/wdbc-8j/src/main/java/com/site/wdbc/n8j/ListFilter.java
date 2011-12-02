package com.site.wdbc.n8j;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter {
   @SuppressWarnings("unused")
   private Configuration m_configuration;

   @SuppressWarnings("unused")
   private int m_minHours;

   @SuppressWarnings("unused")
   private int m_maxDays;

   private String m_phone;
   private String m_address;

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String title = (String) result.getCell(row, "title");
      String link = (String) result.getCell(row, "link");
      String desc = (String) result.getCell(row, "desc");

      if (link == null || title == null) {
         if (desc != null && desc.length() > 4) {
            if (m_phone == null) {
               m_phone = desc;
            } else if (m_address == null) {
               m_address = desc;
            }
         }

         return true;
      } else {
         result.setValue(row, "address", m_address);
         result.setValue(row, "phone", m_phone);
         m_phone = null;
         m_address = null;

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
