package com.site.wdbc.http;

import com.site.lookup.ComponentTestCase;

public class HandlerTest extends ComponentTestCase {
   public void testLookup() throws Exception {
      Handler handler1 = lookup(Handler.class);
      Handler handler2 = lookup(Handler.class);

      assertNotSame(handler1, handler2);
   }
}
