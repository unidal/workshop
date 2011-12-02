package com.site.wdbc.useragent;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.FindQuery;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;

public class FindQueryTest extends ComponentTestCase {
   public void testList() throws Exception {
      FindQuery query = (FindQuery) lookup(WdbcQuery.class, "list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/useragents.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(426, result.getRowSize());
   }

}
