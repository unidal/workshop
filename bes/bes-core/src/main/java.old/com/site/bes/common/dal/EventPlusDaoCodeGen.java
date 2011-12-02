package com.site.bes.common.dal;

import java.util.List;

import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.QueryEngine;

import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;
public class EventPlusDaoCodeGen {
   public EventPlusDo createLocal() {
      EventPlusDo protoDo = new EventPlusDo();

      return protoDo;
   }

   public List findAllByEventId(int eventId, Readset readset) {
      EventPlusDo protoDo = new EventPlusDo();

      protoDo.setEventId(eventId);

      List result = QueryEngine.getInstance().queryMultiple(
            EventPlusEntity.FIND_ALL_BY_EVENT_ID, 
            protoDo,
            readset);
      
      return result;
   }
   
   public EventPlusDo findByPK(int keyEventId, String keyConsumerType, Readset readset) throws DalException {
      EventPlusDo protoDo = new EventPlusDo();

      protoDo.setKeyEventId(keyEventId);
      protoDo.setKeyConsumerType(keyConsumerType);

      EventPlusDo result = (EventPlusDo) QueryEngine.getInstance().querySingle(
            EventPlusEntity.FIND_BY_PK, 
            protoDo,
            readset);
      
      return result;
   }
   
   public int[] upsert(EventPlusDo[] protoDos) throws DalException {
      return QueryEngine.getInstance().insertBatch(
            EventPlusEntity.UPSERT,
            protoDos);
   }
   
   public int insert(EventPlusDo protoDo) throws DalException {
      return QueryEngine.getInstance().insertSingle(
            EventPlusEntity.INSERT,
            protoDo);
   }
   
   public int upsert(EventPlusDo protoDo) throws DalException {
      return QueryEngine.getInstance().insertSingle(
            EventPlusEntity.UPSERT,
            protoDo);
   }
   
   public int[] changeEventStateByPK(EventPlusDo[] protoDos, Updateset updateset) throws DalException {
      return QueryEngine.getInstance().updateBatch(
            EventPlusEntity.CHANGE_EVENT_STATE_BY_PK,
            protoDos,
            updateset);
   }
   
   public int updateByPK(EventPlusDo protoDo, Updateset updateset) throws DalException {
      return QueryEngine.getInstance().updateSingle(
            EventPlusEntity.UPDATE_BY_PK,
            protoDo,
            updateset);
   }
   
   public int changeEventStateByPK(EventPlusDo protoDo, Updateset updateset) throws DalException {
      return QueryEngine.getInstance().updateSingle(
            EventPlusEntity.CHANGE_EVENT_STATE_BY_PK,
            protoDo,
            updateset);
   }
   
   public int deleteByPK(EventPlusDo protoDo) throws DalException {
      return QueryEngine.getInstance().deleteSingle(
            EventPlusEntity.DELETE_BY_PK,
            protoDo);
   }
   
}
