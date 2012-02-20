package com.site.wdbc.scbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;

@RunWith(JUnit4.class)
public class FindQueryTest extends ComponentTestCase {
   @Test
   public void testSummary() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "summary");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "/pages/summary.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }
}
