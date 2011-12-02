package com.site.app.tag.function;

import junit.framework.Assert;

import org.junit.Test;

public class EncoderTest {
   @Test
   public void testUrlEncode() {
      String url = "http://localhost:2000/mvc/ebay?action=seller_transactions&keyword=&dateFrom=2009-08-06&dateTo=2009-08-06&status=paid&search=Search";
      String encodedUrl = Encoder.urlEncode(url);

      Assert
            .assertEquals(
                  "http://localhost:2000/mvc/ebay%3faction%3dseller_transactions%26keyword%3d%26dateFrom%3d2009-08-06%26dateTo%3d2009-08-06%26status%3dpaid%26search%3dSearch",
                  encodedUrl);
   }
}
