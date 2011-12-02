package com.site.bes.queue.memcache.dashboard;

import com.site.lookup.ComponentTestCase;

public class DashboardManagerTest extends ComponentTestCase {
   public void test() throws Exception {
      DashboardManager manager = lookup(DashboardManager.class);
      Dashboard dashboard = manager.getDashboard("testType", "testConsumer");

      assertEquals(0, dashboard.getLastEventId());
      assertEquals("testType", dashboard.getEventType());
      assertEquals("testConsumer", dashboard.getConsumerType());
   }
}
