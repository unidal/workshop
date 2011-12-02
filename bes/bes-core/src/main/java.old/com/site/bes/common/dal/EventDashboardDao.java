package com.site.bes.common.dal;

public class EventDashboardDao extends EventDashboardDaoCodeGen {
   private static final EventDashboardDao s_instance = new EventDashboardDao();

   protected EventDashboardDao() {
   }

   public static EventDashboardDao getInstance() {
      return s_instance;
   }

}
