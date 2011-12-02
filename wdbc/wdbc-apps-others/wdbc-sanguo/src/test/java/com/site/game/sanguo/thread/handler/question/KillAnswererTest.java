package com.site.game.sanguo.thread.handler.question;

import com.site.lookup.ComponentTestCase;

public class KillAnswererTest extends ComponentTestCase {
   public void testAnswer() throws Exception {
      Answerer answerer = lookup(Answerer.class, "kill");

      assertTrue(answerer.canAnswer("���� ��������ɱ"));
      assertEquals(3, answerer.answer("���� ��������ɱ", new String[] { "����", "��ά", "���", "����" }));
   }
}
