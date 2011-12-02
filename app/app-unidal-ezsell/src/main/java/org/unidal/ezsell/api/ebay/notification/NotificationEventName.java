package org.unidal.ezsell.api.ebay.notification;

public enum NotificationEventName {
   Feedback,

   FeedbackLeft,

   ;

   public static NotificationEventName getByName(String name, NotificationEventName defaultValue) {
      for (NotificationEventName e : NotificationEventName.values()) {
         if (e.name().equals(name)) {
            return e;
         }
      }

      return defaultValue;
   }
}
