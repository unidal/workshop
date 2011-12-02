package org.unidal.ezsell.transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpException;
import org.codehaus.plexus.util.IOUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.api.paypal.PaypalApi;
import org.unidal.ezsell.dal.Buyer;
import org.unidal.ezsell.dal.BuyerDao;
import org.unidal.ezsell.dal.BuyerEntity;
import org.unidal.ezsell.dal.Item;
import org.unidal.ezsell.dal.ItemDao;
import org.unidal.ezsell.dal.ItemEntity;
import org.unidal.ezsell.dal.Order;
import org.unidal.ezsell.dal.OrderDao;
import org.unidal.ezsell.dal.OrderEntity;
import org.unidal.ezsell.dal.Payment;
import org.unidal.ezsell.dal.PaymentDao;
import org.unidal.ezsell.dal.PaymentEntity;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.ShippingAddress;
import org.unidal.ezsell.dal.ShippingAddressDao;
import org.unidal.ezsell.dal.ShippingAddressEntity;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;

import com.site.dal.jdbc.DalException;
import com.site.lookup.ComponentTestCase;

public class TransactionLoaderTest extends ComponentTestCase {
   private static final int TEST_SELLER_ID = -1;

   private static long ONE_DAY = 86400L * 1000L;

   private Seller m_seller;

   private SellerDao m_sellerDao;

   private TransactionDao m_trxDao;

   private ItemDao m_itemDao;

   private OrderDao m_orderDao;

   private BuyerDao m_buyerDao;

   private ShippingAddressDao m_shippingAddressDao;

   private PaymentDao m_paymentDao;

   private void assertBuyers(List<Transaction> transactions) {
      Set<String> all = new HashSet<String>();

      for (Transaction trx : transactions) {
         String account = trx.getBuyerAccount();

         if (!all.contains(account)) {
            all.add(account);

            try {
               Buyer buyer = m_buyerDao.findByPK(account, BuyerEntity.READSET_FULL);

               buyer.setKeyBuyerAccount(account);
               m_buyerDao.deleteByPK(buyer);
            } catch (DalException e) {
               e.printStackTrace();
               fail("Buyer(" + account + ") is not found in DB.");
            }
         }
      }
   }

   private void assertItems(List<Transaction> transactions) {
      Set<Long> all = new HashSet<Long>();

      for (Transaction trx : transactions) {
         long itemId = trx.getItemId();

         if (!all.contains(itemId)) {
            all.add(itemId);

            try {
               Item item = m_itemDao.findByPK(itemId, ItemEntity.READSET_FULL);

               item.setKeyItemId(itemId);
               m_itemDao.deleteByPK(item);
            } catch (DalException e) {
               e.printStackTrace();
               fail("Item(" + itemId + ") is not found in DB.");
            }
         }
      }
   }

   private void assertOrders(List<Transaction> transactions) {
      Set<Long> all = new HashSet<Long>();

      for (Transaction trx : transactions) {
         long orderId = trx.getOrderId();

         if (orderId > 0 && !all.contains(orderId)) {
            all.add(orderId);

            try {
               Order order = m_orderDao.findByPK(orderId, OrderEntity.READSET_FULL);

               order.setKeyOrderId(orderId);
               m_orderDao.deleteByPK(order);
            } catch (DalException e) {
               e.printStackTrace();
               fail("Order(" + orderId + ") is not found in DB.");
            }
         }
      }
   }

   private void assertPayments(List<Transaction> transactions) {
      Set<String> all = new HashSet<String>();

      for (Transaction trx : transactions) {
         String paymentId = trx.getPaymentId();

         if (paymentId != null && trx.getPaymentFee() > 0 && !all.contains(paymentId)) {
            all.add(paymentId);

            try {
               Payment payment = m_paymentDao.findByPK(paymentId, PaymentEntity.READSET_FULL);

               payment.setKeyPaymentId(paymentId);
               m_paymentDao.deleteByPK(payment);
            } catch (DalException e) {
               e.printStackTrace();
               fail("Payment(" + paymentId + ") is not found in DB.");
            }
         }
      }
   }

   private void assertShippingAddresses(List<Transaction> transactions) {
      Set<String> all = new HashSet<String>();

      for (Transaction trx : transactions) {
         String paymentId = trx.getPaymentId();

         if (paymentId != null && trx.getPaymentFee() > 0 && !all.contains(paymentId)) {
            all.add(paymentId);

            try {
               ShippingAddress shippingAddress = m_shippingAddressDao.findByPK(paymentId,
                     ShippingAddressEntity.READSET_FULL);

               shippingAddress.setKeyPaymentId(paymentId);
               m_shippingAddressDao.deleteByPK(shippingAddress);
            } catch (DalException e) {
               e.printStackTrace();
               fail("ShippingAddress(" + paymentId + ") is not found in DB.");
            }
         }
      }
   }

   private void assertTransactions(List<Transaction> transactions) {
      assertEquals(21, transactions.size());

      for (Transaction trx : transactions) {
         try {
            Transaction t = m_trxDao.findByItemIdAndTransactionId(trx.getItemId(), trx.getTransactionId(),
                  TransactionEntity.READSET_FULL);

            // set transaction id since it can not be retrived by generated keys
            assertEquals(TEST_SELLER_ID, trx.getSellerId());

            trx.setId(t.getId());
            trx.setKeyId(t.getId());
            m_trxDao.deleteByPK(trx);
         } catch (DalException e) {
            e.printStackTrace();
            fail("Transaction(" + trx.getItemId() + "," + trx.getTransactionId() + ") is not found in DB.");
         }
      }
   }

   private Seller createTestSeller() {
      Seller seller = new Seller();

      seller.setKeySellerId(TEST_SELLER_ID);
      seller.setSellerId(TEST_SELLER_ID);
      seller.setOperatorId(-1);
      seller.setEbayAccount("test");
      seller.setEbayAppId("");
      seller.setEbayAuthToken("");
      seller.setPaypalUsername("kewan_wang_api1.hotmail.com");
      seller.setPaypalPassword("F6KQ9KSAY6GQV8PD");
      seller.setPaypalSignature("An5ns1Kso7MWUdW4ErQKJJJ4qi4-AT6mIP1YrtAvMLlpiGnFFpY7RKxG");
      seller.setTransactionLastFetchDate(new Date(System.currentTimeMillis() - ONE_DAY / 2));

      return seller;
   }

   @Before
   public void setUp() throws Exception {
      super.setUp();

      m_sellerDao = lookup(SellerDao.class);
      m_trxDao = lookup(TransactionDao.class);
      m_itemDao = lookup(ItemDao.class);
      m_orderDao = lookup(OrderDao.class);
      m_buyerDao = lookup(BuyerDao.class);
      m_shippingAddressDao = lookup(ShippingAddressDao.class);
      m_paymentDao = lookup(PaymentDao.class);

      m_seller = createTestSeller();
      m_sellerDao.deleteByPK(m_seller);
      m_sellerDao.insert(m_seller);
   }

   @After
   public void tearDown() throws Exception {
      m_sellerDao.deleteByPK(m_seller);
      super.tearDown();
   }

   @Test
   public void testLoadTransactions() throws Exception {
      TransactionLoader loader = lookup(TransactionLoader.class);
      List<Transaction> transactions = loader.loadLatestTransactionsFromApi(m_seller);

      assertTransactions(transactions);
      assertItems(transactions);
      assertOrders(transactions);
      assertBuyers(transactions);
      assertShippingAddresses(transactions);
      assertPayments(transactions);
   }

   public static class PaypalApiMock extends PaypalApi {
      private File m_resource = new File("src/test/resources/" + getClass().getName().replace('.', '/') + ".xml");

      private Map<String, String> m_map = new HashMap<String, String>();

      public PaypalApiMock() {
         loadResource();
      }

      @Override
      public String getTransactionDetails(String paymentId) throws HttpException, IOException {
         String content = m_map.get(paymentId);

         if (content == null) {
            content = super.getTransactionDetails(paymentId);

            m_map.put(paymentId, content);
            saveResource();
         }

         return content;
      }

      private void loadResource() {
         if (m_resource.exists()) {
            try {
               BufferedReader reader = new BufferedReader(new FileReader(m_resource));

               while (true) {
                  String line = reader.readLine();

                  if (line == null) {
                     break;
                  }

                  int pos = line.indexOf(':');

                  if (pos > 0) {
                     String name = line.substring(0, pos);
                     String content = line.substring(pos + 1);

                     m_map.put(name, content);
                  }
               }

               reader.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }

      private void saveResource() throws IOException {
         FileWriter writer = new FileWriter(m_resource);

         for (Map.Entry<String, String> e : m_map.entrySet()) {
            writer.write(e.getKey() + ":" + e.getValue() + "\r\n");
         }

         writer.close();
      }
   }

   public static class TradingMock extends Trading {
      @Override
      public String getSellerTransactions(Date fromDate, Date toDate, int pageNumber, int entriesPerPage)
            throws HttpException, IOException {
         String content = IOUtil.toString(getClass().getResourceAsStream("GetSellerTransactions.xml"));

         assertEquals("xml data changed.", 95293, content.length());
         return content;
      }
   }
}
