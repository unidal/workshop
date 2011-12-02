package com.site.bes.queue;

import com.site.lookup.ContainerHolder;

public class DefaultEventQueueManager extends ContainerHolder implements EventQueueManager {
   private EventTypeRegistry m_registry;

   public EventQueue getEventQueueByStorageType(String storageType) {
      return lookup(EventQueue.class, storageType);
   }

   public EventQueue getEventQueueByEventType(String eventType) {
      String storageType = m_registry.getStorageType(eventType);

      return getEventQueueByStorageType(storageType);
   }
}
