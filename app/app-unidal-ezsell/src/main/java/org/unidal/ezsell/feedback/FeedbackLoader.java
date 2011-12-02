package org.unidal.ezsell.feedback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.api.ebay.EbayApiManager;
import org.unidal.ezsell.api.ebay.trading.GetFeedback;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.dal.Feedback;
import org.unidal.ezsell.dal.FeedbackDao;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEntity;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.wdbc.WdbcException;

public class FeedbackLoader implements LogEnabled {
   private static final int MAX_DAYS_TO_FETCH = 120;

   @Inject
   private EbayApiManager m_apiManager;

   @Inject
   private GetFeedback m_getFeedback;

   @Inject
   private FeedbackDao m_feedbackDao;

   @Inject
   private TransactionDao m_trxDao;

   @Inject
   private SellerDao m_sellerDao;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private List<Feedback> filterByDate(List<Feedback> feedbacks, long lastFetchDate) {
      List<Feedback> filtered = new ArrayList<Feedback>(feedbacks.size());

      for (Feedback feedback : feedbacks) {
         if (feedback.getCommentDate().getTime() > lastFetchDate) {
            filtered.add(feedback);
         }
      }

      return filtered;
   }

   private int getFeedbackStatus(int feedbackStatus, int type, int role) {
      FeedbackType feedbackType = FeedbackType.getByValue(type, null);
      FeedbackRole feedbackRole = FeedbackRole.getByValue(role, null);
      int status = feedbackStatus;

      if (feedbackType != null && feedbackRole != null) {
         switch (feedbackRole) {
         case TO_BUYER:
            status = feedbackType.getValue() * 10 + (status % 10);
            break;
         case TO_SELLER:
            status = (status / 10) * 10 + feedbackType.getValue();
            break;
         }
      }

      return status;
   }

   public boolean loadFeedbackFromApi(Seller seller, Transaction trx) throws HttpException, IOException, WdbcException,
         DalException {
      long itemId = trx.getItemId();
      long transactionId = trx.getTransactionId();

      m_logger.info("Fetch feedback for transaction(" + itemId + "," + transactionId + ")...");

      Trading trading = m_apiManager.getTrading(seller);
      String content = trading.getFeedbacks(itemId, transactionId);
      List<Feedback> feedbacks = m_getFeedback.getFeedbacks(seller.getSellerId(), content);

      if (feedbacks.size() > 0) {
         m_feedbackDao.insert(feedbacks.toArray(new Feedback[0]));

         try {
            for (Feedback feedback : feedbacks) {
               prepareTransaction(trx, feedback);
            }

            m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
            m_logger.info("Transaction updated.");
            
            return true;
         } catch (Exception e) {
            m_logger.warn("Error when updating transaction(" + itemId + "," + transactionId + ")");
         }
      }
      
      return false;
   }

   public List<Transaction> loadLatestFeedbacksFromApi(Seller seller) throws HttpException, IOException, WdbcException,
         DalException {
      long lastFetchDate = seller.getFeedbackLastFetchDate() == null ? 0 : seller.getFeedbackLastFetchDate().getTime();
      Date now = new Date();

      if (lastFetchDate == 0) {
         Calendar cal = Calendar.getInstance();

         cal.add(Calendar.DATE, -MAX_DAYS_TO_FETCH); // back 120 days
         lastFetchDate = cal.getTimeInMillis();
      }

      seller.setKeySellerId(seller.getSellerId());
      seller.setFeedbackLastFetchDate(now);

      List<Transaction> loaded = new ArrayList<Transaction>();
      int pageNumber = 1;

      while (true) {
         m_logger.info("Fetch seller feedbacks...");

         Trading trading = m_apiManager.getTrading(seller);
         String content = trading.getFeedbacks(pageNumber);
         List<Feedback> feedbacks = m_getFeedback.getFeedbacks(seller.getSellerId(), content);
         List<Feedback> filteredFeedbacks = filterByDate(feedbacks, lastFetchDate);

         if (!feedbacks.isEmpty()) {
            m_feedbackDao.insert(filteredFeedbacks.toArray(new Feedback[0]));
         }

         m_logger.info(filteredFeedbacks.size() + " rows retrieved.");

         for (Feedback feedback : filteredFeedbacks) {
            try {
               Transaction trx = m_trxDao.findByItemIdAndTransactionId(feedback.getItemId(), feedback
                     .getTransactionId(), TransactionEntity.READSET_FULL);
               prepareTransaction(trx, feedback);

               m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
               loaded.add(trx);
            } catch (Exception e) {
               m_logger.warn("No transaction(" + feedback.getId() + "," + feedback.getItemId() + ","
                     + feedback.getTransactionId() + ") found", e);
            }
         }

         if (filteredFeedbacks.size() < feedbacks.size()) {
            break;
         } else {
            pageNumber++;
         }
      }

      m_sellerDao.updateByPK(seller, SellerEntity.UPDATESET_FULL);
      m_logger.info(loaded.size() + " feedbacks loaded.");

      return loaded;
   }

   private void prepareTransaction(Transaction trx, Feedback feedback) {
      int feedbackStatus = getFeedbackStatus(trx.getFeedbackStatus(), feedback.getType(), feedback.getRole());

      trx.setKeyId(trx.getId());
      trx.setFeedbackStatus(feedbackStatus);

      if (feedback.getRole() == FeedbackRole.TO_SELLER.getValue()) {
         trx.setFeedbackRecievedMessage(feedback.getMessage());
         trx.setFeedbackRecievedDate(feedback.getCommentDate());
      } else {
         trx.setFeedbackLeftMessage(feedback.getMessage());
         trx.setFeedbackLeftDate(feedback.getCommentDate());
      }
   }
}
