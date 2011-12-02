package org.unidal.ezsell.api.ebay.trading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.transaction.TransactionStatus;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSourceType;

public class GetSellerTransactions {
   @Inject
   private WdbcQuery m_query;

   @Inject
   private WdbcEngine m_engine;

   public List<Transaction> getTransactions(int sellerId, String content) throws WdbcException {
      WdbcResult result = m_engine.execute(m_query, new StringSource(WdbcSourceType.XML, content));

      return getTransactions(sellerId, result);
   }

   private List<Transaction> getTransactions(int sellerId, WdbcResult result) {
      List<Transaction> list = new ArrayList<Transaction>();
      int rows = result.getRowSize();

      for (int row = 0; row < rows; row++) {
         Transaction trx = new Transaction();

         trx.setSellerId(sellerId);
         trx.setTransactionCreationDate(result.getDate(row, "CreatedDate", null, "yyyy-MM-dd HH:mm:ss"));
         trx.setAmountPaid(result.getDouble(row, "AmountPaid", 0));
         trx.setAdjustmentAmount(result.getDouble(row, "AdjustmentAmount", 0));
         trx.setShippingCost(result.getDouble(row, "ShippingCost", 0));
         trx.setFinalValueFee(result.getDouble(row, "FinalValueFee", 0));
         trx.setPaidTime(result.getDate(row, "PaidTime", null, "yyyy-MM-dd HH:mm:ss"));
         trx.setShippedTime(result.getDate(row, "ShippedTime", null, "yyyy-MM-dd HH:mm:ss"));
         trx.setQuantityPurchased(result.getInt(row, "QuantityPurchased", 0));
         trx.setOrderId(result.getLong(row, "OrderID", 0));
         trx.setTransactionId(result.getLong(row, "TransactionID", 0));
         trx.setTransactionPrice(result.getDouble(row, "TransactionPrice", 0));
         trx.setTransactionSiteId(result.getString(row, "TransactionSiteID"));
         trx.setPaymentId(result.getString(row, "ExTransactionID"));
         trx.setPaymentFee(result.getDouble(row, "ExFeeOrCreditAmount", 0));
         trx.setItemId(result.getLong(row, "ItemID", 0));
         trx.setRelistedItemId(result.getLong(row, "RelistedItemID", 0));
         trx.setItemTitle(result.getString(row, "Title"));
         trx.setShippingTrackingId(result.getString(row, "ShipmentTrackingNumber"));
         trx.setBuyerAccount(result.getString(row, "BuyerID"));
         trx.setBuyerName(result.getString(row, "Name"));
         trx.setCheckoutMessage(result.getString(row, "BuyerCheckoutMessage"));
         trx.setStatus(getTransactionStatus(trx).getValue());

         list.add(trx);
      }

      return list;
   }

   private TransactionStatus getTransactionStatus(Transaction trx) {
      Date paidTime = trx.getPaidTime();
      Date shippedTime = trx.getShippedTime();
      double amountPaid = trx.getAmountPaid();
      TransactionStatus status = null;

      if (paidTime == null && shippedTime == null && amountPaid > 0) {
         status = TransactionStatus.NOT_PAID;
      } else if (paidTime == null && (shippedTime != null || amountPaid < 1e-6)) {
         status = TransactionStatus.REFUNDED;
      } else if (paidTime != null && shippedTime != null && amountPaid > 0) {
         status = TransactionStatus.SHIPPED;
      } else if (paidTime != null && shippedTime == null && amountPaid > 0) {
         status = TransactionStatus.PAID;
      }

      return status;
   }
}
