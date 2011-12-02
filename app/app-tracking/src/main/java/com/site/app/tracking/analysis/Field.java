package com.site.app.tracking.analysis;

import com.site.app.FieldId;

public enum Field implements FieldId {
   DATE_FROM("dateFrom"),

   DATE_TO("dateTo"),

   MAX_NUM("maxNum"),

   TOPN_GROUP_BY("groupBy"),

   PAGE_URL("url"),

   USERNAME("username"),

   PASSWORD("password"),

   LAST_URL("last_url"),

   STATS_TABLE("table"),
   
   STATS_GROUP_BY("groupBy"),
   
   PURGE_TABLE("purgeTable"),
   
   PURGE_TABLE_ACTION("action"),
   
   ;

   private String m_name;

   private Field(String name) {
      m_name = name;
   }

   public String getName() {
      return m_name;
   }
}
