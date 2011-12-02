package org.unidal.ezsell.shipping;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.common.RowSet;
import org.unidal.ezsell.dal.Shipping;

public class ShippingSummaryLoader {
   public List<Shipping> getShippings(BufferedReader reader, int sellerId) throws IOException {
      List<List<String>> rows = load(reader);
      List<Shipping> shippings = new ArrayList<Shipping>();

      if (rows.size() > 1) {
         RowSet result = new RowSet(rows);
         SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");

         for (; result.hasNext(); result.next()) {
            Shipping shipping = new Shipping();

            shipping.setSellerId(sellerId);
            shipping.setShippingTrackingId(result.getString("单号"));
            shipping.setCountry(result.getString("目的地"));
            shipping.setWeight((int) (result.getDouble("重量KG", 0) * 1000d));
            shipping.setPriceBeforeDiscount(result.getDouble("折前价", 0));
            shipping.setDiscountRate(result.getDouble("折扣", 0));
            shipping.setCustomsFee(result.getDouble("关费", 0));
            shipping.setPriceAfterDiscount(result.getDouble("折后价", 0));

            try {
               shipping.setShippingDate(format.parse(result.getString("Date")));
            } catch (Exception e) {
               // ignore it
            }

            if (shipping.getShippingTrackingId() != null) {
               shippings.add(shipping);
            }
         }
      }

      return shippings;
   }

   List<List<String>> load(BufferedReader reader) throws IOException {
      List<List<String>> rows = new ArrayList<List<String>>();

      while (true) {
         String line = reader.readLine();

         if (line == null) {
            break;
         }

         List<String> row = parseRow(line);

         if (row.size() >= 10) {
            rows.add(row);
         }
      }

      return rows;
   }

   private List<String> parseRow(String line) {
      List<String> cols = new ArrayList<String>(40);
      StringBuilder sb = new StringBuilder(256);
      int len = line.length();

      for (int i = 0; i < len; i++) {
         char ch = line.charAt(i);

         switch (ch) {
         case '\t':
         case ',':
            cols.add(sb.toString().trim());
            sb.setLength(0);
            break;
         default:
            sb.append(ch);
         }
      }

      return cols;
   }
}
