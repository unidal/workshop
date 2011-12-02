package com.site.app.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrafficLimitFilter implements Filter {
   private List<Handler> m_handlers = new ArrayList<Handler>();

   public void destroy() {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
         ServletException {
      HttpServletRequest req = (HttpServletRequest) request;
      HttpServletResponse res = (HttpServletResponse) response;

      for (Handler handler : m_handlers) {
         handler.handle(req, res);
      }

      chain.doFilter(req, res);
   }

   public void init(FilterConfig config) throws ServletException {
      m_handlers.add(new HitsHandler());
   }
}
