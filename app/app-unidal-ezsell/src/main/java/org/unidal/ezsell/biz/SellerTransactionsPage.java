package org.unidal.ezsell.biz;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.common.LastUrlManager;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;
import org.unidal.ezsell.feedback.FeedbackLoader;
import org.unidal.ezsell.transaction.TransactionLabel;
import org.unidal.ezsell.transaction.TransactionLoader;
import org.unidal.ezsell.transaction.TransactionOrderBy;
import org.unidal.ezsell.transaction.TransactionStatus;
import org.unidal.ezsell.transaction.TransactionLabel.Mode;
import org.unidal.ezsell.view.FileAction;
import org.unidal.ezsell.view.FileViewer;
import org.unidal.ezsell.view.JspViewer;
import org.unidal.ezsell.view.ReportAction;
import org.unidal.ezsell.view.ReportViewer;

import com.site.app.tag.function.Format;
import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class SellerTransactionsPage implements PageHandler<EbayContext>, LogEnabled {
   private static long ONE_DAY = 86400L * 1000L;

   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private ReportViewer m_reportViewer;

   @Inject
   private FileViewer m_fileViewer;

   @Inject
   private TransactionDao m_trxDao;

   @Inject
   private TransactionLabel m_trxLabel;

   @Inject
   private TransactionLoader m_trxLoader;

   @Inject
   private FeedbackLoader m_feedbackLoader;

   @Inject
   private LastUrlManager m_lastUrlManager;

   private Logger m_logger;

   private List<Transaction> findTransactions(int sellerId, SellerTransactionsPayload payload) throws DalException {
      Date dateFrom = payload.getDateFrom();
      Date dateTo = payload.getDateTo();
      Date shipDateFrom = payload.getShipDateFrom();
      Date shipDateTo = payload.getShipDateTo();
      String keyword = nullIfEmpty(payload.getKeyword());
      String nonKeyword = nullIfEmpty(payload.getNonKeyword());
      String label = nullIfEmpty(payload.getLabel());
      String nonLabel = nullIfEmpty(payload.getNonLabel());
      String buyerAccount = nullIfEmpty(payload.getBuyerAccount());
      String shippingTrackingId = nullIfEmpty(payload.getShippingTrackingId());
      Integer[] statuses = getStatusIds(payload.getStatuses());
      int feedbackStatus = payload.getFeedbackStatus() == null ? 0 : payload.getFeedbackStatus().getValue();
      String orderBy = payload.getOrderBy().getName();

      if (dateTo != null) { // end of that day
         dateTo = new Date(dateTo.getTime() + ONE_DAY - 1000);
      }

      if (shipDateTo != null) { // end of that day
         shipDateTo = new Date(shipDateTo.getTime() + ONE_DAY - 1000);
      }

      Integer[] ids = m_trxLabel.getTransactionIdsByLabels(label, false);
      Integer[] nonIds = m_trxLabel.getTransactionIdsByLabels(nonLabel, true);
      List<Transaction> transactions;

      if (ids != null && ids.length == 0) {
         transactions = Collections.<Transaction> emptyList();
      } else {
         transactions = m_trxDao.findAllByCriterias(sellerId, keyword, nonKeyword, ids, nonIds, dateFrom, dateTo,
               shipDateFrom, shipDateTo, buyerAccount, shippingTrackingId, statuses, feedbackStatus, orderBy,
               TransactionEntity.READSET_FULL);
      }

      return transactions;
   }

   private Integer[] getStatusIds(List<TransactionStatus> statuses) {
      if (statuses == null) {
         return null;
      }

      Integer[] ids = new Integer[statuses.size()];
      int index = 0;

      for (TransactionStatus status : statuses) {
         ids[index++] = status.getValue();
      }

      return ids;
   }

   public void handleInbound(EbayContext ctx) {
      SellerTransactionsPayload payload = (SellerTransactionsPayload) ctx.getPayload();
      Seller seller = ctx.getSeller();

      if (payload.isLoadTransactions()) {
         try {
            List<Transaction> loaded = m_trxLoader.loadLatestTransactionsFromApi(seller);

            ctx.setTransactions(loaded);
         } catch (Exception e) {
            e.printStackTrace();
            ctx.addError(new ErrorObject("transaction.load.error", e));
         }
      } else if (payload.isLoadFeedbacks()) {
         try {
            List<Transaction> loaded = m_feedbackLoader.loadLatestFeedbacksFromApi(seller);

            ctx.setTransactions(loaded);
         } catch (Exception e) {
            e.printStackTrace();
            ctx.addError(new ErrorObject("feedback.load.error", e));
         }

         if (!ctx.hasErrors()) {
            try {
               List<Transaction> trxs = m_trxDao.findAllForLeaveFeedback(seller.getSellerId(),
                     TransactionEntity.READSET_FULL);
               int count = 0;

               m_logger.info("Need check feedbacks left for " + trxs.size() + " transactions.");

               for (Transaction trx : trxs) {
                  boolean loaded = m_feedbackLoader.loadFeedbackFromApi(seller, trx);

                  if (loaded) {
                     count++;
                  }
               }

               m_logger.info(trxs.size() + " transactions checked. " + count + " feedbacks loaded.");
            } catch (Exception e) {
               e.printStackTrace();
               ctx.addError(new ErrorObject("feedback.load.error", e));
            }
         }
      } else if (payload.isApplyLabels() || payload.isRemoveLabels()) {
         Integer[] ids = payload.getTransactionIds();
         String labels = payload.getLabels();

         if (ids != null && labels != null) {
            Mode mode = payload.isApplyLabels() ? Mode.ADD : Mode.REMOVE;

            m_trxLabel.apply(ctx, labels, ids, mode);
         }

         payload.setSearch(true); // shift to search
      } else if (payload.isIndexLabels()) {
         try {
            List<Transaction> transactions = m_trxDao.findAll(seller.getSellerId(), TransactionEntity.READSET_IL);

            m_trxLabel.reindexLabels(transactions);
         } catch (DalException e) {
            e.printStackTrace();
            ctx.addError(new ErrorObject("dal.transaction.error", e));
         }

         payload.setOrderBy(TransactionOrderBy.TRANSACTION_DATE.name());
         payload.setSearch(true); // shift to search
      }
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      EbayModel model = new EbayModel(ctx);
      SellerTransactionsPayload payload = (SellerTransactionsPayload) ctx.getPayload();

      if (payload.isSearch() || payload.isPrintTransactionsReport() || payload.isDownloadCsvFile()) {
         try {
            model.setTransactions(findTransactions(ctx.getSeller().getSellerId(), payload));
            m_lastUrlManager.setLastUrl(ctx);
         } catch (DalException e) {
            e.printStackTrace();
            ctx.addError(new ErrorObject("dal.transactions.error", e));
         }
      } else if (payload.isLoadTransactions()) {
         model.setTransactions(ctx.getTransactions());
      } else {
         setupDefaultSearchValues(payload);
      }

      if (payload.isPrintTransactionsReport()) {
         model.setReportAction(ReportAction.TRANSACTIONS_REPORT);
         model.setReportId(Format.format(new Date(), "yyyyMMddHHmmss"));
         m_reportViewer.view(ctx, model);
      } else if (payload.isDownloadCsvFile()) {
         model.setFileAction(FileAction.TRANSACTIONS_CSV);
         model.setFileId(Format.format(new Date(), "yyyyMMddHHmmss"));
         m_fileViewer.view(ctx, model);
      } else {
         model.setPage(EbayPage.SELLER_TRANSACTIONS);
         m_jspViewer.view(ctx, model);
      }
   }

   private String nullIfEmpty(String keyword) {
      if (keyword != null && keyword.trim().length() == 0) {
         return null;
      }

      return keyword;
   }

   private void setupDefaultSearchValues(SellerTransactionsPayload payload) {
      if (payload.getDateFrom() == null) {
         payload.setDateFrom(new Date(System.currentTimeMillis() - 2 * ONE_DAY));
      }

      if (payload.getOrderBy() == null) {
         payload.setOrderBy(TransactionOrderBy.TRANSACTION_DATE.name());
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
