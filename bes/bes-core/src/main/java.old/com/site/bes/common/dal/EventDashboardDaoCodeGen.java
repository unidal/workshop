package com.site.bes.common.dal;

import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.QueryEngine;

import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;
public class EventDashboardDaoCodeGen {
   public EventDashboardDo createLocal() {
      EventDashboardDo protoDo = new EventDashboardDo();

      return protoDo;
   }

   public EventDashboardDo findByPK(String keyEventType, String keyConsumerType, Readset readset) throws DalException {
      EventDashboardDo protoDo = new EventDashboardDo();

      protoDo.setKeyEventType(keyEventType);
      protoDo.setKeyConsumerType(keyConsumerType);

      EventDashboardDo result = (EventDashboardDo) QueryEngine.getInstance().querySingle(
            EventDashboardEntity.FIND_BY_PK, 
            protoDo,
            readset);
      
      return result;
   }
   
   public int insert(EventDashboardDo protoDo) throws DalException {
      return QueryEngine.getInstance().insertSingle(
            EventDashboardEntity.INSERT,
            protoDo);
   }
   
   public int upsert(EventDashboardDo protoDo) throws DalException {
      return QueryEngine.getInstance().insertSingle(
            EventDashboardEntity.UPSERT,
            protoDo);
   }
   
   public int updateByPK(EventDashboardDo protoDo, Updateset updateset) throws DalException {
      return QueryEngine.getInstance().updateSingle(
            EventDashboardEntity.UPDATE_BY_PK,
            protoDo,
            updateset);
   }
   
   public int deleteByPK(EventDashboardDo protoDo) throws DalException {
      return QueryEngine.getInstance().deleteSingle(
            EventDashboardEntity.DELETE_BY_PK,
            protoDo);
   }
   
}
