package com.site.game.sanguo.thread.handler.question;

import com.site.lookup.ComponentTestCase;

public class ChapterAnswererTest extends ComponentTestCase {
   public void testAnswer() throws Exception {
      Answerer answerer = lookup(Answerer.class, "chapter");

      assertTrue(answerer.canAnswer("屯土山关公约三事 救白马曹操解重围 是三国演义第几回"));
      assertEquals(2, answerer.answer("屯土山关公约三事 救白马曹操解重围 是三国演义第几回", new String[] { "24", "25", "26", "27" }));
   }
}
