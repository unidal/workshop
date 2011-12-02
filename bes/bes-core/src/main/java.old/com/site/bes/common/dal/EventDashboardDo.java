package com.site.bes.common.dal;

import com.site.kernel.dal.db.DataRow;

public class EventDashboardDo extends DataRow {
   private String m_eventType;
   private String m_consumerType;
   private int m_lastFetchedId;
   private java.util.Date m_lastScheduledDate;
   private long m_batchTimeout;
   private java.util.Date m_creationDate;
   private java.util.Date m_lastModifiedDate;

   private String m_keyEventType;
   private String m_keyConsumerType;

   static {
      init();
   }

   protected static void init() {
      initialize("event-dashboard", EventDashboardEntity.class, EventDashboardEntity.HINT_FIELDS);
   }

   protected EventDashboardDo() {
   }

   public long getBatchTimeout() {
      return m_batchTimeout;
   }
   
   public String getConsumerType() {
      return m_consumerType;
   }
   
   public java.util.Date getCreationDate() {
      return m_creationDate;
   }
   
   public String getEventType() {
      return m_eventType;
   }
   
   public String getKeyConsumerType() {
      return m_keyConsumerType;
   }
   
   public String getKeyEventType() {
      return m_keyEventType;
   }
   
   public int getLastFetchedId() {
      return m_lastFetchedId;
   }
   
   public java.util.Date getLastModifiedDate() {
      return m_lastModifiedDate;
   }
   
   public java.util.Date getLastScheduledDate() {
      return m_lastScheduledDate;
   }
   
   public void setBatchTimeout(long batchTimeout) {
      m_batchTimeout = batchTimeout;
      setFieldUsed(EventDashboardEntity.BATCH_TIMEOUT, true);
   }
   
   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
      setFieldUsed(EventDashboardEntity.CONSUMER_TYPE, true);
   }
   
   public void setCreationDate(java.util.Date creationDate) {
      m_creationDate = creationDate;
      setFieldUsed(EventDashboardEntity.CREATION_DATE, true);
   }
   
   public void setEventType(String eventType) {
      m_eventType = eventType;
      setFieldUsed(EventDashboardEntity.EVENT_TYPE, true);
   }
   
   public void setKeyConsumerType(String keyConsumerType) {
      m_keyConsumerType = keyConsumerType;
      setFieldUsed(EventDashboardEntity.KEY_CONSUMER_TYPE, true);
   }
   
   public void setKeyEventType(String keyEventType) {
      m_keyEventType = keyEventType;
      setFieldUsed(EventDashboardEntity.KEY_EVENT_TYPE, true);
   }
   
   public void setLastFetchedId(int lastFetchedId) {
      m_lastFetchedId = lastFetchedId;
      setFieldUsed(EventDashboardEntity.LAST_FETCHED_ID, true);
   }
   
   public void setLastModifiedDate(java.util.Date lastModifiedDate) {
      m_lastModifiedDate = lastModifiedDate;
      setFieldUsed(EventDashboardEntity.LAST_MODIFIED_DATE, true);
   }
   
   public void setLastScheduledDate(java.util.Date lastScheduledDate) {
      m_lastScheduledDate = lastScheduledDate;
      setFieldUsed(EventDashboardEntity.LAST_SCHEDULED_DATE, true);
   }
   
}
