package org.unidal.ezsell.shipping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.ezsell.dal.Shipping;
import org.unidal.ezsell.shipping.ShippingSummaryLoader;


public class ShippingSummaryLoaderTest {
   @Test
   public void testParseCsvFile() throws IOException {
      InputStream in = getClass().getResourceAsStream("shipping_summary.csv");
      Reader reader = new InputStreamReader(in, "utf-8");
      ShippingSummaryLoader loader = new ShippingSummaryLoader();
      List<List<String>> rows = loader.load(new BufferedReader(reader));

      Assert.assertEquals(157, rows.size());
   }

   @Test
   public void testGetShippings() throws IOException {
      InputStream in = getClass().getResourceAsStream("shipping_summary.csv");
      Reader reader = new InputStreamReader(in, "utf-8");
      ShippingSummaryLoader loader = new ShippingSummaryLoader();
      List<Shipping> shippings = loader.getShippings(new BufferedReader(reader), 0);

      Assert.assertEquals(156, shippings.size());

      for (Shipping shipping : shippings) {
         Assert.assertNotNull(shipping.getShippingTrackingId());
         Assert.assertTrue(shipping.getPriceBeforeDiscount() > 0);
         Assert.assertTrue(shipping.getWeight() > 0);
         Assert.assertTrue(shipping.getDiscountRate() > 0);
         Assert.assertTrue(shipping.getCustomsFee() > 0);
         Assert.assertTrue(shipping.getPriceAfterDiscount() > 0);
      }
   }
}
