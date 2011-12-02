package com.site.dal.jdbc.mapping;

import com.site.lookup.ComponentTestCase;

public class TableProviderTest extends ComponentTestCase {
   public void testLookup() throws Exception {
      TableProvider provider = lookup(TableProvider.class, "user");

      assertNotNull(provider);
   }
}
