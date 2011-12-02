package com.site.bes.common.dal;

import java.util.List;

import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.QueryEngine;

import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;
public class EventBatchLogDaoCodeGen {
   public EventBatchLogDo createLocal() {
      EventBatchLogDo protoDo = new EventBatchLogDo();

      return protoDo;
   }

   public List findAllUnprocessed(java.util.Date deadline, Readset readset) {
      EventBatchLogDo protoDo = new EventBatchLogDo();

      protoDo.setDeadline(deadline);

      List result = QueryEngine.getInstance().queryMultiple(
            EventBatchLogEntity.FIND_ALL_UNPROCESSED, 
            protoDo,
            readset);
      
      return result;
   }
   
   public List findAllByEventId(int eventId, Readset readset) {
      EventBatchLogDo protoDo = new EventBatchLogDo();

      protoDo.setEventId(eventId);

      List result = QueryEngine.getInstance().queryMultiple(
            EventBatchLogEntity.FIND_ALL_BY_EVENT_ID, 
            protoDo,
            readset);
      
      return result;
   }
   
   public EventBatchLogDo findByPK(int keyBatchId, Readset readset) throws DalException {
      EventBatchLogDo protoDo = new EventBatchLogDo();

      protoDo.setKeyBatchId(keyBatchId);

      EventBatchLogDo result = (EventBatchLogDo) QueryEngine.getInstance().querySingle(
            EventBatchLogEntity.FIND_BY_PK, 
            protoDo,
            readset);
      
      return result;
   }
   
   public int insert(EventBatchLogDo protoDo) throws DalException {
      return QueryEngine.getInstance().insertSingle(
            EventBatchLogEntity.INSERT,
            protoDo);
   }
   
   public int updateByPK(EventBatchLogDo protoDo, Updateset updateset) throws DalException {
      return QueryEngine.getInstance().updateSingle(
            EventBatchLogEntity.UPDATE_BY_PK,
            protoDo,
            updateset);
   }
   
   public int deleteByPK(EventBatchLogDo protoDo) throws DalException {
      return QueryEngine.getInstance().deleteSingle(
            EventBatchLogEntity.DELETE_BY_PK,
            protoDo);
   }
   
}
