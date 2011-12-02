package com.site.game.sanguo.thread.handler;

import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.lookup.ComponentTestCase;

public class QuestionHandlerTest extends ComponentTestCase {
   public void testHandle() throws Exception {
      ThreadHandler handler = lookup(ThreadHandler.class, "question");
      ThreadContext ctx = new ThreadContext();

      handler.handle(ctx);
   }
}
