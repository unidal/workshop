package org.unidal.ezsell.transaction;

public enum TransactionOrderBy {
   TRANSACTION_DATE("transaction_creation_date", "Transaction Date"),

   LABELS("labels", "Label"),
   
   TITLE("item_title", "Title"),
   
   PAID_TIME("paid_time", "Paid Time"),
   
   SHIPPED_TIME("shipped_time", "Shipped Time"),
   
   COUNTRY("country_name", "Country"),
   
   STATUS("status", "Status"),
   
   ;

   private String m_name;

   private String m_description;

   private TransactionOrderBy(String name, String description) {
      m_name = name;
      m_description = description;
   }

   public static TransactionOrderBy get(String name, TransactionOrderBy defaultValue) {
      if (name != null) {
         for (TransactionOrderBy e : TransactionOrderBy.values()) {
            if (name.equals(e.getName())) {
               return e;
            }
         }
      }

      return defaultValue;
   }

   public TransactionOrderBy[] getValues() {
      return values();
   }
   
   public String getDescription() {
      return m_description;
   }

   public String getName() {
      return m_name;
   }
}
