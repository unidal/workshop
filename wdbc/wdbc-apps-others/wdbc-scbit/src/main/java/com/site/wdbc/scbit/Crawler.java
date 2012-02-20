package com.site.wdbc.scbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.http.Flow;

@RunWith(JUnit4.class)
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

   @Test
   public void execute() throws Exception {
      Flow flow = lookup(Flow.class);

      assertNotNull(flow);
      flow.execute();
   }
}
