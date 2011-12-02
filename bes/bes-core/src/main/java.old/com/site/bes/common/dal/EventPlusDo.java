package com.site.bes.common.dal;

import com.site.kernel.dal.db.DataRow;

public class EventPlusDo extends DataRow {
   private int m_eventId;
   private String m_consumerType;
   private String m_consumerId;
   private String m_eventType;
   private java.util.Date m_nextScheduleDate;
   private int m_maxRetryTimes;
   private int m_retriedTimes;
   private int m_eventState;
   private java.util.Date m_creationDate;
   private java.util.Date m_lastModifiedDate;

   private int m_keyEventId;
   private String m_keyConsumerType;

   static {
      init();
   }

   protected static void init() {
      initialize("event-plus", EventPlusEntity.class, EventPlusEntity.HINT_FIELDS);
   }

   protected EventPlusDo() {
   }

   public String getConsumerId() {
      return m_consumerId;
   }
   
   public String getConsumerType() {
      return m_consumerType;
   }
   
   public java.util.Date getCreationDate() {
      return m_creationDate;
   }
   
   public int getEventId() {
      return m_eventId;
   }
   
   public int getEventState() {
      return m_eventState;
   }
   
   public String getEventType() {
      return m_eventType;
   }
   
   public String getKeyConsumerType() {
      return m_keyConsumerType;
   }
   
   public int getKeyEventId() {
      return m_keyEventId;
   }
   
   public java.util.Date getLastModifiedDate() {
      return m_lastModifiedDate;
   }
   
   public int getMaxRetryTimes() {
      return m_maxRetryTimes;
   }
   
   public java.util.Date getNextScheduleDate() {
      return m_nextScheduleDate;
   }
   
   public int getRetriedTimes() {
      return m_retriedTimes;
   }
   
   public void setConsumerId(String consumerId) {
      m_consumerId = consumerId;
      setFieldUsed(EventPlusEntity.CONSUMER_ID, true);
   }
   
   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
      setFieldUsed(EventPlusEntity.CONSUMER_TYPE, true);
   }
   
   public void setCreationDate(java.util.Date creationDate) {
      m_creationDate = creationDate;
      setFieldUsed(EventPlusEntity.CREATION_DATE, true);
   }
   
   public void setEventId(int eventId) {
      m_eventId = eventId;
      setFieldUsed(EventPlusEntity.EVENT_ID, true);
   }
   
   public void setEventState(int eventState) {
      m_eventState = eventState;
      setFieldUsed(EventPlusEntity.EVENT_STATE, true);
   }
   
   public void setEventType(String eventType) {
      m_eventType = eventType;
      setFieldUsed(EventPlusEntity.EVENT_TYPE, true);
   }
   
   public void setKeyConsumerType(String keyConsumerType) {
      m_keyConsumerType = keyConsumerType;
      setFieldUsed(EventPlusEntity.KEY_CONSUMER_TYPE, true);
   }
   
   public void setKeyEventId(int keyEventId) {
      m_keyEventId = keyEventId;
      setFieldUsed(EventPlusEntity.KEY_EVENT_ID, true);
   }
   
   public void setLastModifiedDate(java.util.Date lastModifiedDate) {
      m_lastModifiedDate = lastModifiedDate;
      setFieldUsed(EventPlusEntity.LAST_MODIFIED_DATE, true);
   }
   
   public void setMaxRetryTimes(int maxRetryTimes) {
      m_maxRetryTimes = maxRetryTimes;
      setFieldUsed(EventPlusEntity.MAX_RETRY_TIMES, true);
   }
   
   public void setNextScheduleDate(java.util.Date nextScheduleDate) {
      m_nextScheduleDate = nextScheduleDate;
      setFieldUsed(EventPlusEntity.NEXT_SCHEDULE_DATE, true);
   }
   
   public void setRetriedTimes(int retriedTimes) {
      m_retriedTimes = retriedTimes;
      setFieldUsed(EventPlusEntity.RETRIED_TIMES, true);
   }
   
}
