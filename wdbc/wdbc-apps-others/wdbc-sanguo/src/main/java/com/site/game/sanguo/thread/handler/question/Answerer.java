package com.site.game.sanguo.thread.handler.question;

public interface Answerer {
   public boolean canAnswer(String question);

   public int answer(String question, String[] choices);
}
