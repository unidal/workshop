package com.site.wdbc.ebay.arch;

import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.cache.CacheManager;
import com.site.wdbc.http.cache.FileCacheManager;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;

class ComponentsConfigurator extends AbstractWdbcComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(Configuration.class) //
            .config(E("config").value("config.xml")));
      all.add(C(ImageHandler.class) //
            .req(Configuration.class, Session.class));
      all.add(C(ResultHandler.class) //
            .req(Configuration.class));

      all.add(C(CacheFilter.class));
      all.add(C(CacheManager.class, FileCacheManager.class)//
            .req(CacheFilter.class));

      return all;
   }
}
