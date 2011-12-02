package com.site.bes.consumer;

public class EventConsumerEvent {
   private String m_eventType;

   private EventConsumer m_consumer;

   public EventConsumerEvent(String eventType, EventConsumer consumer) {
      m_eventType = eventType;
      m_consumer = consumer;
   }

   public String getEventType() {
      return m_eventType;
   }

   public EventConsumer getConsumer() {
      return m_consumer;
   }
}
