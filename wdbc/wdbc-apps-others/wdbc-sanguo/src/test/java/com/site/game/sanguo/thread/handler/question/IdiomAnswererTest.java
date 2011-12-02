package com.site.game.sanguo.thread.handler.question;

import com.site.lookup.ComponentTestCase;

public class IdiomAnswererTest extends ComponentTestCase {
   public void testAnswer() throws Exception {
      Answerer answerer = lookup(Answerer.class, "idiom");

      assertTrue(answerer.canAnswer("�ٲ�����  ����"));
      assertEquals(2, answerer.answer("�ٲ�����  ����", new String[] { "�����", "����", "����", "����" }));
   }
}
