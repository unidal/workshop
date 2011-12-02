package com.site.game.sanguo.thread.handler.question;

import com.site.lookup.ComponentTestCase;

public class KillAnswererTest extends ComponentTestCase {
   public void testAnswer() throws Exception {
      Answerer answerer = lookup(Answerer.class, "kill");

      assertTrue(answerer.canAnswer("于糜 被何人所杀"));
      assertEquals(3, answerer.answer("于糜 被何人所杀", new String[] { "关兴", "姜维", "孙策", "傅佥" }));
   }
}
