package com.site.bes.common.dal;

public class EventBatchLogDao extends EventBatchLogDaoCodeGen {
   private static final EventBatchLogDao s_instance = new EventBatchLogDao();

   protected EventBatchLogDao() {
   }

   public static EventBatchLogDao getInstance() {
      return s_instance;
   }

}
