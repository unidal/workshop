package com.site.app.tracking.analysis;

public enum PurgeTable {
   PAGE_VISIT_TRACK("page_visit_track", "page_visit_track"),

   STATS("stats", "stats"),
   
   ;

   private String m_name;

   private String m_description;

   private PurgeTable(String name, String description) {
      m_name = name;
      m_description = description;
   }

   public static PurgeTable get(String name, PurgeTable defaultValue) {
      if (name != null) {
         for (PurgeTable e : PurgeTable.values()) {
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
