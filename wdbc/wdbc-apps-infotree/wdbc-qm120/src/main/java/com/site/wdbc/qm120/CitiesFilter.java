package com.site.wdbc.qm120;

import java.net.MalformedURLException;
import java.net.URL;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class CitiesFilter extends DefaultWdbcFilter {
   private Session m_session;

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String city = (String) result.getCell(row, "city");
      String link = (String) result.getCell(row, "link");

      if (link == null || city == null) {
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
}
