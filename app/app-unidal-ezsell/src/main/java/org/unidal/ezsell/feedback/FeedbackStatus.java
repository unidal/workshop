package org.unidal.ezsell.feedback;

public enum FeedbackStatus {
   ALL("All", 0),

   NONE("Not recieved and not left", 1),

   RECIEVED("Recieved, but not left", 2),

   LEFT("Not recieved, but left", 3),

   RECIEVED_LEFT("Recieved and left", 4),

   ;

   private String m_description;

   private int m_value;

   private FeedbackStatus(String description, int value) {
      m_description = description;
      m_value = value;
   }

   public String getDescription() {
      return m_description;
   }

   public int getValue() {
      return m_value;
   }

   public static FeedbackStatus getByValue(int value, FeedbackStatus defaultValue) {
      for (FeedbackStatus type : FeedbackStatus.values()) {
         if (type.getValue() == value) {
            return type;
         }
      }

      return defaultValue;
   }
}
