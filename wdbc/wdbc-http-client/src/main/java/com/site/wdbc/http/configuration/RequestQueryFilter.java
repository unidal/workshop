package com.site.wdbc.http.configuration;

import java.util.Arrays;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.lookup.configuration.Configuration;
import com.site.wdbc.SelectQuery;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.impl.FormRequest;
import com.site.wdbc.query.WdbcFilter;

public class RequestQueryFilter {
   private Component m_request;

   private Component m_query;

   private Component m_filter;

   public RequestQueryFilter(String roleHint) {
      m_request = new Component(Request.class, roleHint, FormRequest.class);
      m_query = new Component(WdbcQuery.class, roleHint, SelectQuery.class);
   }

   public RequestQueryFilter(String roleHint, Class<? extends WdbcFilter> filterClass) {
      m_request = new Component(Request.class, roleHint, FormRequest.class);
      m_query = new Component(WdbcQuery.class, roleHint, SelectQuery.class).req(WdbcFilter.class, roleHint);
      m_filter = new Component(WdbcFilter.class, roleHint, filterClass);
   }

   public RequestQueryFilter fconfig(Configuration... children) {
      if (m_filter == null) {
         throw new IllegalArgumentException("No filter defined!");
      }

      m_filter.config(children);
      return this;
   }

   public RequestQueryFilter freq(Class<?>... roleClasses) {
      if (m_filter == null) {
         throw new IllegalArgumentException("No filter defined!");
      }

      m_filter.req(roleClasses);
      return this;
   }

   public List<Component> getComponents() {
      if (m_filter == null) {
         return Arrays.asList(m_request, m_query);
      } else {
         return Arrays.asList(m_request, m_query, m_filter);
      }
   }

   public RequestQueryFilter qconfig(Configuration... children) {
      m_query.config(children);
      return this;
   }

   public RequestQueryFilter qreq(Class<?>... roleClasses) {
      m_query.req(roleClasses);
      return this;
   }

   public RequestQueryFilter rconfig(Configuration... children) {
      m_request.config(children);
      return this;
   }

   public RequestQueryFilter rreq(Class<?>... roleClasses) {
      m_request.req(roleClasses);
      return this;
   }
}
