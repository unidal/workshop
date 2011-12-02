package org.unidal.ezsell.register;

public enum OperatorStatus {
   CREATED(1),

   EMAIL_CONFIRMED(2),

   EBAY_SELLER_LINKED(3),

   SUSPENDED(4);

   private int m_value;

   private OperatorStatus(int value) {
      m_value = value;
   }

   public static OperatorStatus getByValue(int value, OperatorStatus defaultValue) {
      for (OperatorStatus e : OperatorStatus.values()) {
         if (e.getValue() == value) {
            return e;
         }
      }

      return defaultValue;
   }

   public int getValue() {
      return m_value;
   }
}
