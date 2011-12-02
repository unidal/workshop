package com.site.bes.consumer;

import java.util.List;

public interface EventConsumerRegistry {
   public void register(EventConsumer consumer);

   public void register(String eventType, EventConsumer consumer);

   public void addListener(EventConsumerListener listener);

   public void removeListener(EventConsumerListener listener);

   public List<EventConsumer> getConsumers(String eventType);
}
