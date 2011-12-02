package com.site.wdbc;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.query.WdbcHandler;

public class WdbcHandlerTest extends ComponentTestCase {
   public void testLookup() throws Exception {
      WdbcHandler handler = lookup(WdbcHandler.class);

      assertNotNull(handler);
      assertNotNull(handler.getResult());
   }
}
