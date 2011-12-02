package com.site.bes.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.site.bes.EventState;
import com.site.bes.common.dal.EventBatchLogDao;
import com.site.bes.common.dal.EventBatchLogDo;
import com.site.bes.common.dal.EventBatchLogEntity;
import com.site.bes.common.dal.EventDao;
import com.site.bes.common.dal.EventDo;
import com.site.bes.common.dal.EventEntity;
import com.site.bes.common.dal.EventPlusDao;
import com.site.bes.common.dal.EventPlusDo;
import com.site.kernel.dal.DalException;
import com.site.kernel.logging.Log;

public class EventRecoveryThread extends Thread {
   private static final Log s_log = Log.getLog(EventThread.class);

   private int m_timeout;

   public EventRecoveryThread(int timeout) {
      m_timeout = timeout;
      setName("EventRecoveryThread");
   }

   public void run() {
      Calendar cal = Calendar.getInstance();

      try {
         while (true) {
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.MILLISECOND, -m_timeout);

            List batches = EventBatchLogDao.getInstance().findAllUnprocessed(cal.getTime(), EventBatchLogEntity.READSET_FULL);

            for (int i = 0; i < batches.size(); i++) {
               EventBatchLogDo batch = (EventBatchLogDo) batches.get(i);

               rescheduleEvents(batch);
            }

            // Maximum of 30 seconds or half of timeout
            Thread.sleep(Math.max(30 * 1000l, m_timeout / 2));
         }
      } catch (InterruptedException e) {
         // ignore it
      } catch (DalException e) {
         s_log.log(e);
      }
   }

   private void rescheduleEvents(EventBatchLogDo batch) throws DalException {
      List events = EventDao.getInstance().findAllByIdRange(batch.getStartEventId(), batch.getEndEventId(), EventEntity.READSET_FETCH_EVENTS);
      List<EventPlusDo> list = new ArrayList<EventPlusDo>();

      for (int i = 0; i < events.size(); i++) {
         EventDo event = (EventDo) events.get(i);
         EventPlusDo e = EventPlusDao.getInstance().createLocal();

         e.setEventId(event.getEventId());
         e.setEventType(batch.getEventType());
         e.setConsumerType(batch.getConsumerType());
         e.setMaxRetryTimes(event.getMaxRetryTimes());
         e.setEventState(EventState.RESCHEDULE.getId());
         e.setRetriedTimes(0);
         e.setNextScheduleDate(new Date());

         list.add(e);
      }

      if (!list.isEmpty()) {
         EventPlusDo[] dos = new EventPlusDo[list.size()];

         list.toArray(dos);
         EventPlusDao.getInstance().upsert(dos);
      }

      batch.setKeyBatchId(batch.getBatchId());
      batch.setProcessStatus(EventProcessStatus.ALL_FAILURE.getId());
      batch.setFetchedRows(list.size());
      batch.setSuccessCount(0);
      batch.setFailureCount(list.size());

      EventBatchLogDao.getInstance().updateByPK(batch, EventBatchLogEntity.UPDATESET_EVENT_STAT);
   }
}
