package org.unidal.ezsell.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.common.RowSet;
import org.unidal.ezsell.dal.Payment;

public class PayPalPaymentLoader {
   private static final String EMPTY = "";

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public List<Payment> getPayments(BufferedReader reader) throws IOException {
      List<List<String>> rows = load(reader);
      List<Payment> payments = new ArrayList<Payment>();

      if (rows.size() > 1) {
         RowSet result = new RowSet(rows);

         for (; result.hasNext(); result.next()) {
            Payment payment = null;

            if (result.getString("交易号") != null) {
               payment = getPaymentCn(result);
            } else if (result.getString("Transaction ID") != null) {
               payment = getPaymentEn(result);
            }

            if (payment != null && payment.getPaymentId() != null && payment.getPaymentId().trim().length() > 0) {
               payments.add(payment);
            }
         }
      }

      return payments;
   }

   private Payment getPaymentCn(RowSet result) {
      Payment payment = new Payment();

      payment.setPaymentId(result.getString("交易号"));
      payment.setFeeAmount(result.getDouble("费用", 0));
      payment.setAmount(result.getDouble("总额", 0));
      payment.setBuyerAccount(result.getString("买家号"));
      payment.setBuyerName(result.getString("姓名"));

      try {
         payment.setOrderTime(m_format.parse(result.getString("日期") + " " + result.getString("时间")));
      } catch (ParseException e) {
         // ignore it
      }
      return payment;
   }

   private Payment getPaymentEn(RowSet result) {
      Payment payment = new Payment();

      payment.setPaymentId(result.getString("Transaction ID"));
      payment.setFeeAmount(result.getDouble("Fee", 0));
      payment.setAmount(result.getDouble("Gross", 0));
      payment.setBuyerAccount(result.getString("Buyer ID"));
      payment.setBuyerName(result.getString("Name"));

      try {
         payment.setOrderTime(m_format.parse(result.getString("Date") + " " + result.getString("Time")));
      } catch (ParseException e) {
         // ignore it
      }

      return payment;
   }

   List<List<String>> load(BufferedReader reader) throws IOException {
      List<List<String>> rows = new ArrayList<List<String>>();
      boolean header = true;

      while (true) {
         String line = reader.readLine();

         if (line == null) {
            break;
         }

         List<String> row = parseRow(line, header);

         if (row.size() > 20) {
            rows.add(row);
            header = false;
         }
      }

      return rows;
   }

   private List<String> parseRow(String line, boolean header) {
      List<String> cols = new ArrayList<String>(40);
      StringBuilder sb = new StringBuilder(256);
      boolean inQuote = false;
      int len = line.length();

      for (int i = 0; i < len; i++) {
         char ch = line.charAt(i);

         switch (ch) {
         case '"':
            if (!inQuote) {
               if (sb.length() == 0) {
                  inQuote = true;
               } else {
                  sb.append(ch);
               }
            } else {
               if (i + 1 < len && line.charAt(i + 1) == ch) {
                  sb.append(ch);
                  i++;
                  continue;
               }

               if (sb.length() == 0) {
                  cols.add(EMPTY);
               } else {
                  cols.add(sb.toString().trim());
               }

               sb.setLength(0);
               inQuote = false;
            }

            break;
         case '\t':
         case ',':
            if (inQuote) {
               sb.append(ch);
            } else if (header) {
               cols.add(sb.toString().trim());
               sb.setLength(0);
            } else if (sb.length() > 0) {
               cols.add(sb.toString().trim());
               sb.setLength(0);
            }

            break;
         default:
            sb.append(ch);
         }
      }

      return cols;
   }
}
