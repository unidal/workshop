package com.site.wdbc.linkedin;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;

public class QueryTest extends ComponentTestCase {
   public void testPageLinks() throws Exception {
      WdbcEngine engine = lookup(WdbcEngine.class);
      assertNotNull(engine);

      WdbcQuery query = lookup(WdbcQuery.class, "page-links");
      assertNotNull(query);

      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(14, result.getRowSize());
   }

   public void testResultSet() throws Exception {
      WdbcEngine engine = lookup(WdbcEngine.class);
      assertNotNull(engine);

      WdbcQuery query = lookup(WdbcQuery.class, "result-set");
      assertNotNull(query);

      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(10, result.getRowSize());
   }

   public void testProfile() throws Exception {
      WdbcEngine engine = lookup(WdbcEngine.class);
      assertNotNull(engine);

      WdbcQuery query = lookup(WdbcQuery.class, "profile");
      assertNotNull(query);

      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/details.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testJobs() throws Exception {
      WdbcEngine engine = lookup(WdbcEngine.class);
      assertNotNull(engine);

      WdbcQuery query = lookup(WdbcQuery.class, "jobs");
      assertNotNull(query);

      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/details.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(9, result.getRowSize());
   }

   public void testJobs2() throws Exception {
      WdbcEngine engine = lookup(WdbcEngine.class);
      assertNotNull(engine);

      WdbcQuery query = lookup(WdbcQuery.class, "jobs");
      assertNotNull(query);

      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/details2.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }

   public void testStateTitle() throws Exception {
      WdbcEngine engine = lookup(WdbcEngine.class);
      assertNotNull(engine);

      WdbcQuery query = lookup(WdbcQuery.class, "state-title");
      assertNotNull(query);

      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/details.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertTrue(result.getRowSize() > 0);
   }
}
