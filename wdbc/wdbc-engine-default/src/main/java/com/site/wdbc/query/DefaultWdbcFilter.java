package com.site.wdbc.query;

import com.site.wdbc.WdbcResult;

public class DefaultWdbcFilter implements WdbcFilter {
   public void doFilter(WdbcResult result) {
      int rowSize = result.getRowSize();

      for (int i = rowSize - 1; i >= 0; i--) {
         if (shouldRemoveRow(result, i)) {
            result.removeRow(i);
         }
      }
   }

   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      // override it
      return false;
   }

   protected void aggregate(WdbcResult result, String rowSeparator) {
      StringBuffer sb = new StringBuffer(1024);
      int rows = result.getRowSize();
      int cols = result.getColumnSize();
      Object[] aggregated = new Object[cols];

      for (int col = 0; col < cols; col++) {
         boolean first = true;

         sb.setLength(0);

         for (int row = 0; row < rows; row++) {
            String cell = (String) result.getCell(row, col);

            if (cell != null) {
               if (first) {
                  first = false;
               } else if (rowSeparator != null) {
                  sb.append(rowSeparator);
               }

               sb.append(cell);
            }
         }

         aggregated[col] = sb.toString();
      }

      result.clear();

      for (int col = 0; col < cols; col++) {
         result.addValue(col, aggregated[col]);
      }

      result.applyLastRow();
   }

   protected String aggregate(WdbcResult result, String colName, String rowSeparator) {
      StringBuffer sb = new StringBuffer(1024);
      int rows = result.getRowSize();
      boolean first = true;

      for (int row = 0; row < rows; row++) {
         String cell = (String) result.getCell(row, colName);

         if (cell != null) {
            if (first) {
               first = false;
            } else if (rowSeparator != null) {
               sb.append(rowSeparator);
            }

            sb.append(cell);
         }
      }

      return sb.toString();
   }
}
