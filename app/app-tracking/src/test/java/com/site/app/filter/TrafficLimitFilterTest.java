package com.site.app.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.servlet.ServletHandler;

import com.site.lookup.ComponentTestCase;

public class TrafficLimitFilterTest extends ComponentTestCase {
   public void testFilter() throws Exception {
      Server server = new Server(80);
      ServletHandler handler = new ServletHandler();

      handler.addFilterWithMapping(TrafficLimitFilter.class, "/tl/*", 1);
      handler.addServletWithMapping(SimpleServlet.class, "/tl/*");

      server.addHandler(handler);
      server.addHandler(new DefaultHandler());
      server.start();
      server.join();
   }

   public static final class SimpleServlet extends HttpServlet {
      private static final long serialVersionUID = 1L;

      @Override
      public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
         res.setContentType("text/html");
         res.getWriter().println("hits: " + req.getAttribute(HitsHandler.LAST_FIVE_MINUTES_HITS));
      }
   }
}
