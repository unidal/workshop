package com.site.wdbc.linkedin;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Flow;

public class FlowTest extends ComponentTestCase {
   public void testFlow() throws Exception {
      Flow flow = lookup(Flow.class, "linked-in");

      assertNotNull(flow);

      flow.execute();
   }
}
