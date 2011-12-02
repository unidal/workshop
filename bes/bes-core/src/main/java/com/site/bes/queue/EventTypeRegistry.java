package com.site.bes.queue;

public interface EventTypeRegistry {
   public void register(String eventType);

   public void register(String eventType, String storageType);

   public String getStorageType(String eventType);
}
