package com.site.test.browser;

import java.io.File;

import com.site.test.env.Platform;

public class InternetExplorerBrowser extends AbstractBrowser {
   private File getInstallPath() {
      return Platform.getProgramFile("Internet Explorer/iexplore.exe");
   }

   @Override
   public String[] getCommandLine(String url) {
      return new String[] { getInstallPath().toString(), url };
   }

   public boolean isAvailable() {
      return getInstallPath().exists();
   }

   public BrowserType getId() {
      return BrowserType.INTERNET_EXPLORER;
   }
}
