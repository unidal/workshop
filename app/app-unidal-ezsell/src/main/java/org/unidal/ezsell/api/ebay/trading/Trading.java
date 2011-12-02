package org.unidal.ezsell.api.ebay.trading;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import org.unidal.ezsell.api.ebay.AbstractEbayApiRequest;
import org.unidal.ezsell.feedback.FeedbackType;

public class Trading extends AbstractEbayApiRequest {
   private static int VERSION = 635;

   public Trading() {
      setGatewayUri("https://api.ebay.com/ws/api.dll");
   }

   public String getFeedbacks(int pageNumber) throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");

         JSONObject pagination = newJSONObject(payload, "Pagination");

         pagination.put("PageNumber ", String.valueOf(pageNumber));
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for getFeedback().");
      }

      return doCallForXml(new RouteInfo("GetFeedback", 0, VERSION), payload);
   }

   public String getFeedbacks(long itemId, long transactionId) throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");
         payload.put("ItemID", String.valueOf(itemId));
         payload.put("TransactionID", String.valueOf(transactionId));
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for getFeedback().");
      }

      return doCallForXml(new RouteInfo("GetFeedback", 0, VERSION), payload);
   }

   public String markAsShipped(long itemId, long transactionId, String shipmentTrackingNumber,
         String shippingCarrierUsed) throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");
         payload.put("ItemID", String.valueOf(itemId));
         payload.put("TransactionID", String.valueOf(transactionId));
         payload.put("Shipped", "true");

         if (shipmentTrackingNumber != null && shippingCarrierUsed != null) {
            JSONObject shipment = newJSONObject(payload, "Shipment");
            JSONObject shipmentTrackingDetails = newJSONObject(shipment, "ShipmentTrackingDetails");

            shipmentTrackingDetails.put("ShipmentTrackingNumber", shipmentTrackingNumber);
            shipmentTrackingDetails.put("ShippingCarrierUsed", shippingCarrierUsed);
         }
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for CompleteSale().");
      }

      return doCallForXml(new RouteInfo("CompleteSale", 0, VERSION), payload);
   }

   public String markAsPaid(long itemId, long transactionId) throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");
         payload.put("ItemID", String.valueOf(itemId));
         payload.put("TransactionID", String.valueOf(transactionId));
         payload.put("Paid", "true");
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for CompleteSale().");
      }

      return doCallForXml(new RouteInfo("CompleteSale", 0, VERSION), payload);
   }

   public String leaveFeedback(long itemId, long transactionId, FeedbackType type, String commentText, String targetUser)
         throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");
         payload.put("ItemID", String.valueOf(itemId));
         payload.put("TransactionID", String.valueOf(transactionId));
         payload.put("CommentType", type.getName());
         payload.put("CommentText", commentText);
         payload.put("TargetUser", targetUser);
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for LeaveFeedback().");
      }

      return doCallForXml(new RouteInfo("LeaveFeedback", 0, VERSION), payload);
   }

   public String getSellerTransactions(Date fromDate, Date toDate, int pageNumber, int entriesPerPage) throws HttpException, IOException {
      JSONObject payload = new JSONObject();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

      format.setTimeZone(TimeZone.getTimeZone("GMT"));

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");
         payload.put("IncludeContainingOrder", "true");
         payload.put("IncludeFinalValueFee", "true");
         payload.put("ModTimeFrom", format.format(fromDate));
         payload.put("ModTimeTo", format.format(toDate));

         JSONObject pagination = newJSONObject(payload, "Pagination");

         pagination.put("PageNumber ", String.valueOf(pageNumber));
         pagination.put("EntriesPerPage", String.valueOf(entriesPerPage));
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for GetSellerTransactions(" + fromDate + ","
               + toDate + ").");
      }

      return doCallForXml(new RouteInfo("GetSellerTransactions", 0, VERSION), payload);
   }

   public String getSellerTransactions(int days) throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("Platform", "eBay");
         payload.put("DetailLevel", "ReturnAll");
         payload.put("IncludeContainingOrder", "true");
         payload.put("IncludeFinalValueFee", "true");
         payload.put("NumberOfDays", days);
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for GetSellerTransactions(" + days + ").");
      }

      return doCallForXml(new RouteInfo("GetSellerTransactions", 0, VERSION), payload);
   }
}
