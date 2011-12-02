package com.site.wdbc.query.path;

import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;

public class WordPattern implements WdbcWordPattern {
   private String m_name;

   private String m_pattern;

   private String m_word;

   private Evaluator m_evaluator;

   public WordPattern(String name, String pattern) {
      m_name = name;
      m_pattern = pattern;

      parse(m_pattern);
   }

   public String getName() {
      return m_name;
   }

   public Evaluator getEvaluator() {
      return m_evaluator;
   }

   public String getWord() {
      return m_word;
   }

   public boolean matchesEvent(WdbcEventType eventType) {
      return m_evaluator.matchesEvent(eventType);
   }

   public boolean matchesWord(WdbcContext context) {
      String text = String.valueOf(m_evaluator.evaluate(context));
      boolean matched = (text != null && text.contains(m_word));

      return matched;
   }

   private void parse(String pattern) {
      int index1 = pattern.lastIndexOf('#');
      int index2 = pattern.lastIndexOf('@');
      int index = Math.max(index1, index2);

      if (index < 0) {
         m_word = pattern;
         m_evaluator = new Evaluator("#text");
      } else {
         m_word = pattern.substring(0, index);
         m_evaluator = new Evaluator(pattern.substring(index));
      }
   }

   @Override
   public String toString() {
      return m_evaluator.toString();
   }
}
