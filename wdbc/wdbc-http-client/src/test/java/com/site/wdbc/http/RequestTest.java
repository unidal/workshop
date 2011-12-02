package com.site.wdbc.http;

import com.site.lookup.ComponentTestCase;

public class RequestTest extends ComponentTestCase {
   public void test() throws Exception {
      Session session = lookup(Session.class);
      Request request = lookup(Request.class, "advanced-search");

      request.execute(session);
   }
}
