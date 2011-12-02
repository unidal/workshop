package com.site.email;

import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailMessage {
   private Email m_email;

   private String m_name;

   private boolean m_textOnly;

   public EmailMessage(String name, boolean textOnly) {
      m_name = name;
      m_textOnly = textOnly;

      if (textOnly) {
         m_email = new SimpleEmail();
      } else {
         m_email = new HtmlEmail();
      }
   }

   public void setAuthentication(String userName, String password) {
      m_email.setAuthentication(userName, password);
   }

   public void addBcc(String email, String name) throws EmailException {
      try {
         m_email.addBcc(email, name);
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public void addCc(String email, String name) throws EmailException {
      try {
         m_email.addCc(email, name);
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public void addHeader(String name, String value) {
      m_email.addHeader(name, value);
   }

   public void addReplyTo(String email, String name) throws EmailException {
      try {
         m_email.addReplyTo(email, name);
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public void addTo(String email, String name) throws EmailException {
      try {
         m_email.addTo(email, name);
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public InternetAddress getFromAddress() {
      return m_email.getFromAddress();
   }

   public String getName() {
      return m_name;
   }

   public String getSubject() {
      return m_email.getSubject();
   }

   public boolean isTextOnly() {
      return m_textOnly;
   }

   public String send() throws EmailException {
      try {
         return m_email.send();
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public void setBounceAddress(String email) {
      m_email.setBounceAddress(email);
   }

   public void setCharset(String charset) {
      m_email.setCharset(charset);
   }

   public void setDebug(boolean debug) {
      m_email.setDebug(debug);
   }

   public void setFrom(String email, String name) throws EmailException {
      try {
         m_email.setFrom(email, name);
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public void setHeaders(Map<String, String> headers) {
      m_email.setHeaders(headers);
   }

   public void setHtmlMessage(String htmlMessage) throws EmailException {
      if (m_textOnly) {
         throw new EmailException("Can't set html message, it's TEXT only");
      }

      try {
         ((HtmlEmail) m_email).setHtmlMsg(htmlMessage);
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }

   public void setSmtpHost(String mailHost) {
      m_email.setHostName(mailHost);
   }

   public void setSmtpPort(int smtpPort) {
      m_email.setSmtpPort(smtpPort);
   }

   public void setSubject(String subject) {
      m_email.setSubject(subject);
   }

   public void setTextMessage(String textMessage) throws EmailException {
      try {
         if (m_textOnly) {
            ((SimpleEmail) m_email).setMsg(textMessage);
         } else {
            ((HtmlEmail) m_email).setTextMsg(textMessage);
         }
      } catch (org.apache.commons.mail.EmailException e) {
         throw new EmailException(e);
      }
   }
}
