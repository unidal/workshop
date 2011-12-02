package com.site.game.sanguo.thread.handler.question;

import com.site.lookup.ComponentTestCase;

public class NameAnswererTest extends ComponentTestCase {
   public void testAnswer() throws Exception {
      Answerer answerer = lookup(Answerer.class, "name");

      assertTrue(answerer.canAnswer("曹仁  的字是什么?"));
      assertEquals(2, answerer.answer("曹仁  的字是什么?", new String[] { "字文若", "字子孝", "字伯符", "字子阳" }));
   }
}
