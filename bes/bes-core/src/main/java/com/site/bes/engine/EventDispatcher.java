package com.site.bes.engine;

import com.site.bes.consumer.EventConsumer;
import com.site.bes.queue.EventQueue;

public interface EventDispatcher extends Runnable {
   public void setConsumer(EventConsumer consumer);

   public void setEventQueue(EventQueue queue);

   public void setEventType(String eventType);

   public String getEventType();
}
