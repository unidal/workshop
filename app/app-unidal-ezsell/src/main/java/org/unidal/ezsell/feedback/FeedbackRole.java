package org.unidal.ezsell.feedback;

public enum FeedbackRole {
   TO_SELLER("Seller", 1),

   TO_BUYER("Buyer", 2),

   UNKNOWN("Unknown", 9);

   private String m_name;

   private int m_value;

   private FeedbackRole(String name, int value) {
      m_name = name;
      m_value = value;
   }

   public String getName() {
      return m_name;
   }

   public int getValue() {
      return m_value;
   }

   public static FeedbackRole getByName(String name, FeedbackRole defaultValue) {
      for (FeedbackRole role : FeedbackRole.values()) {
         if (role.getName().equals(name)) {
            return role;
         }
      }

      return defaultValue;
   }

   public static FeedbackRole getByValue(int value, FeedbackRole defaultValue) {
      for (FeedbackRole role : FeedbackRole.values()) {
         if (role.getValue() == value) {
            return role;
         }
      }

      return defaultValue;
   }
}
