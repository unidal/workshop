package org.unidal.ezsell.api.ebay.trading.wdbc;

import java.util.Date;

import com.site.wdbc.http.configuration.WdbcFieldMeta;
import com.site.wdbc.http.configuration.WdbcMeta;

@WdbcMeta(name = "GetSellerTransactions", filter = GetSellerTransactionsFilter.class)
public class EbayTransaction extends AbstractWdbc {
   @WdbcFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", value = "TransactionArray.Transaction[*].CreatedDate")
   private Date m_createdDate;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ConvertedAmountPaid")
   private double m_amountPaid;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ConvertedAdjustmentAmount")
   private double m_adjustmentAmount;

   @WdbcFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", value = "TransactionArray.Transaction[*].PaidTime")
   private Date m_paidTime;

   @WdbcFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", value = "TransactionArray.Transaction[*].ShippedTime")
   private Date m_shippedTime;

   @WdbcFieldMeta("TransactionArray.Transaction[*].QuantityPurchased")
   private int m_quantityPurchased;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ContainingOrder.OrderID")
   private long m_orderId;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ContainingOrder.OrderStatus")
   private String m_orderStatus;

   @WdbcFieldMeta("TransactionArray.Transaction[*].TransactionID")
   private long m_transactionId;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ConvertedTransactionPrice")
   private double m_transactionPrice;

   @WdbcFieldMeta("TransactionArray.Transaction[*].TransactionSiteID")
   private String m_transactionSiteId;

   @WdbcFieldMeta("TransactionArray.Transaction[*].BestOfferSale")
   private boolean m_bestOfferSale;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ShippingServiceSelected.ShippingServiceCost")
   private double m_shippingCost;

   @WdbcFieldMeta("TransactionArray.Transaction[*].FinalValueFee")
   private double m_finalValueFee;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ExternalTransaction.ExternalTransactionID")
   private String m_paymentId;
   
   @WdbcFieldMeta("TransactionArray.Transaction[*].ExternalTransaction.FeeOrCreditAmount")
   private double m_paymentFee;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Item.ItemID")
   private long m_itemId;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Item.Title")
   private String m_itemTitle;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Item.ListingDetails.RelistedItemID")
   private long m_relistedItemId;

   @WdbcFieldMeta("TransactionArray.Transaction[*].ShippingDetails.ShipmentTrackingNumber")
   private String m_shipmentTrackingNumber;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.UserID")
   private String m_buyerAccount;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.Email")
   private String m_buyerEmail;

   @WdbcFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", value = "TransactionArray.Transaction[*].Buyer.RegistrationDate")
   private Date m_buyerRegistrationDate;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.CityName")
   private String m_cityName;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.CountryName")
   private String m_countryName;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.Name")
   private String m_buyerName;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.Phone")
   private String m_phone;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.PostalCode")
   private String m_postalCode;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.StateOrProvince")
   private String m_stateOrProvince;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.Street1")
   private String m_street;

   @WdbcFieldMeta("TransactionArray.Transaction[*].Buyer.BuyerInfo.ShippingAddress.Street2")
   private String m_street2;

   @WdbcFieldMeta("TransactionArray.Transaction[*].BuyerCheckoutMessage")
   private String m_buyerCheckoutMessage;

   public double getAdjustmentAmount() {
      return m_adjustmentAmount;
   }

   public double getAmountPaid() {
      return m_amountPaid;
   }

   public String getBuyerAccount() {
      return m_buyerAccount;
   }

   public String getBuyerCheckoutMessage() {
      return m_buyerCheckoutMessage;
   }

   public String getBuyerEmail() {
      return m_buyerEmail;
   }

   public String getBuyerName() {
      return m_buyerName;
   }

   public Date getBuyerRegistrationDate() {
      return m_buyerRegistrationDate;
   }

   public String getCityName() {
      return m_cityName;
   }

   public String getCountryName() {
      return m_countryName;
   }

   public Date getCreatedDate() {
      return m_createdDate;
   }

   public double getFinalValueFee() {
      return m_finalValueFee;
   }

   public long getItemId() {
      return m_itemId;
   }

   public String getItemTitle() {
      return m_itemTitle;
   }

   public long getOrderId() {
      return m_orderId;
   }

   public String getOrderStatus() {
      return m_orderStatus;
   }

   public Date getPaidTime() {
      return m_paidTime;
   }

   public double getPaymentFee() {
      return m_paymentFee;
   }

   public String getPaymentId() {
      return m_paymentId;
   }

   public String getPhone() {
      return m_phone;
   }

   public String getPostalCode() {
      return m_postalCode;
   }

   public int getQuantityPurchased() {
      return m_quantityPurchased;
   }

   public long getRelistedItemId() {
      return m_relistedItemId;
   }

   public String getShipmentTrackingNumber() {
      return m_shipmentTrackingNumber;
   }

   public Date getShippedTime() {
      return m_shippedTime;
   }

   public double getShippingCost() {
      return m_shippingCost;
   }

   public String getStateOrProvince() {
      return m_stateOrProvince;
   }

   public String getStreet() {
      return m_street;
   }

   public String getStreet2() {
      return m_street2;
   }

   public long getTransactionId() {
      return m_transactionId;
   }

   public double getTransactionPrice() {
      return m_transactionPrice;
   }

   public String getTransactionSiteId() {
      return m_transactionSiteId;
   }

   public boolean isBestOfferSale() {
      return m_bestOfferSale;
   }
}
