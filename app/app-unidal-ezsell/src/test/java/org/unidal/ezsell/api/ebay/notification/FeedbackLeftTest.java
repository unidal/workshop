package org.unidal.ezsell.api.ebay.notification;

import java.util.List;

import org.junit.Test;
import org.unidal.ezsell.api.ebay.trading.GetFeedback;
import org.unidal.ezsell.dal.Feedback;
import org.unidal.ezsell.feedback.FeedbackRole;
import org.unidal.ezsell.feedback.FeedbackType;

public class FeedbackLeftTest extends AbstractNotificationTest {
   @Test
   public void test() throws Exception {
      GetFeedback getFeedback = lookup(GetFeedback.class);
      String content = getEventResponse("FeedbackLeft");
      List<Feedback> feedbacks = getFeedback.getFeedbacks(1, content);

      assertEquals(1, feedbacks.size());

      for (Feedback feedback : feedbacks) {
         assertTrue(feedback.getId() > 0);
         assertTrue(feedback.getItemId() > 0);
         assertEquals(FeedbackType.POSITIVE.getValue(), feedback.getType());
         assertEquals(FeedbackRole.TO_BUYER.getValue(), feedback.getRole());
      }
   }
}
