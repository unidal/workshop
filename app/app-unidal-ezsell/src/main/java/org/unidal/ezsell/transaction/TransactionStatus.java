package org.unidal.ezsell.transaction;

public enum TransactionStatus {
   NOT_PAID("not_paid", 1, "Not Paid"),

   PAID("paid", 2, "Paid"),

   SHIPPED("shipped", 3, "Shipped"),

   REFUNDED("refunded", 4, "Refunded"),

   UNKNOWN("unknown", 9, "Unknown"),

   ;

   private String m_name;

   private String m_description;

   private int m_value;

   private TransactionStatus(String name, int value, String description) {
      m_name = name;
      m_value = value;
      m_description = description;
   }

   public static TransactionStatus getByName(String name, TransactionStatus defaultValue) {
      for (TransactionStatus e : TransactionStatus.values()) {
         if (e.getName().equals(name)) {
            return e;
         }
      }

      return defaultValue;
   }

   public static TransactionStatus getByValue(int value, TransactionStatus defaultValue) {
      for (TransactionStatus e : TransactionStatus.values()) {
         if (e.getValue() == value) {
            return e;
         }
      }

      return defaultValue;
   }

   public TransactionStatus[] getValues() {
      return values();
   }

   public String getDescription() {
      return m_description;
   }

   public int getValue() {
      return m_value;
   }

   public String getName() {
      return m_name;
   }
}
