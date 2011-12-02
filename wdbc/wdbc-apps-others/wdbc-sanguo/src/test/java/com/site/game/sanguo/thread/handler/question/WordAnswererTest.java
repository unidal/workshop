package com.site.game.sanguo.thread.handler.question;

import com.site.lookup.ComponentTestCase;

public class WordAnswererTest extends ComponentTestCase {
   public void testAnswer() throws Exception {
      Answerer answerer = lookup(Answerer.class, "word");

      assertTrue(answerer.canAnswer("歇后语 黄忠射箭 的下一句是:"));
      assertEquals(3, answerer.answer("歇后语 黄忠射箭 的下一句是:", new String[] { "白送", "一目了然", "百发百中", "唯才是举" }));
   }
}
