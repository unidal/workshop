package com.site.kernel;

import java.io.File;

import com.site.kernel.common.BaseTestCase;

public class SystemPathFinderTest extends BaseTestCase {
   public void testGetAppRoot() {
      File appRoot = SystemPathFinder.getAppRoot();

      out("Application root: " + appRoot);
   }

   public void testGetConfigDir() {
      File configDir = SystemPathFinder.getAppConfig();

      out("Config dir: " + configDir);
   }
}
