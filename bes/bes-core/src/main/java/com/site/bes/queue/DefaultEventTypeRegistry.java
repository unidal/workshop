package com.site.bes.queue;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

public class DefaultEventTypeRegistry implements EventTypeRegistry, LogEnabled {
   private String m_defaultStorageType;

   private Map<String, String> m_map = new HashMap<String, String>();

   private Logger m_logger;

   public void register(String eventType) {
      register(eventType, m_defaultStorageType);
   }

   public void register(String eventType, String storageType) {
      m_logger.info("Register event type: " + eventType + ", storage: " + storageType);
      m_map.put(eventType, storageType);
   }

   public void setDefaultStorageType(String defaultStorageType) {
      m_defaultStorageType = defaultStorageType;
   }

   public String getStorageType(String eventType) {
      String storageType = m_map.get(eventType);

      if (storageType == null) {
         throw new RuntimeException("EventType(" + eventType + ") is not registered yet.");
      }

      return storageType;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
