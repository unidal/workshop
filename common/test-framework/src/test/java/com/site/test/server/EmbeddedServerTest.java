package com.site.test.server;

import java.io.IOException;
import java.net.URL;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.mortbay.log.Log;

import com.site.lookup.ComponentTestCase;
import com.site.test.browser.Browser;

public class EmbeddedServerTest extends ComponentTestCase {
   public void testServer() throws Exception {
      Log.setLog(null);

      final EmbeddedServer server = EmbeddedServerManager.create(2000);
      final String message = "In method service() of test-servlet";

      server.addServlet(new GenericServlet() {
         private static final long serialVersionUID = 1L;

         @Override
         public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
            res.getWriter().write(message);
         }
      }, "test-servlet", "/*");
      server.start(1);

      final Browser browser = lookup(Browser.class, "memory");
      
      browser.display(new URL(server.getBaseUrl()));
      server.join();
      assertEquals(message, browser.toString());
   }
}
