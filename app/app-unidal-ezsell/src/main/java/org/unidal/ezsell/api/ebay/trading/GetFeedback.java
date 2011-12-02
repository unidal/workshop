package org.unidal.ezsell.api.ebay.trading;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.dal.Feedback;
import org.unidal.ezsell.feedback.FeedbackRole;
import org.unidal.ezsell.feedback.FeedbackType;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSourceType;

public class GetFeedback {
   @Inject
   private WdbcQuery m_query;

   @Inject
   private WdbcEngine m_engine;

   public List<Feedback> getFeedbacks(int sellerId, String content) throws WdbcException {
      WdbcResult result = m_engine.execute(m_query, new StringSource(WdbcSourceType.XML, content));

      return getFeedbacks(sellerId, result);
   }

   private List<Feedback> getFeedbacks(int sellerId, WdbcResult result) {
      List<Feedback> list = new ArrayList<Feedback>();
      int rows = result.getRowSize();

      for (int row = 0; row < rows; row++) {
         Feedback feedback = new Feedback();

         feedback.setSellerId(sellerId);
         feedback.setId(result.getLong(row, "feedbackId", 0));
         feedback.setItemId(result.getLong(row, "itemId", 0));
         feedback.setTransactionId(result.getLong(row, "transactionId", 0));
         feedback.setCommentDate(result.getDate(row, "commentTime", null, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
         feedback.setMessage(result.getString(row, "commentText"));
         feedback.setType(getFeedbackType(result.getString(row, "commentType")).getValue());
         feedback.setRole(getFeedbackRole(result.getString(row, "role")).getValue());

         list.add(feedback);
      }

      return list;
   }

   private FeedbackRole getFeedbackRole(String role) {
      return FeedbackRole.getByName(role, FeedbackRole.UNKNOWN);
   }

   private FeedbackType getFeedbackType(String type) {
      return FeedbackType.getByName(type, FeedbackType.UNKNOWN);
   }
}
