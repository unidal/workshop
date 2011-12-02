package com.site.wdbc.ebay.arch;

import java.net.URL;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class SectionFilter extends DefaultWdbcFilter {
   private Session m_session;

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      URL lastUrl = m_session.getLastUrl();
      String title = result.getString(row, "title");
      String link = result.getString(row, "link");

      if (title != null && (title.contains("Section Review ") || title.contains("Lesson Review ") || title.contains("Quiz"))) {
         return true;
      } else if (link != null) {
         try {
            result.setValue(row, "link", new URL(lastUrl, link).toExternalForm());
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return false;
   }
}
