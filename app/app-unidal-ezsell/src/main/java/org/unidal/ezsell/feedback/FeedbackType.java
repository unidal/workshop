package org.unidal.ezsell.feedback;

public enum FeedbackType {
   POSITIVE("Positive", 1),

   NEUTRAL("Neutral", 2),

   NEGATIVE("Negative", 3),

   WITHDRAWN("Withdrawn", 4),

   UNKNOWN("Unknown", 9);

   private String m_name;

   private int m_value;

   private FeedbackType(String name, int value) {
      m_name = name;
      m_value = value;
   }

   public String getName() {
      return m_name;
   }

   public int getValue() {
      return m_value;
   }

   public static FeedbackType getByName(String name, FeedbackType defaultValue) {
      for (FeedbackType type : FeedbackType.values()) {
         if (type.getName().equals(name)) {
            return type;
         }
      }

      return defaultValue;
   }

   public static FeedbackType getByValue(int value, FeedbackType defaultValue) {
      for (FeedbackType type : FeedbackType.values()) {
         if (type.getValue() == value) {
            return type;
         }
      }

      return defaultValue;
   }
}
