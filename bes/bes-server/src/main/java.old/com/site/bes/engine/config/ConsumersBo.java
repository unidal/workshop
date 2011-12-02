package com.site.bes.engine.config;

import java.util.List;

public class ConsumersBo extends ConsumersDo {

   static {
      init();
   }

   public void addConsumerBo(ConsumerBo consumer) {
      addConsumerDo(consumer);
   }

   @SuppressWarnings("unchecked")
   public List<ConsumerBo> getConsumerBos() {
      return getConsumerDos();
   }

   public boolean isThreadRunning(String eventType, String consumerType) {
      List consumers = getConsumerBos();

      for (int i = 0; i < consumers.size(); i++) {
         ConsumerBo consumer = (ConsumerBo) consumers.get(i);

         if (consumer.getType().equals(consumerType)) {
            return consumer.isThreadRunning(eventType);
         }
      }
      
      return false;
   }

   public void startThread(String eventType, String consumerType) {
      List consumers = getConsumerBos();

      for (int i = 0; i < consumers.size(); i++) {
         ConsumerBo consumer = (ConsumerBo) consumers.get(i);

         if (consumerType == null && consumer.isEnabled()) {
            consumer.startThread(eventType);
         } else if (consumer.getType().equals(consumerType)) {
            consumer.startThread(eventType);
         }
      }
   }

   public void stopThread(String eventType, String consumerType) {
      List consumers = getConsumerBos();

      for (int i = 0; i < consumers.size(); i++) {
         ConsumerBo consumer = (ConsumerBo) consumers.get(i);

         if (consumerType == null || consumer.getType().equals(consumerType)) {
            consumer.stopThread(eventType);
         }
      }
   }
}
