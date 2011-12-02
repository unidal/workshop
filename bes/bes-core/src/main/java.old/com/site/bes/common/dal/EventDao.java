package com.site.bes.common.dal;

public class EventDao extends EventDaoCodeGen {
   private static final EventDao s_instance = new EventDao();

   protected EventDao() {
   }

   public static EventDao getInstance() {
      return s_instance;
   }

}
