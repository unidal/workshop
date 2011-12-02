package com.site.bes.common.helpers;

import java.util.Date;

import com.site.bes.Event;
import com.site.bes.EventPayload;
import com.site.bes.EventPayloadException;
import com.site.bes.EventPayloadFormat;
import com.site.bes.EventState;
import com.site.kernel.SystemRegistry;

public class DefaultEvent implements Event {
   private Date m_creationDate;

   private int m_eventId;

   private EventPayload m_eventPayload;

   private EventState m_eventState;

   private String m_eventType;

   private int m_maxRetryTimes;

   private Date m_nextScheduleDate;

   private EventPayloadFormat m_payloadFormat;

   private String m_producerId;

   private String m_producerType;

   private String m_refId;

   private int m_retriedTimes;

   private String m_stringPayload;

   public Date getCreationDate() {
      return m_creationDate;
   }

   public int getEventId() {
      return m_eventId;
   }

   public EventPayload getEventPayload() throws EventPayloadException {
      return m_eventPayload;
   }

   public EventState getEventState() {
      return m_eventState;
   }

   public String getEventType() {
      return m_eventType;
   }

   public int getMaxRetryTimes() {
      return m_maxRetryTimes;
   }

   public Date getNextScheduleDate() {
      return m_nextScheduleDate;
   }

   public EventPayloadFormat getPayloadFormat() {
      return m_payloadFormat;
   }

   public String getProducerId() {
      return m_producerId;
   }

   public String getProducerType() {
      return m_producerType;
   }

   public String getRefId() {
      return m_refId;
   }

   public int getRetriedTimes() {
      return m_retriedTimes;
   }

   public String getStringPayload() {
      if (m_stringPayload == null && m_eventPayload != null) {
         EventPayloadFormatter formatter = (EventPayloadFormatter) SystemRegistry.newInstance(EventPayloadFormatter.class, m_payloadFormat);

         m_stringPayload = formatter.format(m_eventPayload);
      }

      return m_stringPayload;
   }

   public void setCreationDate(Date creationDate) {
      m_creationDate = creationDate;
   }

   public void setEventId(int eventId) {
      m_eventId = eventId;
   }

   public void setEventPayload(EventPayload eventPayload) {
      m_eventPayload = eventPayload;
   }

   public void setEventState(EventState eventState) {
      m_eventState = eventState;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setMaxRetryTimes(int maxRetryTimes) {
      m_maxRetryTimes = maxRetryTimes;
   }

   public void setNextScheduleDate(Date nextScheduleDate) {
      m_nextScheduleDate = nextScheduleDate;
   }

   public void setPayloadFormat(EventPayloadFormat payloadFormat) {
      m_payloadFormat = payloadFormat;
   }

   public void setProducerId(String producerId) {
      m_producerId = producerId;
   }

   public void setProducerType(String producerType) {
      m_producerType = producerType;
   }

   public void setRefId(String refId) {
      m_refId = refId;
   }

   public void setRetriedTimes(int retriedTimes) {
      m_retriedTimes = retriedTimes;
   }

   public void setStringPayload(String stringPayload) {
      m_stringPayload = stringPayload;
   }

}
