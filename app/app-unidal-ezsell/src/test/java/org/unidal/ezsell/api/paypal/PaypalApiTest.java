package org.unidal.ezsell.api.paypal;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.unidal.ezsell.api.paypal.PaypalTransaction.Item;
import org.unidal.ezsell.api.paypal.PaypalTransactionSearch.Transaction;

import com.site.lookup.ComponentTestCase;

public class PaypalApiTest extends ComponentTestCase {
   private PaypalApi m_transaction;

   @Before
   public void setUp() throws Exception {
      super.setUp();

      m_transaction = lookup(PaypalApi.class);

      m_transaction.setApiUsername("kewan_wang_api1.hotmail.com");
      m_transaction.setApiPassword("F6KQ9KSAY6GQV8PD");
      m_transaction.setApiSignature("An5ns1Kso7MWUdW4ErQKJJJ4qi4-AT6mIP1YrtAvMLlpiGnFFpY7RKxG");
   }

   @Test
   public void notestTransactionSearch() throws Exception {
      long ONE_DAY = 86400000L;
      String content = m_transaction.transactionSearch(new Date(System.currentTimeMillis() - 10 * ONE_DAY), null, null);

      System.out.println(content);

      PaypalMapping mapping = lookup(PaypalMapping.class);
      PaypalTransactionSearch trx = mapping.apply(PaypalTransactionSearch.class, content);

      System.out.println(trx);
      
      for (Transaction t: trx.getTransactions()) {
         System.out.println(t);
      }
   }

   @Test
   public void testGetTransactionDetails() throws Exception {
      String content = m_transaction.getTransactionDetails("3AL033405F367343C");

      System.out.println(content);
      
      PaypalMapping mapping = lookup(PaypalMapping.class);
      PaypalTransaction trx = mapping.apply(PaypalTransaction.class, content);
      
      System.out.println(trx);

      for (Item item: trx.getItems()) {
         System.out.println(item);
      }
   }
}
