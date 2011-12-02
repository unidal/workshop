package org.unidal.ezsell.api.ebay.notification;

public enum NotificationStatus {
   RECIEVED("recieved", 1),

   PROCESSED("processed", 2),

   SKIPPED("skipped", 3),

   FAILURE("failure", 4),

   UNKNOWN("unknown", 9),

   ;

   private String m_name;

   private int m_value;

   private NotificationStatus(String name, int value) {
      m_name = name;
      m_value = value;
   }

   public static NotificationStatus getByName(String name, NotificationStatus defaultValue) {
      for (NotificationStatus e : NotificationStatus.values()) {
         if (e.getName().equals(name)) {
            return e;
         }
      }

      return defaultValue;
   }

   public static NotificationStatus getByValue(int value, NotificationStatus defaultValue) {
      for (NotificationStatus e : NotificationStatus.values()) {
         if (e.getValue() == value) {
            return e;
         }
      }

      return defaultValue;
   }

   public NotificationStatus[] getValues() {
      return values();
   }

   public int getValue() {
      return m_value;
   }

   public String getName() {
      return m_name;
   }
}
