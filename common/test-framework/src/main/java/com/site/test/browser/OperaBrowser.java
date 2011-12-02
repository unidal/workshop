package com.site.test.browser;

import java.io.File;

import com.site.test.env.Platform;

public class OperaBrowser extends AbstractBrowser {
   private File getInstallPath() {
      return Platform.getProgramFile("opera/opera.exe");
   }

   @Override
   public String[] getCommandLine(String url) {
      return new String[] { getInstallPath().toString(), url };
   }

   public boolean isAvailable() {
      return getInstallPath().exists();
   }

   public BrowserType getId() {
      return BrowserType.OPERA;
   }
}
