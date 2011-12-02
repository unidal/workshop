package com.site.bes.queue.memcache.dashboard;

import com.site.bes.queue.memcache.Formatter;

public class DefaultDashboard implements Dashboard {
   private Formatter<Dashboard, Dashboard> m_formatter;
   
   private String m_eventType;

   private String m_consumerType;

   private int m_lastEventId;

   public String getConsumerType() {
      return m_consumerType;
   }

   public String getEventType() {
      return m_eventType;
   }

   public int getLastEventId() {
      return m_lastEventId;
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setLastEventId(int lastEventId) {
      m_lastEventId = lastEventId;
   }

   public String getId() {
      return m_formatter.getId(this);
   }

   public String getValue() {
      return m_formatter.getValue(this);
   }
}
