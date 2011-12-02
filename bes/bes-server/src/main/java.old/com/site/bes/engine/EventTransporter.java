package com.site.bes.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.site.bes.EventBatch;
import com.site.bes.EventRetryPolicy;
import com.site.bes.EventState;
import com.site.bes.common.dal.EventBatchLogDao;
import com.site.bes.common.dal.EventBatchLogDo;
import com.site.bes.common.dal.EventBatchLogEntity;
import com.site.bes.common.dal.EventDao;
import com.site.bes.common.dal.EventDo;
import com.site.bes.common.dal.EventEntity;
import com.site.bes.common.dal.EventPlusDao;
import com.site.bes.common.dal.EventPlusDo;
import com.site.bes.common.dal.EventPlusEntity;
import com.site.bes.common.helpers.DefaultEventRetryPolicy;
import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.Ref;

public class EventTransporter {
   private String m_eventType;

   private String m_consumerType;

   private String m_consumerId;

   private EventRetryPolicy m_retryPolicy = new DefaultEventRetryPolicy();

   public void collectFailedEvents(EventBatch batch) throws DalException {
      List<EventPlusDo> list = new ArrayList<EventPlusDo>(batch.length());

      batch.reset(); // goto header

      while (batch.hasNext()) {
         DefaultEvent event = (DefaultEvent) batch.next();
         EventState state = event.getEventState();

         if (state != EventState.COMPLETED && state != EventState.SKIPPED) {
            list.add(getEventPlusDo(event));
         }
      }

      if (!list.isEmpty()) {
         EventPlusDo[] events = new EventPlusDo[list.size()];

         list.toArray(events);
         EventPlusDao.getInstance().upsert(events);
      }
   }

   public void fetchEvents(EventBatch batch, EventStatistics statistics) throws DalException {
      long start = System.currentTimeMillis();
      Ref batchId = new Ref();
      List events = EventDao.getInstance().fetchEvents(m_eventType, m_consumerType, m_consumerId, batchId, EventEntity.READSET_FETCH_EVENTS);
      int size = events.size();

      for (int i = 0; i < size; i++) {
         EventDo event = (EventDo) events.get(i);

         event.setEventType(m_eventType);
         batch.add(new DefaultEvent(event, statistics));
      }

      statistics.setFetchTime(System.currentTimeMillis() - start);
      statistics.setFetchedRows(batch.length());
      statistics.setBatchId(((Integer) batchId.get()).intValue());
   }

   public void fetchScheduleEvents(EventBatch batch, EventStatistics statistics) throws DalException {
      long start = System.currentTimeMillis();
      Calendar cal = Calendar.getInstance();
      Date endScheduleDate = cal.getTime();

      cal.add(Calendar.DATE, -1); // a day ago
      Date startScheduleDate = cal.getTime();

      List events = EventDao.getInstance().fetchScheduleEvents(m_eventType, m_consumerType, startScheduleDate, endScheduleDate, EventEntity.READSET_FETCH_EVENTS);
      int size = events.size();

      if (size > 0) {
         EventPlusDo[] dos = new EventPlusDo[size];

         for (int i = 0; i < size; i++) {
            EventDo event = (EventDo) events.get(i);

            dos[i] = EventPlusDao.getInstance().createLocal();
            dos[i].setKeyEventId(event.getEventId());
            dos[i].setConsumerId(m_consumerId);
            dos[i].setEventState(EventState.PROCESSING.getId());
         }

         int[] flags = EventPlusDao.getInstance().changeEventStateByPK(dos, EventPlusEntity.UPDATESET_EVENT_STATE);

         for (int i = 0; i < size; i++) {
            EventDo event = (EventDo) events.get(i);

            if (flags[i] == 1) {
               event.setEventType(m_eventType);
               batch.add(new DefaultEvent(event, statistics));
            }
         }
      }

      statistics.setFetchTime(System.currentTimeMillis() - start);
      statistics.setFetchedRows(batch.length());
   }

   private EventPlusDo getEventPlusDo(DefaultEvent event) {
      EventPlusDo e = EventPlusDao.getInstance().createLocal();
      EventState state = event.getEventState();

      e.setEventId(event.getEventId());
      e.setEventType(event.getEventType());
      e.setConsumerType(m_consumerType);
      e.setConsumerId(m_consumerId);
      e.setMaxRetryTimes(event.getMaxRetryTimes());

      if (state == EventState.PROCESSING) {
         state = EventState.RESCHEDULE;
      }

      e.setEventState(state.getId());
      if (state == EventState.RESCHEDULE || state == EventState.RETRY) {
         Date nextScheduleDate = m_retryPolicy.getNextScheduleDate(event);

         e.setNextScheduleDate(nextScheduleDate);
      } else {
         e.setNextScheduleDate(null);
      }
      return e;
   }

   public void setConsumerId(String consumerId) {
      m_consumerId = consumerId;
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
   }

   public void setEventRetryPolicy(EventRetryPolicy retryPolicy) {
      if (retryPolicy != null) {
         m_retryPolicy = retryPolicy;
      }
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void updateBatchLog(EventStatistics stat) throws DalException {
      EventBatchLogDo batchLog = EventBatchLogDao.getInstance().createLocal();

      batchLog.setKeyBatchId(stat.getBatchId());
      batchLog.setFetchedRows(stat.getFetchedRows());
      batchLog.setSuccessCount(stat.getSuccessCount());
      batchLog.setFailureCount(stat.getFailureCount());
      batchLog.setFetchTime((int) stat.getFetchTime());
      batchLog.setMaxWaitTime((int) stat.getMaxWaitTime());
      batchLog.setAvgWaitTime((int) stat.getAvgWaitTime());
      batchLog.setAvgProcessTime((int) stat.getAvgProcessTime());
      batchLog.setMaxProcessTime((int) stat.getMaxProcessTime());
      batchLog.setProcessStatus(stat.getProcessStatus().getId());

      EventBatchLogDao.getInstance().updateByPK(batchLog, EventBatchLogEntity.UPDATESET_EVENT_STAT);
   }

   public void updateEvents(EventBatch batch) throws DalException {
      List<EventPlusDo> list = new ArrayList<EventPlusDo>(batch.length());

      batch.reset(); // goto header

      while (batch.hasNext()) {
         DefaultEvent event = (DefaultEvent) batch.next();

         list.add(getEventPlusDo(event));
      }

      if (!list.isEmpty()) {
         EventPlusDo[] events = new EventPlusDo[list.size()];

         list.toArray(events);
         EventPlusDao.getInstance().upsert(events);
      }
   }

}
