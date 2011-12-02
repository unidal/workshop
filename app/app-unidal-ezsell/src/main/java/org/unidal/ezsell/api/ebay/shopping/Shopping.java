package org.unidal.ezsell.api.ebay.shopping;

import java.io.IOException;

import org.apache.http.HttpException;
import org.json.JSONObject;
import org.unidal.ezsell.api.ebay.AbstractEbayApiRequest;

public class Shopping extends AbstractEbayApiRequest {
   public Shopping() {
      setGatewayUri("http://open.api.ebay.com/shopping");
   }

   public JSONObject getSingleItem(long itemId) throws HttpException, IOException {
      return doCallForJson(new RouteInfo("GetSingleItem", 0, 627), "ItemId=" + itemId);
   }
}
