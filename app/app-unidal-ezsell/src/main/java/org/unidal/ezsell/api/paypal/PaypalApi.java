package org.unidal.ezsell.api.paypal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

public class PaypalApi extends AbstractPaypalApiRequest {
   private static String VERSION = "59.0";

   public PaypalApi() {
      setEndPoint("https://api-3t.paypal.com/nvp");
   }

   public String transactionSearch(Date fromDate, Date toDate, String transactionId) throws HttpException, IOException {
      JSONObject payload = new JSONObject();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

      format.setTimeZone(TimeZone.getTimeZone("GMT"));

      try {
         payload.put("StartDate", format.format(fromDate));

         if (toDate != null) {
            payload.put("EndData", format.format(toDate));
         }

         if (transactionId != null) {
            payload.put("TransactionId", transactionId);
         }
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for TransactionSearch(" + fromDate + "," + toDate
               + "," + transactionId + ").");
      }

      return doCallForNvp(new RouteInfo("TransactionSearch", VERSION), payload);
   }

   public String getTransactionDetails(String paymentId) throws HttpException, IOException {
      JSONObject payload = new JSONObject();

      try {
         payload.put("TransactionId", paymentId);
      } catch (JSONException e) {
         throw new RuntimeException("Error when constructing payload for GetTransactionDetails (" + paymentId + ").");
      }

      return doCallForNvp(new RouteInfo("GetTransactionDetails", VERSION), payload);
   }
}
