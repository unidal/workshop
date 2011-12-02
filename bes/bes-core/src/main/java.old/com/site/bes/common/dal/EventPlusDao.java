package com.site.bes.common.dal;

public class EventPlusDao extends EventPlusDaoCodeGen {
   private static final EventPlusDao s_instance = new EventPlusDao();

   protected EventPlusDao() {
   }

   public static EventPlusDao getInstance() {
      return s_instance;
   }

}
