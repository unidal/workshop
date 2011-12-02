package com.site.wdbc.ganji;

import com.site.lookup.ComponentTestCase;

public class ConfigurationTest extends ComponentTestCase {
   public void testConfiguration() throws Exception {
      Configuration configuration = lookup(Configuration.class);

      assertNotNull(configuration);
      assertEquals("http://www.xiaoxishu.com/XSpiderRobot", configuration.getAction());
      assertEquals(3, configuration.getMaxDays());
      assertEquals(2, configuration.getMaxPages());
      assertEquals("sh", configuration.getCityId());
   }
}
