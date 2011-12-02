package com.site.bes.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.bes.consumer.annotation.Consumer;

public class DefaultEventConsumerRegistry implements EventConsumerRegistry, LogEnabled {
   private final List<EventConsumerListener> m_listeners = new ArrayList<EventConsumerListener>();

   private final Map<String, List<EventConsumer>> m_map = new LinkedHashMap<String, List<EventConsumer>>();

   private Logger m_logger;

   public void addListener(EventConsumerListener listener) {
      if (!m_listeners.contains(listener)) {
         m_listeners.add(listener);

         for (Map.Entry<String, List<EventConsumer>> entry : m_map.entrySet()) {
            String eventType = entry.getKey();
            List<EventConsumer> consumers = entry.getValue();

            for (EventConsumer consumer : consumers) {
               notifyListener(eventType, consumer, listener);
            }
         }
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public List<EventConsumer> getConsumers(final String eventType) {
      List<EventConsumer> consumers = m_map.get(eventType);

      if (consumers == null) {
         return Collections.emptyList();
      } else {
         return consumers;
      }
   }

   private void notifyListener(final String eventType, final EventConsumer consumer, EventConsumerListener listener) {
      try {
         listener.handleRegister(new EventConsumerEvent(eventType, consumer));
      } catch (RuntimeException e) {
         m_logger.warn("Error when handling event registering. EventType: " + eventType + ", consumer: " + consumer
               + ", listener: " + listener, e);
      }
   }

   private void notifyListeners(final String eventType, final EventConsumer consumer) {
      for (EventConsumerListener listener : m_listeners) {
         notifyListener(eventType, consumer, listener);
      }
   }

   public void register(final EventConsumer consumer) {
      register(null, consumer);
   }

   public void register(String eventType, final EventConsumer consumer) {
      Consumer annotation = consumer.getClass().getAnnotation(Consumer.class);

      if (annotation == null) {
         throw new RuntimeException(consumer.getClass() + " is not annontated by " + Consumer.class);
      }

      if (eventType == null) {
         eventType = annotation.defaultEventType();
      }

      List<EventConsumer> consumers = m_map.get(eventType);

      if (consumers == null) {
         consumers = new ArrayList<EventConsumer>(3);
         m_map.put(eventType, consumers);
      }

      if (!consumers.contains(consumer)) {
         m_logger.info("Registry consumer(" + consumer + ") for event type: " + eventType);
         consumers.add(consumer);
      }

      notifyListeners(eventType, consumer);
   }

   public void removeListener(EventConsumerListener listener) {
      m_listeners.remove(listener);
   }
}
