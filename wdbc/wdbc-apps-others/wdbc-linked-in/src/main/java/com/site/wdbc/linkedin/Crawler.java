package com.site.wdbc.linkedin;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Flow;

public class Crawler extends ComponentTestCase {
   public static void main(String[] args) {
      Crawler crawler = new Crawler();

      try {
         crawler.setUp();
         crawler.execute();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void execute() throws Exception {
      Flow flow = lookup(Flow.class, "linked-in");

      assertNotNull(flow);
      flow.execute();
   }
}
