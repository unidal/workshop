package com.site.wdbc.ebay.arch;

import java.net.URL;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class CourseFilter extends DefaultWdbcFilter {
   private Session m_session;

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      URL lastUrl = m_session.getLastUrl();
      String link = result.getString(row, "link");
      String title = result.getString(row, "title");

      if (link != null && title != null && link.contains("view.php") && !title.contains("OBSOLETE")) {
         try {
            result.setValue(row, "link", new URL(lastUrl, link).toExternalForm());
         } catch (Exception e) {
            e.printStackTrace();
         }

         return false;
      } else {
         return true;
      }
   }
}
