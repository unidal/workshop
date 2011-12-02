package com.site.wdbc.qm120;

import java.net.MalformedURLException;
import java.net.URL;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter {
   private Session m_session;

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
         URL lastUrl = m_session.getLastUrl();

         if (lastUrl != null) {
            try {
               result.setValue(row, "link", new URL(lastUrl, link).toExternalForm());
            } catch (MalformedURLException e) {
               // ignore it
            }
         }

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
