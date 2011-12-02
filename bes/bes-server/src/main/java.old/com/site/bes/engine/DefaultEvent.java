package com.site.bes.engine;

import java.util.Date;

import com.site.bes.Event;
import com.site.bes.EventPayload;
import com.site.bes.EventPayloadException;
import com.site.bes.EventPayloadFormat;
import com.site.bes.EventState;
import com.site.bes.common.dal.EventDo;
import com.site.bes.common.helpers.EventPayloadFormatter;
import com.site.kernel.SystemRegistry;

public class DefaultEvent implements Event {
   private EventDo m_event;

   private EventStatistics m_statistics;

   private EventState m_state;

   private long m_startWait;

   private long m_startProcess;

   private int m_retriedTimes;

   private Date m_nextScheduleDate;

   public DefaultEvent(EventDo event, EventStatistics statistics) {
      m_event = event;
      m_state = EventState.PROCESSING;
      m_statistics = statistics;
      m_startWait = System.currentTimeMillis();
   }

   public Date getCreationDate() {
      return m_event.getCreationDate();
   }

   public int getEventId() {
      return m_event.getEventId();
   }

   public String getStringPayload() {
      return m_event.getPayload();
   }

   public EventPayload getEventPayload() {
      EventPayload payload = (EventPayload) SystemRegistry.newInstance(EventPayload.class, m_event.getEventType());
      EventPayloadFormat format = EventPayloadFormat.getById(m_event.getPayloadType());

      if (format == EventPayloadFormat.CUSTOM) {
         // Do nothing here, let application to parse the payload
      } else if (format != null) {
         EventPayloadFormatter formatter = (EventPayloadFormatter) SystemRegistry.newInstance(EventPayloadFormatter.class, format);

         formatter.parse(payload, m_event.getPayload());
      } else {
         throw new EventPayloadException("Unknown payload format. payload type: " + m_event.getPayloadType() + ", event id: " + m_event.getEventId());
      }

      return payload;
   }

   public EventState getEventState() {
      return m_state;
   }

   public String getEventType() {
      return m_event.getEventType();
   }

   public int getMaxRetryTimes() {
      return m_event.getMaxRetryTimes();
   }

   public Date getNextScheduleDate() {
      return m_nextScheduleDate;
   }

   public String getProducerId() {
      return m_event.getProducerId();
   }

   public String getProducerType() {
      return m_event.getProducerType();
   }

   public String getRefId() {
      return m_event.getRefId();
   }

   public int getRetriedTimes() {
      return m_retriedTimes;
   }

   public void setEventState(EventState state) {
      if (m_startProcess <= 0) {
         throw new RuntimeException("startProcess() method should be called before event is consumed");
      }

      long end = System.currentTimeMillis();

      m_state = state;
      m_statistics.addWaitTime(end - m_startWait);
      m_statistics.addProcessTime(end - m_startProcess);
      m_statistics.calculateEventState(state);
   }

   public void setNextScheduleDate(Date date) {
      m_nextScheduleDate = date;
   }

   void startProcess() {
      m_startProcess = System.currentTimeMillis();
   }

}
