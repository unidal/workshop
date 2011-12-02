package com.site.dal.jdbc.query;

import com.site.lookup.ComponentTestCase;

public class QueryResolverTest extends ComponentTestCase {
   public void testResolve() throws Exception {
      QueryResolver resolver = lookup(QueryResolver.class, "MySql");
      assertNotNull(resolver);
   }
}
