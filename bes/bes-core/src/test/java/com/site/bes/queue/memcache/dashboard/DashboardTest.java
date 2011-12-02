package com.site.bes.queue.memcache.dashboard;

import com.site.lookup.ComponentTestCase;

public class DashboardTest extends ComponentTestCase {
   public void test() throws Exception {
      Dashboard dashboard = lookup(Dashboard.class);

      dashboard.setEventType("testType");
      dashboard.setConsumerType("testConsumer");
      dashboard.setLastEventId(12345);

      assertEquals(12345, dashboard.getLastEventId());
      assertEquals("d-testType-testConsumer", dashboard.getId());
      assertEquals("12345", dashboard.getValue());
   }
}
