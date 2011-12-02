package org.unidal.ezsell.api.ebay.trading;

import java.util.List;

import org.junit.Test;
import org.unidal.ezsell.api.ebay.trading.GetFeedback;
import org.unidal.ezsell.dal.Feedback;
import org.unidal.ezsell.feedback.FeedbackRole;
import org.unidal.ezsell.feedback.FeedbackType;

public class GetFeedbackTest extends AbstractTradingTest {
   @Test
   public void test() throws Exception {
      GetFeedback getFeedback = lookup(GetFeedback.class);
      List<Feedback> feedbacks = getFeedback.getFeedbacks(1, getResponse("GetFeedback.xml"));

      assertEquals(25, feedbacks.size());

      for (Feedback feedback : feedbacks) {
         assertTrue(feedback.getId() > 0);
         assertTrue(feedback.getItemId() > 0);
         assertTrue(feedback.getTransactionId() > 0);
         assertEquals(FeedbackType.POSITIVE.getValue(), feedback.getType());
         assertEquals(FeedbackRole.TO_SELLER.getValue(), feedback.getRole());
      }
   }
}
