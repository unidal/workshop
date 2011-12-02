package com.site.bes.server.admin.type;

import java.util.List;

public class EventType {
   private String m_eventType;

   private List<ConsumerType> m_consumerList;

   public List<ConsumerType> getConsumerList() {
      return m_consumerList;
   }

   public String getEventType() {
      return m_eventType;
   }

   public void setConsumerList(List<ConsumerType> consumerList) {
      m_consumerList = consumerList;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }
}
