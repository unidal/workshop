package com.site.bes.queue.memcache.block;

import com.site.bes.Event;
import com.site.bes.queue.memcache.Cache;
import com.site.lookup.ComponentTestCase;

public class BlockManagerTest extends ComponentTestCase {
   @Override
   protected void setUp() throws Exception {
      super.setUp();

      Cache cache = lookup(Cache.class);

      cache.clearAll();
   }

   public void test() throws Exception {
      BlockManager manager = lookup(BlockManager.class);
      Event event = new Event();

      manager.add(event);
      manager.add(event);

      assertTrue(event.getId() > 0);
      assertEquals(event.getId() - 1, manager.getEvents(0).get(0).getId());
      assertEquals(event.getId(), manager.getEvents(0).get(1).getId());
   }
}
