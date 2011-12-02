package com.site.wdbc.linkedin;

import org.codehaus.plexus.util.IOUtil;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Response;
import com.site.wdbc.http.Session;

public class RequestTest extends ComponentTestCase {
   public void testLogin() throws Exception {
      Session session = lookup(Session.class);
      Request request = lookup(Request.class, "login");

      assertNotNull(session);
      assertNotNull(request);

      Response response = request.execute(session);
      String key = "<title>Redirecting...</title>";

      assertTrue(IOUtil.toString(response.getContent(), response.getCharset()).contains(key));
   }

   public void testSearch() throws Exception {
      Session session = lookup(Session.class);
      Request request = lookup(Request.class, "search");

      assertNotNull(session);
      assertNotNull(request);

      Response response = request.execute(session);
      String key = "<title>LinkedIn: People Search Results</title>";

      assertTrue(IOUtil.toString(response.getContent(), response.getCharset()).contains(key));
   }

   public void testList() throws Exception {
      Session session = lookup(Session.class);
      Request request = lookup(Request.class, "list");

      assertNotNull(session);
      assertNotNull(request);

      Response response = request.execute(session);
      String key = "<title>LinkedIn: People Search Results</title>";

      assertTrue(IOUtil.toString(response.getContent(), response.getCharset()).contains(key));
   }

   public void testDetails() throws Exception {
      Session session = lookup(Session.class);
      Request request = lookup(Request.class, "details");

      assertNotNull(session);
      assertNotNull(request);

      Response response = request.execute(session);
      String key = "<title>LinkedIn: ";

      assertTrue(IOUtil.toString(response.getContent(), response.getCharset()).contains(key));
   }
}
