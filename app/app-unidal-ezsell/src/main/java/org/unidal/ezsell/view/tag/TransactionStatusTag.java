package org.unidal.ezsell.view.tag;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.feedback.FeedbackType;
import org.unidal.ezsell.transaction.TransactionStatus;

import com.site.lookup.annotation.Inject;
import com.site.web.jsp.tag.AbstractBodyTag;

public class TransactionStatusTag extends AbstractBodyTag {
   private static final String IMG_BASE = "/img/";

   private static final long serialVersionUID = 1L;

   private PageContext m_pageContext;

   private Transaction m_trx;

   private String m_imgBase = IMG_BASE;

   private MessageFormat m_format = new MessageFormat("{0}{1,date,yyyy-MM-dd HH:mm:ss}");

   @Override
   protected void handleBody() throws JspTagException {
      TransactionStatus status = TransactionStatus.getByValue(m_trx.getStatus(), TransactionStatus.UNKNOWN);
      StringBuilder sb = new StringBuilder(1024);

      switch (status) {
      case REFUNDED:
         showIcon(sb, "refunded.png", "Payment refunded");
         break;
      default:
         if (m_trx.getPaidTime() != null) {
            showIcon(sb, "paid.png", formatDate("Paid at ", m_trx.getPaidTime()));
         } else {
            showIcon(sb, "not_paid.png", "Not paid");
         }
         break;
      }

      if (m_trx.getShippedTime() != null) {
         showIcon(sb, "shipped.png", formatDate("Shipped at ", m_trx.getShippedTime()));
      } else {
         showIcon(sb, "not_shipped.png", "Item not shipped");
      }

      int feedbackStatus = m_trx.getFeedbackStatus();

      showFeedbackLeftIcon(sb, feedbackStatus);
      showFeedbackRecievedIcon(sb, feedbackStatus);

      try {
         m_pageContext.getOut().append(sb);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private String formatDate(String message, Date date) {
      if (date != null) {
         return m_format.format(new Object[] { message, date });
      } else {
         return "";
      }
   }

   @Override
   public void setPageContext(PageContext pageContext) {
      String contextPath = pageContext.getServletContext().getContextPath();

      m_pageContext = pageContext;

      if (contextPath != null) {
         m_imgBase = contextPath + IMG_BASE;
      }

      super.setPageContext(pageContext);
   }

   @Inject
   public void setTransaction(Transaction transaction) {
      m_trx = transaction;
   }

   private void showFeedbackLeftIcon(StringBuilder sb, int feedbackStatus) {
      int leftType = feedbackStatus / 10;

      if (leftType > 0) {
         Date feedbackLeftDate = m_trx.getFeedbackLeftDate();
         FeedbackType type = FeedbackType.getByValue(leftType, FeedbackType.UNKNOWN);
         String msg;
         String style;

         switch (type) {
         case NEGATIVE:
            msg = formatDate("Negative feedback left at ", feedbackLeftDate);
            style = "background:red";
            break;
         case NEUTRAL:
            msg = formatDate("Neutral feedback left at ", feedbackLeftDate);
            style = "background:gray";
            break;
         case POSITIVE:
            msg = formatDate("Positive feedback left at ", feedbackLeftDate);
            style = null;
            break;
         default:
            msg = formatDate(type.getName() + " feedback left at ", feedbackLeftDate);
            style = null;
            break;
         }

         if (m_trx.getFeedbackLeftMessage() != null) {
            msg += "\r\n" + m_trx.getFeedbackLeftMessage();
         }

         showIcon(sb, "feedback_left.png", msg, style);
      } else {
         showIcon(sb, "feedback_not_left.png", "Feedback not left");
      }
   }

   private void showFeedbackRecievedIcon(StringBuilder sb, int feedbackStatus) {
      int recievedType = feedbackStatus % 10;

      if (recievedType > 0) {
         Date feedbackRecievedDate = m_trx.getFeedbackRecievedDate();
         FeedbackType type = FeedbackType.getByValue(recievedType, FeedbackType.UNKNOWN);
         String msg;
         String style;

         switch (type) {
         case NEGATIVE:
            msg = formatDate("Negative feedback recieved at ", feedbackRecievedDate);
            style = "background:red";
            break;
         case NEUTRAL:
            msg = formatDate("Neutral feedback recieved at ", feedbackRecievedDate);
            style = "background:gray";
            break;
         case POSITIVE:
            msg = formatDate("Positive feedback recieved at ", feedbackRecievedDate);
            style = null;
            break;
         default:
            msg = formatDate(type.getName() + " feedback recieved at ", feedbackRecievedDate);
            style = null;
            break;
         }

         if (m_trx.getFeedbackRecievedMessage() != null) {
            msg += "\r\n" + m_trx.getFeedbackRecievedMessage();
         }

         showIcon(sb, "feedback_recieved.png", msg, style);
      } else {
         showIcon(sb, "feedback_not_recieved.png", "Feedback not recieved");
      }
   }

   private void showIcon(StringBuilder sb, String name, String alt) {
      showIcon(sb, name, alt, null);
   }

   private void showIcon(StringBuilder sb, String name, String alt, String style) {
      sb.append("<img with=\"16\" height=\"16\" src=\"").append(m_imgBase).append(name);
      sb.append("\" title=\"").append(alt).append("\"");

      if (style != null) {
         sb.append(" style=\"").append(style).append("\"");
      }

      sb.append(">");
   }
}
