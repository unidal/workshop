package com.site.test;

import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;
import com.site.test.browser.Browser;
import com.site.test.browser.BrowserManager;
import com.site.test.browser.BrowserType;
import com.site.test.browser.ConsoleBrowser;
import com.site.test.browser.DefaultBrowser;
import com.site.test.browser.FirefoxBrowser;
import com.site.test.browser.InternetExplorerBrowser;
import com.site.test.browser.MemoryBrowser;
import com.site.test.browser.OperaBrowser;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(BrowserManager.class));
      all.add(C(Browser.class, BrowserType.DEFAULT.getId(), DefaultBrowser.class));
      all.add(C(Browser.class, BrowserType.MEMORY.getId(), MemoryBrowser.class).is(PER_LOOKUP));
      all.add(C(Browser.class, BrowserType.CONSOLE.getId(), ConsoleBrowser.class));
      all.add(C(Browser.class, BrowserType.FIREFOX.getId(), FirefoxBrowser.class));
      all.add(C(Browser.class, BrowserType.INTERNET_EXPLORER.getId(), InternetExplorerBrowser.class));
      all.add(C(Browser.class, BrowserType.OPERA.getId(), OperaBrowser.class));

      return all;
   }
}
