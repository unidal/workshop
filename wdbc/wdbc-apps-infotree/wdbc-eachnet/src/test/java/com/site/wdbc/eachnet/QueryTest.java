package com.site.wdbc.eachnet;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;

public class QueryTest extends ComponentTestCase {
   public void testList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "pages/list.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(21, result.getRowSize());
   }
//TODO
//   public void testNextPage() throws Exception {
//      WdbcQuery query = lookup(WdbcQuery.class, "next-page");
//      assertNotNull(query);
//
//      WdbcEngine engine = lookup(WdbcEngine.class);
//      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "pages/list.html", "utf-8");
//      WdbcResult result = engine.execute(query, source);
//
//      assertNotNull(result);
//      assertEquals(result.getRowSize(), 1);
//   }

   public void testDetails() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "details");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "pages/details.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testDescription() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "description");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "pages/description.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

}
