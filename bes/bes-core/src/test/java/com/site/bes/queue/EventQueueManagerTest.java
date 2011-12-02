package com.site.bes.queue;

import com.site.lookup.ComponentTestCase;

public class EventQueueManagerTest extends ComponentTestCase {
   public void test() throws Exception {
      EventQueueManager manager = lookup(EventQueueManager.class);
      EventTypeRegistry registry = lookup(EventTypeRegistry.class);

      registry.register("event.1");

      assertNotNull(manager.getEventQueueByStorageType("memcache"));
      assertNotNull(manager.getEventQueueByStorageType("mysql"));
      assertNotNull(manager.getEventQueueByEventType("event.1"));
   }
}
