package org.unidal.ezsell.api.ebay.trading.wdbc;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class GetSellerTransactionsFilter extends DefaultWdbcFilter {
   private static final String CDATA_SUFFIX = "]]>";

   private static final String CDATA_PREFIX = "<![CDATA[";

   private void removeCDATA(WdbcResult result, int row, String colName) {
      String str = (String) result.getCell(row, colName);
      String trimmed = str == null ? null : str.trim();

      if (trimmed != null && trimmed.startsWith(CDATA_PREFIX) && trimmed.endsWith(CDATA_SUFFIX)) {
         String removed = trimmed.substring(CDATA_PREFIX.length(), trimmed.length() - CDATA_SUFFIX.length());

         result.setValue(row, colName, removed);
      }
   }
   
   private void removeInvalidRequest(WdbcResult result, int row, String colName) {
      String str = (String) result.getCell(row, colName);
      String trimmed = str == null ? null : str.trim();
      
      if ("Invalid Request".equals(trimmed)) {
         result.setValue(row, colName, null);
      }
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      removeCDATA(result, row, "buyerCheckoutMessage");
      removeInvalidRequest(result, row, "buyerEmail");
      
      return false;
   }
}
