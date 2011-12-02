package com.site.test.browser;

public class DefaultBrowser extends AbstractBrowser {
   @Override
   public String[] getCommandLine(String url) {
      return new String[] { "rundll32", "url.dll,FileProtocolHandler", url };
   }

   public boolean isAvailable() {
      return true;
   }

   public BrowserType getId() {
      return BrowserType.DEFAULT;
   }
}
