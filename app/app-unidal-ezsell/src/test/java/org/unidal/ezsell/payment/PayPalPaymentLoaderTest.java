package org.unidal.ezsell.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.ezsell.dal.Payment;

public class PayPalPaymentLoaderTest {
   @Test
   public void testCsvPaymentEn() throws IOException {
      InputStream in = getClass().getResourceAsStream("payment_en.csv");
      Reader reader = new InputStreamReader(in, "GBK");
      PayPalPaymentLoader loader = new PayPalPaymentLoader();
      List<List<String>> rows = loader.load(new BufferedReader(reader));

      Assert.assertEquals(11, rows.size());

      boolean header = true;

      for (List<String> row : rows) {
         if (!header) {
            Assert.assertEquals("CTT", row.get(2));
         } else {
            header = false;
         }
      }
   }

   @Test
   public void testCsvPaymentCn() throws IOException {
      InputStream in = getClass().getResourceAsStream("payment_cn.csv");
      Reader reader = new InputStreamReader(in, "GBK");
      PayPalPaymentLoader loader = new PayPalPaymentLoader();
      List<List<String>> rows = loader.load(new BufferedReader(reader));

      Assert.assertEquals(11, rows.size());

      boolean header = true;

      for (List<String> row : rows) {
         if (!header) {
            Assert.assertEquals("CTT", row.get(2));
         } else {
            header = false;
         }
      }
   }

   @Test
   public void testGetPayments() throws IOException {
      InputStream in = getClass().getResourceAsStream("payment.txt");
      Reader reader = new InputStreamReader(in, "GBK");
      PayPalPaymentLoader loader = new PayPalPaymentLoader();
      List<Payment> payments = loader.getPayments(new BufferedReader(reader));

      Assert.assertEquals(10, payments.size());

      for (Payment payment : payments) {
         Assert.assertNotNull(payment.getTransactionId());
         Assert.assertTrue(payment.getAmount() != 0);
         Assert.assertTrue(payment.getFeeAmount() != 0);
      }
   }

   @Test
   public void testTabbedPayment() throws IOException {
      InputStream in = getClass().getResourceAsStream("payment.txt");
      Reader reader = new InputStreamReader(in, "GBK");
      PayPalPaymentLoader loader = new PayPalPaymentLoader();
      List<List<String>> rows = loader.load(new BufferedReader(reader));

      Assert.assertEquals(11, rows.size());

      boolean header = true;

      for (List<String> row : rows) {
         if (!header) {
            Assert.assertEquals("CTT", row.get(2));
         } else {
            header = false;
         }
      }
   }
}
