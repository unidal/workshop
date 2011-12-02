package org.unidal.ezsell.view;

public class Email {
   private String m_subject;

   private String m_textContent;

   private String m_htmlContent;

   public String getHtmlContent() {
      return m_htmlContent;
   }

   public String getSubject() {
      return m_subject;
   }

   public String getTextContent() {
      return m_textContent;
   }

   public void setHtmlContent(String htmlContent) {
      m_htmlContent = htmlContent;
   }

   public void setSubject(String subject) {
      m_subject = subject;
   }

   public void setTextContent(String textContent) {
      m_textContent = textContent;
   }

   @Override
   public String toString() {
      return "Email[subject=" + m_subject + ",text=" + m_textContent + ",html=" + m_htmlContent + "]";
   }
}
