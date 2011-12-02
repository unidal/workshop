package org.unidal.ezsell.api.ebay.trading.wdbc;

import com.site.wdbc.http.configuration.WdbcFieldMeta;
import com.site.wdbc.http.configuration.WdbcMeta;

@WdbcMeta(name = "CompleteSale")
public class EbayCompleteSale extends AbstractWdbc {
   @WdbcFieldMeta("Ack")
   private String m_ack;

   public String getAck() {
      return m_ack;
   }
}
