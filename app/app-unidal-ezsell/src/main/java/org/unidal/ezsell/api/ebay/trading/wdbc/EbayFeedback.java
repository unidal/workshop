package org.unidal.ezsell.api.ebay.trading.wdbc;

import java.util.Date;

import com.site.wdbc.http.configuration.WdbcFieldMeta;
import com.site.wdbc.http.configuration.WdbcMeta;

@WdbcMeta(name = "GetFeedback")
public class EbayFeedback extends AbstractWdbc {
   @WdbcFieldMeta(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", value = "FeedbackDetailArray.FeedbackDetail[*].CommentTime")
   private Date m_commentTime;

   @WdbcFieldMeta("FeedbackDetailArray.FeedbackDetail[*].CommentType")
   private String m_commentType;

   @WdbcFieldMeta("FeedbackDetailArray.FeedbackDetail[*].CommentText")
   private String m_commentText;

   @WdbcFieldMeta("FeedbackDetailArray.FeedbackDetail[*].Role")
   private String m_role;

   @WdbcFieldMeta("FeedbackDetailArray.FeedbackDetail[*].FeedbackID")
   private long m_feedbackId;

   @WdbcFieldMeta("FeedbackDetailArray.FeedbackDetail[*].ItemID")
   private long m_itemId;

   @WdbcFieldMeta("FeedbackDetailArray.FeedbackDetail[*].TransactionID")
   private long m_transactionId;

   public Date getCommentTime() {
      return m_commentTime;
   }

   public String getCommentType() {
      return m_commentType;
   }

   public String getCommentText() {
      return m_commentText;
   }

   public String getRole() {
      return m_role;
   }

   public long getFeedbackId() {
      return m_feedbackId;
   }

   public long getItemId() {
      return m_itemId;
   }

   public long getTransactionId() {
      return m_transactionId;
   }
}
