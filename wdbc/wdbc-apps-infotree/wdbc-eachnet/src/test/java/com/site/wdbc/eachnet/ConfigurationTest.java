package com.site.wdbc.eachnet;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.eachnet.Configuration;

public class ConfigurationTest extends ComponentTestCase {
   public void testConfiguration() throws Exception {
      Configuration configuration = lookup(Configuration.class);

      assertNotNull(configuration);
      assertEquals("http://www.xiaoxishu.com/XSpiderRobot", configuration.getAction());
      assertEquals(3, configuration.getMaxDays());
      assertEquals(30, configuration.getMaxPages());
      assertEquals("sh", configuration.getCityId());
      assertEquals("�Ϻ���", configuration.getCityName());
      assertEquals("�Ϻ�", configuration.getProvinceName());
   }
}
