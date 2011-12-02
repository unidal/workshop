package com.site.game.sanguo;

import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.engine.ThreadEngine;
import com.site.lookup.ComponentTestCase;

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
      ThreadEngine engine = lookup(ThreadEngine.class);
      ThreadContext ctx = new ThreadContext();

      engine.execute(ctx);
   }
}