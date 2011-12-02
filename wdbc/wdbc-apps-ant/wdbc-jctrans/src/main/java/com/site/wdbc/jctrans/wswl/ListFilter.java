package com.site.wdbc.jctrans.wswl;

import java.net.URL;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter {
   private Session m_session;

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String title = (String) result.getCell(row, "title");
      String link = (String) result.getCell(row, "link");

      if (link == null || title == null) {
         return true;
      } else {
         try {
            if (m_session != null) {
               link = new URL(m_session.getLastUrl(), link).toExternalForm();
            }
         } catch (Exception e) {
            // ignore it
         }

         int pos1 = link.lastIndexOf('/');
         int pos2 = link.lastIndexOf('.');

         if (pos1 > 0 && pos2 > pos1) {
            result.setValue(row, "id", link.substring(pos1 + 1, pos2));
         } else {
            return true;
         }

         result.setValue(row, "link", link);
         return false;
      }
   }
}
