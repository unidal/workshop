package com.site.wdbc.http.configuration;

import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.http.Handler;
import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.cache.CacheManager;
import com.site.wdbc.http.cache.NoCacheManager;
import com.site.wdbc.http.impl.DefaultHandler;
import com.site.wdbc.http.impl.DefaultProcessor;
import com.site.wdbc.http.impl.DefaultSession;
import com.site.wdbc.http.impl.FormRequest;

class ComponentsConfigurator extends AbstractWdbcComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(Session.class, DefaultSession.class) //
            .req(CacheManager.class) //
            .config(E("headers").add( //
                  E("header", "name", "User-Agent") //
                        .value("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; .NET CLR 1.1.4322)") //
                  )));
      all.add(C(FormRequest.class) //
            .is(PER_LOOKUP));
      all.add(C(Handler.class, DefaultHandler.class) //
            .is(PER_LOOKUP) //
            .req(WdbcEngine.class));
      all.add(C(CacheManager.class, NoCacheManager.class));
      all.add(C(Processor.class, DefaultProcessor.class));

      return all;
   }
}
