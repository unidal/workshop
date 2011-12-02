package com.site.bes.queue;

public interface EventQueueManager {
   public EventQueue getEventQueueByStorageType(String storageType);

   public EventQueue getEventQueueByEventType(String eventType);
}
