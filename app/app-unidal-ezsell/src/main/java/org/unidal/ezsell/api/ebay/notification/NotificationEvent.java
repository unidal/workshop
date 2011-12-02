package org.unidal.ezsell.api.ebay.notification;

public class NotificationEvent {
   private NotificationEventName m_eventName;

   private String m_userAccount;

   private String m_responseContent;

   public NotificationEvent(String eventName, String userAccount, String responseContent) {
      m_eventName = NotificationEventName.getByName(eventName, null);
      m_userAccount = userAccount;
      m_responseContent = responseContent;

      if (m_eventName == null) {
         throw new RuntimeException("Unsupported event name: " + eventName);
      }
   }

   public NotificationEventName getEventName() {
      return m_eventName;
   }

   public String getUserAccount() {
      return m_userAccount;
   }

   public String getResponseContent() {
      return m_responseContent;
   }

   @Override
   public String toString() {
      return "Event[" + m_eventName.name() + "," + m_userAccount + "]";
   }
}
