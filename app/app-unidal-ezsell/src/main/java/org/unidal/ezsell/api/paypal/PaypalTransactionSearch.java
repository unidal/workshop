package org.unidal.ezsell.api.paypal;

import java.util.Date;
import java.util.List;

@PaypalMeta(name = "GetTransactionDetails")
public class PaypalTransactionSearch extends AbstractPaypal {
   @PaypalFieldMeta("Ack")
   private String m_ack;

   @PaypalFieldMeta(value = "transactions", property = false)
   private List<Transaction> m_transactions;

   public String getAck() {
      return m_ack;
   }

   public List<Transaction> getTransactions() {
      return m_transactions;
   }

   @PaypalMeta(name = "transaction")
   public static class Transaction extends AbstractPaypal {
      @PaypalFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss'Z'", value = "Timestamp")
      private Date m_transactionDate;

      @PaypalFieldMeta("Timezone")
      private String m_timezone;

      @PaypalFieldMeta("Type")
      private String m_type;

      @PaypalFieldMeta(value = "Email", required = false)
      private String m_email;

      @PaypalFieldMeta("Name")
      private String m_name;

      @PaypalFieldMeta("TransactionId")
      private String m_id;

      @PaypalFieldMeta("Status")
      private String m_stastus;

      @PaypalFieldMeta("Amt")
      private double m_amount;

      @PaypalFieldMeta("FeeAmt")
      private double m_feeAmount;

      @PaypalFieldMeta("NetAmt")
      private double m_netAmount;

      @PaypalFieldMeta("CurrencyCode")
      private String m_currencyCode;

      public double getAmount() {
         return m_amount;
      }

      public String getCurrencyCode() {
         return m_currencyCode;
      }

      public String getEmail() {
         return m_email;
      }

      public double getFeeAmount() {
         return m_feeAmount;
      }

      public String getId() {
         return m_id;
      }

      public String getName() {
         return m_name;
      }

      public double getNetAmount() {
         return m_netAmount;
      }

      public String getStastus() {
         return m_stastus;
      }

      public String getTimezone() {
         return m_timezone;
      }

      public Date getTransactionDate() {
         return m_transactionDate;
      }

      public String getType() {
         return m_type;
      }
   }
}
