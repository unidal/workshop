package com.site.game.sanguo.thread.handler;

import org.junit.Test;

import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.lookup.ComponentTestCase;

public class TaskHandlerTest extends ComponentTestCase {
   private ThreadContext m_ctx = new ThreadContext();

   @Override
   public void setUp() throws Exception {
      super.setUp();

      lookup(ThreadHandler.class, "signin").handle(m_ctx);
      assertTrue(m_ctx.isSignedIn());

      lookup(ThreadHandler.class, "landing-page").handle(m_ctx);
      assertNotNull(m_ctx.getMainVillageId());
   }

   @Test
   public void testHandle() throws Exception {
      ThreadHandler handler = lookup(ThreadHandler.class, "task");

      handler.handle(m_ctx);
      assertNotNull(m_ctx.getGame());
   }
}
