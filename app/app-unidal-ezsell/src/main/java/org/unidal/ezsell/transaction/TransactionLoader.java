package org.unidal.ezsell.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.api.ebay.EbayApiManager;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.api.ebay.trading.wdbc.EbayTransaction;
import org.unidal.ezsell.api.ebay.trading.wdbc.WdbcMapping;
import org.unidal.ezsell.api.paypal.PaypalApi;
import org.unidal.ezsell.api.paypal.PaypalApiManager;
import org.unidal.ezsell.api.paypal.PaypalMapping;
import org.unidal.ezsell.api.paypal.PaypalTransaction;
import org.unidal.ezsell.api.paypal.PaypalTransaction.ShipTo;
import org.unidal.ezsell.dal.Buyer;
import org.unidal.ezsell.dal.BuyerDao;
import org.unidal.ezsell.dal.Item;
import org.unidal.ezsell.dal.ItemDao;
import org.unidal.ezsell.dal.Order;
import org.unidal.ezsell.dal.OrderDao;
import org.unidal.ezsell.dal.Payment;
import org.unidal.ezsell.dal.PaymentDao;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEntity;
import org.unidal.ezsell.dal.ShippingAddress;
import org.unidal.ezsell.dal.ShippingAddressDao;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;

import com.site.app.tag.function.Format;
import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcSourceType;

public class TransactionLoader implements LogEnabled {
   private static final int MAX_DAYS_TO_FETCH = 120;

   private static long ONE_DAY = 86400L * 1000L;

   @Inject
   private EbayApiManager m_ebayApiManager;

   @Inject
   private WdbcMapping m_wdbcMapping;

   @Inject
   private PaypalApiManager m_paypalApiManager;

   @Inject
   private PaypalMapping m_paypalMapping;

   @Inject
   private SellerDao m_sellerDao;

   @Inject
   private TransactionDao m_trxDao;

   @Inject
   private ItemDao m_itemDao;

   @Inject
   private OrderDao m_orderDao;

   @Inject
   private BuyerDao m_buyerDao;

   @Inject
   private ShippingAddressDao m_shippingAddressDao;

   @Inject
   private PaymentDao m_paymentDao;
   
   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private Item[] getItems(List<EbayTransaction> list) {
      int len = list.size();
      Item[] items = new Item[len];

      for (int i = 0; i < len; i++) {
         EbayTransaction trx = list.get(i);
         Item item = new Item();

         item.setItemId(trx.getItemId());
         item.setItemTitle(trx.getItemTitle());
         item.setOriginalItemId(trx.getRelistedItemId());

         items[i] = item;
      }

      return items;
   }

   private Order[] getOrders(List<EbayTransaction> list) {
      int len = list.size();
      List<Order> orders = new ArrayList<Order>(len);
      Set<Long> all = new HashSet<Long>();

      for (int i = 0; i < len; i++) {
         EbayTransaction trx = list.get(i);
         long orderId = trx.getOrderId();

         if (orderId > 0 && !all.contains(orderId)) {
            Order order = new Order();

            order.setOrderId(trx.getOrderId());
            order.setAmountPaid(trx.getAmountPaid());
            order.setAdjustmentAmount(trx.getAdjustmentAmount());
            order.setOrderStatus(trx.getOrderStatus());

            orders.add(order);
            all.add(orderId);
         }
      }

      return orders.toArray(new Order[0]);
   }

   private Buyer[] getBuyers(List<EbayTransaction> list) {
      int len = list.size();
      List<Buyer> buyers = new ArrayList<Buyer>(len);
      Set<String> all = new HashSet<String>();

      for (int i = 0; i < len; i++) {
         EbayTransaction trx = list.get(i);
         String account = trx.getBuyerAccount();

         if (!all.contains(account)) {
            Buyer buyer = new Buyer();

            buyer.setBuyerAccount(account);
            buyer.setBuyerName(trx.getBuyerName());
            buyer.setBuyerEmail(trx.getBuyerEmail());
            buyer.setRegistrationDate(trx.getBuyerRegistrationDate());

            buyers.add(buyer);
            all.add(account);
         }
      }

      return buyers.toArray(new Buyer[0]);
   }

   public List<Transaction> loadLatestTransactionsFromApi(Seller seller) throws HttpException, IOException,
         WdbcException, DalException {
      long lastFetchDate = seller.getTransactionLastFetchDate() == null ? 0 : seller.getTransactionLastFetchDate()
            .getTime();
      long now = System.currentTimeMillis();

      if (lastFetchDate == 0) {
         Calendar cal = Calendar.getInstance();

         cal.setTimeInMillis(now);
         cal.add(Calendar.DATE, -MAX_DAYS_TO_FETCH); // back 120 days
         lastFetchDate = cal.getTimeInMillis();
      }

      seller.setKeySellerId(seller.getSellerId());

      List<Transaction> loaded = new ArrayList<Transaction>();
      int days = (int) ((now - lastFetchDate) / ONE_DAY);

      for (int i = 0; i < days + 1; i++) {
         Date startDate = new Date(lastFetchDate);
         Date endDate = new Date(Math.min(now, lastFetchDate + ONE_DAY));

         m_logger.info("Fetch seller transactions from " + Format.format(startDate, "yyyy-MM-dd HH:mm:ss") + " to "
               + Format.format(endDate, "yyyy-MM-dd HH:mm:ss"));

         Trading trading = m_ebayApiManager.getTrading(seller);
         int pageNumber = 1;
         int entriesPerPage = 25;

         while (true) {
            String content = trading.getSellerTransactions(startDate, endDate, pageNumber, entriesPerPage);
            StringSource source = new StringSource(WdbcSourceType.XML, content);
            List<EbayTransaction> list = m_wdbcMapping.apply(EbayTransaction.class, source);

            if (!list.isEmpty()) {
               m_trxDao.insert(getTransactions(seller.getSellerId(), list, loaded));
               m_itemDao.insert(getItems(list));
               m_orderDao.insert(getOrders(list));
               m_buyerDao.insert(getBuyers(list));
            }

            m_logger.info(list.size() + " rows retrieved.");

            if (list.size() < 25) {
               break;
            }

            pageNumber++;
         }

         lastFetchDate = Math.min(now, lastFetchDate + ONE_DAY);
         seller.setTransactionLastFetchDate(new Date(lastFetchDate));
         m_sellerDao.updateByPK(seller, SellerEntity.UPDATESET_FULL);
      }

      m_logger.info(loaded.size() + " seller transactions loaded.");

      List<PaypalTransaction> payments = loadPaypalTransactions(seller, loaded);

      m_shippingAddressDao.insert(getShippingAddresses(payments));
      m_paymentDao.insert(getPayments(seller, payments));
      m_logger.info(payments.size() + " payments loaded.");

      
      return loaded;
   }

   private ShippingAddress[] getShippingAddresses(List<PaypalTransaction> transactions) throws DalException {
      Set<String> all = new HashSet<String>();
      List<ShippingAddress> list = new ArrayList<ShippingAddress>(transactions.size());

      for (PaypalTransaction trx : transactions) {
         String paymentId = trx.getPaymentId();

         if (paymentId != null && trx.getFeeAmount() > 0 && !all.contains(paymentId)) {
            ShipTo s = trx.getShipTo();
            ShippingAddress sa = new ShippingAddress();

            sa.setPaymentId(trx.getPaymentId());
            sa.setBuyerAccount(trx.getBuyerAccount());
            sa.setName(s.getName());
            sa.setStreet(s.getStreet());
            sa.setCity(s.getCity());
            sa.setState(s.getState());
            sa.setCountryCode(s.getCountryCode());
            sa.setCountryName(s.getCountryName());
            sa.setPostalCode(s.getPostalCodes());

            if (trx.getBuyerAccount() != null) {
               list.add(sa);
            }

            all.add(paymentId);
         }
      }

      return list.toArray(new ShippingAddress[0]);
   }

   private Payment[] getPayments(Seller seller, List<PaypalTransaction> transactions) throws DalException {
      Set<String> all = new HashSet<String>();
      List<Payment> list = new ArrayList<Payment>(transactions.size());

      for (PaypalTransaction trx : transactions) {
         String paymentId = trx.getPaymentId();

         if (paymentId != null && trx.getFeeAmount() > 0 && !all.contains(paymentId)) {
            Payment p = new Payment();

            p.setSellerId(seller.getSellerId());
            p.setAmount(trx.getAmount());
            p.setBuyerAccount(trx.getBuyerAccount());
            p.setBuyerEmail(trx.getBuyerEmail());
            p.setBuyerName(trx.getShipTo().getName());
            p.setCurrencyCode(trx.getCurrencyCode());
            p.setExchangeRate(trx.getExchangeRate());
            p.setFeeAmount(trx.getFeeAmount());
            p.setNotes(trx.getNotes());
            p.setOrderId(trx.getOrderId());
            p.setOrderTime(trx.getOrderTime());
            p.setPaymentId(trx.getPaymentId());
            p.setPaymentType(trx.getPaymentType());
            p.setPaymentStatus(trx.getPaymentStatus());
            p.setParentPaymentId(trx.getParentPaymentId());
            p.setPendingReason(trx.getPendingReason());
            p.setReasonCode(trx.getReasonCode());
            p.setReceiptId(trx.getReceiptId());
            p.setSettleAmount(trx.getSettleAmount());
            p.setTaxAmount(trx.getTaxAmount());

            if (trx.getBuyerAccount() != null) {
               list.add(p);
            }

            all.add(paymentId);
         }
      }

      return list.toArray(new Payment[0]);
   }

   private List<PaypalTransaction> loadPaypalTransactions(Seller seller, List<Transaction> transactions)
         throws HttpException, IOException {
      Set<String> all = new HashSet<String>();
      List<PaypalTransaction> list = new ArrayList<PaypalTransaction>(transactions.size());
      PaypalApi paypalApi = m_paypalApiManager.getPaypalApi(seller);

      for (Transaction trx : transactions) {
         String paymentId = trx.getPaymentId();

         if (paymentId != null && trx.getPaymentFee() > 0 && !all.contains(paymentId)) {
            String content = paypalApi.getTransactionDetails(paymentId);
            PaypalTransaction payment = m_paypalMapping.apply(PaypalTransaction.class, content);

            list.add(payment);
            all.add(paymentId);
         }
      }

      return list;
   }

   private Transaction[] getTransactions(int sellerId, List<EbayTransaction> list, List<Transaction> loaded) {
      int len = list.size();
      Transaction[] transactions = new Transaction[len];

      for (int i = 0; i < len; i++) {
         EbayTransaction t = list.get(i);
         Transaction trx = new Transaction();

         trx.setSellerId(sellerId);
         trx.setTransactionCreationDate(t.getCreatedDate());
         trx.setAmountPaid(t.getAmountPaid());
         trx.setAdjustmentAmount(t.getAdjustmentAmount());
         trx.setShippingCost(t.getShippingCost());
         trx.setFinalValueFee(t.getFinalValueFee());
         trx.setPaidTime(t.getPaidTime());
         trx.setShippedTime(t.getShippedTime());
         trx.setQuantityPurchased(t.getQuantityPurchased());
         trx.setOrderId(t.getOrderId());
         trx.setTransactionId(t.getTransactionId());
         trx.setTransactionPrice(t.getTransactionPrice());
         trx.setTransactionSiteId(t.getTransactionSiteId());
         trx.setPaymentId(t.getPaymentId());
         trx.setPaymentFee(t.getPaymentFee());
         trx.setItemId(t.getItemId());
         trx.setItemTitle(t.getItemTitle());
         trx.setShippingTrackingId(t.getShipmentTrackingNumber());
         trx.setBuyerAccount(t.getBuyerAccount());
         trx.setBuyerName(t.getBuyerAccount());
         trx.setCheckoutMessage(t.getBuyerCheckoutMessage());
         trx.setStatus(getTransactionStatus(trx).getValue());

         transactions[i] = trx;
         loaded.add(trx);
      }

      return transactions;
   }

   private TransactionStatus getTransactionStatus(Transaction trx) {
      Date paidTime = trx.getPaidTime();
      Date shippedTime = trx.getShippedTime();
      double amountPaid = trx.getAmountPaid();
      TransactionStatus status = null;

      if (paidTime == null && shippedTime == null && amountPaid > 0) {
         status = TransactionStatus.NOT_PAID;
      } else if (paidTime == null && (shippedTime != null || amountPaid < 1e-6)) {
         status = TransactionStatus.REFUNDED;
      } else if (paidTime != null && shippedTime != null && amountPaid > 0) {
         status = TransactionStatus.SHIPPED;
      } else if (paidTime != null && shippedTime == null && amountPaid > 0) {
         status = TransactionStatus.PAID;
      }

      return status;
   }
}
