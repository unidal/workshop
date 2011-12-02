package com.site.wdbc.state;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Flow;

public class StateFlowTest extends ComponentTestCase {
   public void testFlow() throws Exception {
      Flow flow = lookup(Flow.class, "us-states");

      assertNotNull(flow);

      flow.execute();
   }
}
