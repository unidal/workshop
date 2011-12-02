package com.site.bes.server.admin.type;

import java.util.Date;

public class DashboardType {
   private String m_eventType;

   private String m_consumerType;

   private int m_lastFetchedId;

   private Date m_lastScheduledDate;

   private long m_batchTimeout;

   private Date m_creationDate;

   private Date m_lastModifiedDate;

   private boolean m_running;
   
   public long getBatchTimeout() {
      return m_batchTimeout;
   }

   public String getConsumerType() {
      return m_consumerType;
   }

   public Date getCreationDate() {
      return m_creationDate;
   }

   public String getEventType() {
      return m_eventType;
   }

   public int getLastFetchedId() {
      return m_lastFetchedId;
   }

   public Date getLastModifiedDate() {
      return m_lastModifiedDate;
   }

   public Date getLastScheduledDate() {
      return m_lastScheduledDate;
   }

   public boolean isRunning() {
      return m_running;
   }

   public void setBatchTimeout(long batchTimeout) {
      m_batchTimeout = batchTimeout;
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
   }

   public void setCreationDate(Date creationDate) {
      m_creationDate = creationDate;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setLastFetchedId(int lastFetchedId) {
      m_lastFetchedId = lastFetchedId;
   }

   public void setLastModifiedDate(Date lastModifiedDate) {
      m_lastModifiedDate = lastModifiedDate;
   }

   public void setLastScheduledDate(Date lastScheduledDate) {
      m_lastScheduledDate = lastScheduledDate;
   }

   public void setRunning(boolean running) {
      m_running = running;
   }

}
