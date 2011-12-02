package com.site.wdbc.http.configuration;

import java.util.List;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;
import com.site.lookup.configuration.Configuration;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.http.Flow;
import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.impl.DefaultFlow;
import com.site.wdbc.http.impl.DefaultSession;
import com.site.wdbc.http.impl.FormRequest;
import com.site.wdbc.query.WdbcFilter;

public abstract class AbstractWdbcComponentsConfigurator extends AbstractResourceConfigurator {

   public static Component F() {
      return C(Flow.class, DefaultFlow.class);
   }

   public static Component P(Class<? extends Processor> processorClass) {
      return C(Processor.class, processorClass);
   }

   public static Configuration P(String name, String path) {
      return E("path", "name", name).value(path);
   }

   public static Component Q(String roleHint, Class<? extends WdbcQuery> queryClass) {
      return C(WdbcQuery.class, roleHint, queryClass);
   }

   public static Component R(String roleHint) {
      return C(Request.class, roleHint, FormRequest.class);
   }

   public static RequestQueryFilter RQ(String roleHint) {
      return new RequestQueryFilter(roleHint);
   }

   public static RequestQueryFilter RQF(String roleHint, Class<? extends WdbcFilter> filterClass) {
      return new RequestQueryFilter(roleHint, filterClass);
   }

   public static Component S() {
      return C(Session.class, DefaultSession.class);
   }

   public static List<Component> WDBC(Class<?> wdbcClass) {
      return new WdbcComponentsConfigurator(wdbcClass).defineComponents();
   }
}
