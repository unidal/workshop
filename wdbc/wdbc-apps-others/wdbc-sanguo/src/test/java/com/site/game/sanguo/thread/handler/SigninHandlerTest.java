package com.site.game.sanguo.thread.handler;

import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.lookup.ComponentTestCase;

public class SigninHandlerTest extends ComponentTestCase {
   public void testHandle() throws Exception {
      ThreadHandler handler = lookup(ThreadHandler.class, "signin");
      ThreadContext ctx = new ThreadContext();

      handler.handle(ctx);
      assertTrue(ctx.isSignedIn());
   }
}
