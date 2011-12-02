package com.site.bes.server.admin.type;

import java.util.Date;

import com.site.bes.EventState;

public final class ConsumptionType {
   private int m_eventId;

   private String m_eventType;

   private String m_consumerType;

   private String m_consumerId;

   private EventState m_eventState;

   private int m_retriedTimes;

   private Date m_nextScheduleDate;

   public String getConsumerId() {
      return m_consumerId;
   }

   public String getConsumerType() {
      return m_consumerType;
   }

   public int getEventId() {
      return m_eventId;
   }

   public EventState getEventState() {
      return m_eventState;
   }

   public String getEventType() {
      return m_eventType;
   }

   public Date getNextScheduleDate() {
      return m_nextScheduleDate;
   }

   public int getRetriedTimes() {
      return m_retriedTimes;
   }

   public void setConsumerId(String consumerId) {
      m_consumerId = consumerId;
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
   }

   public void setEventId(int eventId) {
      m_eventId = eventId;
   }

   public void setEventState(EventState eventState) {
      m_eventState = eventState;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setNextScheduleDate(Date nextScheduleDate) {
      m_nextScheduleDate = nextScheduleDate;
   }

   public void setRetriedTimes(int retriedTimes) {
      m_retriedTimes = retriedTimes;
   }

}