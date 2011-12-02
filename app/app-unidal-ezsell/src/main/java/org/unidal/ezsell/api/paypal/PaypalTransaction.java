package org.unidal.ezsell.api.paypal;

import java.util.Date;
import java.util.List;

@PaypalMeta(name = "GetTransactionDetails")
public class PaypalTransaction extends AbstractPaypal {
   @PaypalFieldMeta("Ack")
   private String m_ack;

   @PaypalFieldMeta("Amt")
   private double m_amount;

   @PaypalFieldMeta("FeeAmt")
   private double m_feeAmount;
   
   @PaypalFieldMeta("TaxAmt")
   private double m_taxAmount;
   
   @PaypalFieldMeta("SettleAmt")
   private double m_settleAmount;

   @PaypalFieldMeta("CurrencyCode")
   private String m_currencyCode;

   @PaypalFieldMeta("ExchangeRate")
   private double m_exchangeRate;

   @PaypalFieldMeta("TransactionId")
   private String m_paymentId;
   
   @PaypalFieldMeta("ParentTransactionId")
   private String m_parentPaymentId;

   @PaypalFieldMeta("BuyerId")
   private String m_buyerAccount;
   
   @PaypalFieldMeta("Email")
   private String m_buyerEmail;
   
   @PaypalFieldMeta("OrderId")
   private long m_orderId;

   @PaypalFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss'Z'", value = "orderTime")
   private Date m_orderTime;
   
   @PaypalFieldMeta("PaymentType")
   private String m_paymentType;

   @PaypalFieldMeta("PaymentStatus")
   private String m_paymentStatus;
   
   @PaypalFieldMeta("ReceiptId")
   private String m_receiptId;

   @PaypalFieldMeta("Note")
   private String m_notes;

   @PaypalFieldMeta("PendingReason")
   private String m_pendingReason;

   @PaypalFieldMeta("ReasonCode")
   private String m_reasonCode;

   @PaypalFieldMeta(value = "shipTo")
   private ShipTo m_shipTo;

   @PaypalFieldMeta(value = "items", property = false)
   private List<Item> m_items;

   public String getAck() {
      return m_ack;
   }

   public double getAmount() {
      return m_amount;
   }

   public String getBuyerAccount() {
      return m_buyerAccount;
   }

   public String getBuyerEmail() {
      return m_buyerEmail;
   }

   public String getCurrencyCode() {
      return m_currencyCode;
   }

   public double getExchangeRate() {
      return m_exchangeRate;
   }

   public double getFeeAmount() {
      return m_feeAmount;
   }

   public List<Item> getItems() {
      return m_items;
   }

   public String getNotes() {
      return m_notes;
   }

   public long getOrderId() {
      return m_orderId;
   }

   public Date getOrderTime() {
      return m_orderTime;
   }

   public String getParentPaymentId() {
      return m_parentPaymentId;
   }

   public String getPaymentId() {
      return m_paymentId;
   }

   public String getPaymentStatus() {
      return m_paymentStatus;
   }

   public String getPaymentType() {
      return m_paymentType;
   }

   public String getPendingReason() {
      return m_pendingReason;
   }

   public String getReasonCode() {
      return m_reasonCode;
   }

   public String getReceiptId() {
      return m_receiptId;
   }

   public double getSettleAmount() {
      return m_settleAmount;
   }

   public ShipTo getShipTo() {
      return m_shipTo;
   }

   public double getTaxAmount() {
      return m_taxAmount;
   }

   @PaypalMeta(name = "Item")
   public static class Item extends AbstractPaypal {
      @PaypalFieldMeta("Name")
      private String m_itemTitle;

      @PaypalFieldMeta("Number")
      private long m_itemId;

      @PaypalFieldMeta("QTY")
      private int m_quantity;

      public long getItemId() {
         return m_itemId;
      }

      public String getItemTitle() {
         return m_itemTitle;
      }

      public int getQuantity() {
         return m_quantity;
      }
   }

   @PaypalMeta(name = "ShipTo")
   public static class ShipTo extends AbstractPaypal {
      @PaypalFieldMeta("shipToName")
      private String m_name;

      @PaypalFieldMeta("shipToStreet")
      private String m_street;

      @PaypalFieldMeta("shipToCity")
      private String m_city;

      @PaypalFieldMeta("shipToState")
      private String m_state;

      @PaypalFieldMeta("shipToCountryCode")
      private String m_countryCode;

      @PaypalFieldMeta("shipToCountryName")
      private String m_countryName;

      @PaypalFieldMeta("shipToZip")
      private String m_postalCodes;

      public String getCity() {
         return m_city;
      }

      public String getCountryCode() {
         return m_countryCode;
      }

      public String getCountryName() {
         return m_countryName;
      }

      public String getName() {
         return m_name;
      }

      public String getPostalCodes() {
         return m_postalCodes;
      }

      public String getState() {
         return m_state;
      }

      public String getStreet() {
         return m_street;
      }
   }
}
