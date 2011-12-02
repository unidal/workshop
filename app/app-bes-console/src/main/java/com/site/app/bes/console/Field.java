package com.site.app.bes.console;

import com.site.app.FieldId;

public enum Field implements FieldId {
   DATE_FROM("dateFrom"),

   DATE_TO("dateTo"),

   USERNAME("username"),

   PASSWORD("password"),

   LAST_URL("last_url"),

   STATS_GROUP_BY("groupBy"), 
   
   DATE("date"),
   
   ;

   private String m_name;

   private Field(String name) {
      m_name = name;
   }

   public String getName() {
      return m_name;
   }
}
