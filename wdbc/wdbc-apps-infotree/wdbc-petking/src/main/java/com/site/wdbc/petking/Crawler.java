package com.site.wdbc.petking;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Flow;

public class Crawler extends ComponentTestCase {
   public static void main(String[] args) {
      Crawler crawler = new Crawler();

      try {
         crawler.setUp();
         crawler.execute();
         crawler.tearDown();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void execute() throws Exception {
      Flow flow = lookup(Flow.class);

      assertNotNull(flow);
      flow.execute();
   }
}
