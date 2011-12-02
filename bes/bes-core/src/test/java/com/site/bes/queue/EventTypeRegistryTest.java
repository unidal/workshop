package com.site.bes.queue;

import com.site.lookup.ComponentTestCase;

public class EventTypeRegistryTest extends ComponentTestCase {
   public void test() throws Exception {
      EventTypeRegistry registry = lookup(EventTypeRegistry.class);

      registry.register("type.1");
      registry.register("type.2", "mysql");
      registry.register("type.3", "memcache");

      assertNotNull(registry.getStorageType("type.1"));
      assertNotNull(registry.getStorageType("type.2"));
      assertNotNull(registry.getStorageType("type.3"));
      assertEquals("mysql", registry.getStorageType("type.2"));
      assertEquals("memcache", registry.getStorageType("type.3"));
   }
}
