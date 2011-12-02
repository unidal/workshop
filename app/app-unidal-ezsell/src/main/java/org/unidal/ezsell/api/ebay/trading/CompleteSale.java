package org.unidal.ezsell.api.ebay.trading;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSourceType;

public class CompleteSale {
   @Inject
   private WdbcQuery m_query;

   @Inject
   private WdbcEngine m_engine;

   public boolean completeSale(String content) throws WdbcException {
      WdbcResult result = m_engine.execute(m_query, new StringSource(WdbcSourceType.XML, content));

      return completeSale(result);
   }

   private boolean completeSale(WdbcResult result) {
      int rows = result.getRowSize();

      if (rows > 0) {
         String ack = result.getString(0, "Ack");

         return "Success".equals(ack);
      }

      return false;
   }
}
