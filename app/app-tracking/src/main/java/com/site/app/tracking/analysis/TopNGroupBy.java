package com.site.app.tracking.analysis;

public enum TopNGroupBy {
   PAGE("page", "Page URL"),

   SOURCE("source", "From Page"),

   CATEGORY1("category1", "Main Category"),

   CATEGORY2("category2", "Second Category"),
   
   ON_TOP("onTop", "Is On Top?"),

   ;

   private String m_name;

   private String m_description;

   private TopNGroupBy(String name, String description) {
      m_name = name;
      m_description = description;
   }

   public static TopNGroupBy get(String name, TopNGroupBy defaultValue) {
      if (name != null) {
         for (TopNGroupBy e : TopNGroupBy.values()) {
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
