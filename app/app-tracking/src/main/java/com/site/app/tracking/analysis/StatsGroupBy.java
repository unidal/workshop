package com.site.app.tracking.analysis;

public enum StatsGroupBy {
   FIFTEEN_MINUTES("15m", "Every 15 minutes"),

   THIRTY_MINUTES("30m", "Every 30 minutes"),
   
   HOUR("1h", "Every hour"),

   DAY("1d", "Every day"),

   ;

   private String m_name;

   private String m_description;

   private StatsGroupBy(String name, String description) {
      m_name = name;
      m_description = description;
   }

   public static StatsGroupBy get(String name, StatsGroupBy defaultValue) {
      if (name != null) {
         for (StatsGroupBy e : StatsGroupBy.values()) {
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
