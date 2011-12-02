package com.site.wdbc.sosoko;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Flow;

public class FlowTest extends ComponentTestCase {
   public void testFlow() throws Exception {
      Flow flow = lookup(Flow.class);

      assertNotNull(flow);

      flow.execute();
   }
}
