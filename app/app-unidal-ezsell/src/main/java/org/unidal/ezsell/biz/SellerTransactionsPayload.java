package org.unidal.ezsell.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.unidal.ezsell.EbayPayload;
import org.unidal.ezsell.feedback.FeedbackStatus;
import org.unidal.ezsell.transaction.TransactionOrderBy;
import org.unidal.ezsell.transaction.TransactionStatus;

import com.site.web.mvc.payload.annotation.FieldMeta;

public class SellerTransactionsPayload extends EbayPayload {
   @FieldMeta("keyword")
   private String m_keyword;

   @FieldMeta("nonKeyword")
   private String m_nonKeyword;

   @FieldMeta("label")
   private String m_label;

   @FieldMeta("nonLabel")
   private String m_nonLabel;

   @FieldMeta(value = "dateFrom", format = "yyyy-MM-dd")
   private Date m_dateFrom;

   @FieldMeta(value = "dateTo", format = "yyyy-MM-dd")
   private Date m_dateTo;

   @FieldMeta(value = "shipDateFrom", format = "yyyy-MM-dd")
   private Date m_shipDateFrom;

   @FieldMeta(value = "shipDateTo", format = "yyyy-MM-dd")
   private Date m_shipDateTo;

   @FieldMeta("status")
   private List<TransactionStatus> m_statuses;

   @FieldMeta("feedbackStatus")
   private FeedbackStatus m_feedbackStatus;

   @FieldMeta("orderBy")
   private TransactionOrderBy m_orderBy;

   @FieldMeta("labels")
   private String m_labels;

   @FieldMeta("buyerAccount")
   private String m_buyerAccount;

   @FieldMeta("shippingTrackingId")
   private String m_shippingTrackingId;

   @FieldMeta("sti")
   private Integer[] m_transactionIds;

   @FieldMeta("search")
   private boolean m_search;

   @FieldMeta("lt")
   private boolean m_loadTransactions;

   @FieldMeta("lf")
   private boolean m_loadFeedbacks;

   @FieldMeta("ptr")
   private boolean m_printTransactionsReport;

   @FieldMeta("dcf")
   private boolean m_downloadCsvFile;

   @FieldMeta("al")
   private boolean m_applyLabels;

   @FieldMeta("rl")
   private boolean m_removeLabels;

   @FieldMeta("indexLabels")
   private boolean m_indexLabels;

   public String getBuyerAccount() {
      return m_buyerAccount;
   }

   public Date getDateFrom() {
      return m_dateFrom;
   }

   public Date getDateTo() {
      return m_dateTo;
   }

   public FeedbackStatus getFeedbackStatus() {
      return m_feedbackStatus;
   }

   public FeedbackStatus[] getFeedbackStatuses() {
      return FeedbackStatus.values();
   }

   public String getKeyword() {
      return m_keyword;
   }

   public String getLabel() {
      return m_label;
   }

   public String getLabels() {
      return m_labels;
   }

   public String getNonKeyword() {
      return m_nonKeyword;
   }

   public String getNonLabel() {
      return m_nonLabel;
   }

   public TransactionOrderBy getOrderBy() {
      return m_orderBy;
   }

   public Date getShipDateFrom() {
      return m_shipDateFrom;
   }

   public Date getShipDateTo() {
      return m_shipDateTo;
   }

   public String getShippingTrackingId() {
      return m_shippingTrackingId;
   }

   // helper method for JSP
   public TransactionStatus getStatus() {
      return TransactionStatus.NOT_PAID;
   }

   public List<TransactionStatus> getStatuses() {
      return m_statuses;
   }

   public Integer[] getTransactionIds() {
      return m_transactionIds;
   }

   public boolean isApplyLabels() {
      return m_applyLabels;
   }

   public boolean isDownloadCsvFile() {
      return m_downloadCsvFile;
   }

   public boolean isIndexLabels() {
      return m_indexLabels;
   }

   public boolean isLoadFeedbacks() {
      return m_loadFeedbacks;
   }

   public boolean isLoadTransactions() {
      return m_loadTransactions;
   }

   public boolean isPrintTransactionsReport() {
      return m_printTransactionsReport;
   }

   public boolean isRemoveLabels() {
      return m_removeLabels;
   }

   public boolean isSearch() {
      return m_search;
   }

   public void setApplyLabels(boolean applyLabels) {
      m_applyLabels = applyLabels;
   }

   public void setBuyerAccount(String buyerAccount) {
      m_buyerAccount = buyerAccount;
   }

   public void setDateFrom(Date dateFrom) {
      m_dateFrom = dateFrom;
   }

   public void setDateTo(Date dateTo) {
      m_dateTo = dateTo;
   }

   public void setDownloadCsvFile(boolean downloadCsvFile) {
      m_downloadCsvFile = downloadCsvFile;
   }

   public void setFeedbackStatus(int feedbackStatus) {
      m_feedbackStatus = FeedbackStatus.getByValue(feedbackStatus, null);
   }

   public void setIndexLabels(boolean indexLabels) {
      m_indexLabels = indexLabels;
   }

   public void setKeyword(String keyword) {
      m_keyword = keyword;
   }

   public void setLabel(String label) {
      m_label = label;
   }

   public void setLabels(String labels) {
      m_labels = labels;
   }

   public void setLoadFeedbacks(boolean loadFeedbacks) {
      m_loadFeedbacks = loadFeedbacks;
   }

   public void setLoadTransactions(boolean refresh) {
      m_loadTransactions = refresh;
   }

   public void setNonKeyword(String nonKeyword) {
      m_nonKeyword = nonKeyword;
   }

   public void setNonLabel(String nonLabel) {
      m_nonLabel = nonLabel;
   }

   public void setOrderBy(String orderBy) {
      m_orderBy = TransactionOrderBy.get(orderBy, TransactionOrderBy.TRANSACTION_DATE);
   }

   public void setPrintTransactionsReport(boolean printTransactionsReport) {
      m_printTransactionsReport = printTransactionsReport;
   }

   public void setRemoveLabels(boolean removeLabels) {
      m_removeLabels = removeLabels;
   }

   public void setSearch(boolean search) {
      m_search = search;
   }

   public void setShipDateFrom(Date shipDateFrom) {
      m_shipDateFrom = shipDateFrom;
   }

   public void setShipDateTo(Date shipDateTo) {
      m_shipDateTo = shipDateTo;
   }

   public void setShippingTrackingId(String shippingTrackingId) {
      m_shippingTrackingId = shippingTrackingId;
   }

   public void setStatuses(String[] statuses) {
      m_statuses = new ArrayList<TransactionStatus>();

      for (String name : statuses) {
         TransactionStatus status = TransactionStatus.getByName(name, null);

         if (status != null) {
            m_statuses.add(status);
         }
      }
   }

   public void setTransactionIds(Integer[] transactionIds) {
      m_transactionIds = transactionIds;
   }
}
