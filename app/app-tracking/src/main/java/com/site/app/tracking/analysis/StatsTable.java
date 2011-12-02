package com.site.app.tracking.analysis;

public enum StatsTable {
   PAGE_VISIT_TRACK("page_visit_track", "page_visit_track"),

   STATS("stats", "stats"),

   ;

   private String m_name;

   private String m_description;

   private StatsTable(String name, String description) {
      m_name = name;
      m_description = description;
   }

   public static StatsTable get(String name, StatsTable defaultValue) {
      if (name != null) {
         for (StatsTable e : StatsTable.values()) {
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
