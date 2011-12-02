package org.unidal.ezsell.transaction;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.api.ebay.EbayApiManager;
import org.unidal.ezsell.api.ebay.trading.CompleteSale;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.Transaction;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.WdbcException;

public class TransactionProcessor implements LogEnabled {
   @Inject
   private EbayApiManager m_apiManager;

   @Inject
   private CompleteSale m_completeSale;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public boolean markAsShipped(Seller seller, Transaction trx) throws HttpException, IOException, WdbcException {
      int id = trx.getId();
      TransactionStatus status = TransactionStatus.getByValue(trx.getStatus(), TransactionStatus.UNKNOWN);

      switch (status) {
      case PAID:
         Trading trading = m_apiManager.getTrading(seller);
         String content = trading.markAsShipped(trx.getItemId(), trx.getTransactionId(), trx.getShippingTrackingId(),
               trx.getShippingCarrier());
         boolean success = m_completeSale.completeSale(content);

         if (success) {
            trx.setShippedTime(new Date());
            trx.setStatus(TransactionStatus.SHIPPED.getValue());

            return true;
         } else {
            m_logger.warn("Transaction(" + id + ") can't be marked as shipped.");
         }
         break;
      default:
         m_logger.warn("Can't mark as shipped for transaction(" + id + ") with status: " + status.getName());
      }

      return false;
   }
}
