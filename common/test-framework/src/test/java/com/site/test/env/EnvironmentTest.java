package com.site.test.env;

import com.site.lookup.ComponentTestCase;

public class EnvironmentTest extends ComponentTestCase {
   public void testPlatform() {
      System.out.println("Current platform: " + System.getProperty("os.name"));
   }
}
