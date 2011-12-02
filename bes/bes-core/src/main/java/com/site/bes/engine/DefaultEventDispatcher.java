package com.site.bes.engine;

import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.bes.Event;
import com.site.bes.EventException;
import com.site.bes.consumer.EventConsumer;
import com.site.bes.queue.EventQueue;

public class DefaultEventDispatcher implements EventDispatcher, LogEnabled {
   private String m_eventType;

   private EventQueue m_queue;

   private EventConsumer m_consumer;

   private long m_interval = 1000L;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public String getEventType() {
      return m_eventType;
   }

   private List<Event> pollEvents() {
      try {
         return m_queue.poll(m_eventType, m_consumer.getType(), m_consumer.getId());
      } catch (EventException e) {
         m_logger.error("Error when polling events of type(" + m_eventType + ") for consumer(" + m_consumer.getType()
               + ").", e);
      }

      return null;
   }

   private void processEvents(List<Event> events) {
      try {
         m_consumer.process(events);
      } catch (Exception e) {
         m_logger.error("Error when processing events of type(" + m_eventType + ") in consumer(" + m_consumer.getType()
               + ").", e);
      }
   }

   public void run() {
      while (true) {
         List<Event> events = pollEvents();

         if (events != null) {
            processEvents(events);
            update(events);
         }

         try {
            Thread.sleep(m_interval);
         } catch (InterruptedException e) {
            break;
         }
      }
   }

   public void setConsumer(EventConsumer consumer) {
      m_consumer = consumer;
   }

   public void setEventQueue(EventQueue queue) {
      m_queue = queue;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setInterval(long interval) {
      m_interval = interval;
   }

   private void update(List<Event> events) {
      try {
         m_queue.update(events, m_consumer.getType(), m_consumer.getId());
      } catch (Exception e) {
         m_logger.error("Error when updating events state of type(" + m_eventType + ") in consumer("
               + m_consumer.getType() + ").", e);
      }
   }
}
