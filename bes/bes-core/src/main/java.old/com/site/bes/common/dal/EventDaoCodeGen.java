package com.site.bes.common.dal;

import java.util.List;

import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.QueryEngine;

import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;import com.site.kernel.dal.db.Ref;

public class EventDaoCodeGen {
   public EventDo createLocal() {
      EventDo protoDo = new EventDo();

      return protoDo;
   }

   public List findAllByIdRange(int startEventId, int endEventId, Readset readset) {
      EventDo protoDo = new EventDo();

      protoDo.setStartEventId(startEventId);
      protoDo.setEndEventId(endEventId);

      List result = QueryEngine.getInstance().queryMultiple(
            EventEntity.FIND_ALL_BY_ID_RANGE, 
            protoDo,
            readset);
      
      return result;
   }
   
   public List fetchEvents(String eventType, String consumerType, String consumerId, Ref batchId, Readset readset) {
      EventDo protoDo = new EventDo();

      protoDo.setEventType(eventType);
      protoDo.setConsumerType(consumerType);
      protoDo.setConsumerId(consumerId);

      List result = QueryEngine.getInstance().queryMultiple(
            EventEntity.FETCH_EVENTS, 
            protoDo,
            readset);
      
      batchId.set(protoDo.getFieldValue(EventEntity.BATCH_ID));

      return result;
   }
   
   public List fetchScheduleEvents(String eventType, String consumerType, java.util.Date startScheduleDate, java.util.Date endScheduleDate, Readset readset) {
      EventDo protoDo = new EventDo();

      protoDo.setEventType(eventType);
      protoDo.setConsumerType(consumerType);
      protoDo.setStartScheduleDate(startScheduleDate);
      protoDo.setEndScheduleDate(endScheduleDate);

      List result = QueryEngine.getInstance().queryMultiple(
            EventEntity.FETCH_SCHEDULE_EVENTS, 
            protoDo,
            readset);
      
      return result;
   }
   
   public List findByEventType(String eventType, Readset readset) {
      EventDo protoDo = new EventDo();

      protoDo.setEventType(eventType);

      List result = QueryEngine.getInstance().queryMultiple(
            EventEntity.FIND_BY_EVENT_TYPE, 
            protoDo,
            readset);
      
      return result;
   }
   
   public EventDo findByPK(int keyEventId, Readset readset) throws DalException {
      EventDo protoDo = new EventDo();

      protoDo.setKeyEventId(keyEventId);

      EventDo result = (EventDo) QueryEngine.getInstance().querySingle(
            EventEntity.FIND_BY_PK, 
            protoDo,
            readset);
      
      return result;
   }
   
   public int insert(EventDo protoDo) throws DalException {
      return QueryEngine.getInstance().insertSingle(
            EventEntity.INSERT,
            protoDo);
   }
   
   public int updateByPK(EventDo protoDo, Updateset updateset) throws DalException {
      return QueryEngine.getInstance().updateSingle(
            EventEntity.UPDATE_BY_PK,
            protoDo,
            updateset);
   }
   
   public int deleteByPK(EventDo protoDo) throws DalException {
      return QueryEngine.getInstance().deleteSingle(
            EventEntity.DELETE_BY_PK,
            protoDo);
   }
   
}
