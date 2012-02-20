package com.site.wdbc.scbit;

import java.net.URL;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class SummaryFilter extends DefaultWdbcFilter {
   private Session m_session;

   @Override
   public void doFilter(WdbcResult result) {
      int rows = result.getRowSize();
      String title = null;
      String category = null;
      String categoryLink = null;
      String description = null;

      for (int row = 0; row < rows; row++) {
         if (title == null) {
            title = (String) result.getCell(row, "title");
         }

         if (categoryLink == null) {
            String link = (String) result.getCell(row, "category-link");

            if (link != null && link.startsWith("/nar/database/cat/")) {
               try {
                  if (m_session != null) {
                     categoryLink = new URL(m_session.getLastUrl(), link).toExternalForm();
                     category = (String) result.getCell(row, "category");
                  }
               } catch (Exception e) {
                  // ignore it
               }
            }
         }

         if (description == null) {
            String hint = (String) result.getCell(row, "hint");

            if (hint != null && hint.equals("Database Description") && row + 1 < rows) {
               description = (String) result.getCell(row + 1, "description");
            }
         }
      }

      result.clear();
      result.addValue("title", title);
      result.addValue("link", null);
      result.addValue("category", category);
      result.addValue("category-link", categoryLink);
      result.addValue("description", description);
      result.applyLastRow();
   }
}
