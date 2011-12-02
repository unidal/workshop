package com.site.bes.engine.config;

import java.util.List;

import com.site.bes.EventConsumer;
import com.site.kernel.SystemInjection;
import com.site.kernel.SystemRegistry;

public class ListenOnBo extends ListenOnDo {

   static {
      init();
   }

   public void addEventBo(EventBo event) {
      addEventDo(event);
   }

   @SuppressWarnings("unchecked")
   public List<EventBo> getEventBos() {
      return (List<EventBo>) getEventDos();
   }

   public void startThread(ConsumerBo consumer, String eventType) {
      String consumerType = consumer.getType();
      EventConsumer eventConsumer = (EventConsumer) SystemRegistry.lookup(EventConsumer.class, consumerType);

      if (eventConsumer == null) {
         throw new RuntimeException("No consumer registered for type(" + consumerType + ")");
      } else {
         ConfigurationBo config = consumer.getConfigurationBo();

         if (config != null) {
            SystemInjection.injectProperties(eventConsumer, config.getNames(), config.getValues());
         }
      }

      for (EventBo event : getEventBos()) {
         if (eventType == null || event.getType().equals(eventType)) {
            event.startThread(consumer, eventConsumer);
         }
      }
   }

   public void stopThread(ConsumerBo consumer, String eventType) {
      for (EventBo event : getEventBos()) {
         if (eventType == null || event.getType().equals(eventType)) {
            event.stopThread(consumer);
         }
      }
   }

   public boolean isThreadRunning(String eventType) {
      for (EventBo event : getEventBos()) {
         if (event.getType().equals(eventType)) {
            return event.isThreadRunning();
         }
      }
      
      return false;
   }
}
