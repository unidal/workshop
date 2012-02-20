package com.site.wdbc.scbit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class CopyOfSummaryFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      String title = first(result, "title");
      String link = first(result, "link", new Handler() {
         @Override
         public boolean handle(WdbcResult result, int row, String colName, String colValue) {
            return !colValue.startsWith("MAILTO:") && colValue.indexOf(':') > 0;
         }
      });
      String category = first(result, "category", new Handler() {
         @Override
         public boolean handle(WdbcResult result, int row, String colName, String colValue) {
            return !colValue.startsWith("http:");
         }
      });
      String categoryLink = first(result, "category-link", new Handler() {
         @Override
         public boolean handle(WdbcResult result, int row, String colName, String colValue) {
            return colValue.startsWith("/nar/");
         }
      });

      List<String> descriptions = columns(result, "description", true);

      Collections.sort(descriptions, new Comparator<String>() {
         @Override
         public int compare(String o1, String o2) {
            return o1.length() > o2.length() ? -1 : 1;
         }
      });

      result.clear();
      result.addValue("title", title);
      result.addValue("link", link);
      result.addValue("category", category);
      result.addValue("category-link", categoryLink);
      result.addValue("description", descriptions.isEmpty() ? null : descriptions.get(0));
      result.applyLastRow();
   }

   protected String first(WdbcResult result, String colName) {
      return first(result, colName, null);
   }

   protected String first(WdbcResult result, String colName, Handler handler) {
      int rows = result.getRowSize();

      for (int row = 0; row < rows; row++) {
         String cell = (String) result.getCell(row, colName);

         if (cell != null && cell.length() > 0) {
            if (handler == null) {
               return cell;
            } else if (handler.handle(result, row, colName, cell)) {
               return cell;
            }
         }
      }

      return null;
   }

   protected List<String> columns(WdbcResult result, String colName, boolean ignoreEmpty) {
      int rows = result.getRowSize();
      List<String> values = new ArrayList<String>(rows);

      for (int row = 0; row < rows; row++) {
         String cell = (String) result.getCell(row, colName);

         if (cell != null) {
            values.add(cell);
         } else if (!ignoreEmpty) {
            values.add(cell);
         }
      }

      return values;
   }

   protected static interface Handler {
      public boolean handle(WdbcResult result, int row, String colName, String colValue);
   }
}
