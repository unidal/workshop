package org.unidal.weather;

import org.codehaus.plexus.PlexusContainer;
import org.junit.Test;
import org.mortbay.servlet.GzipFilter;

import com.site.test.junit.HttpTestCase;
import com.site.test.server.EmbeddedServer;
import com.site.web.MVC;

public class TestServer extends HttpTestCase {
   public static void main(String[] args) throws Exception {
      main(new TestServer());
   }

   @Override
   protected void configure(EmbeddedServer server) {
      PlexusContainer container = getContainer();
      MVC mvc = new MVC();

      mvc.setContainer(container);
      server.addServlet(mvc, "mvc-servlet", "/mvc/*");
      server.addFilter(GzipFilter.class, "gzip-filter", "/mvc/weather/*");
   }

   @Override
   protected boolean logEnabled() {
      return true;
   }

   @Test
   public void test() {
      // dummy
   }
}
