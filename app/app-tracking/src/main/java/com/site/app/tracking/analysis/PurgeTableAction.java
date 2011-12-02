package com.site.app.tracking.analysis;

public enum PurgeTableAction {
   CHECK("Check", "Count how many rows will be purged"),
   
   PURGE("Purge", "Purge the selected table"),

   ;

   private String m_name;

   private String m_description;

   private PurgeTableAction(String name, String description) {
      m_name = name;
      m_description = description;
   }

   public static PurgeTableAction get(String name, PurgeTableAction defaultValue) {
      if (name != null) {
         for (PurgeTableAction e : PurgeTableAction.values()) {
            if (name.equals(e.getName())) {
               return e;
            }
         }
      }

      return defaultValue;
   }

   public String getDescription() {
      return m_description;
   }

   public String getName() {
      return m_name;
   }
}
