package com.site.wdbc.query.path;

import java.util.ArrayList;
import java.util.List;

import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;

public class PathPattern implements WdbcPathPattern {
   private String m_name;

   private String m_pattern;

   public List<String> m_sectionNames;

   public List<WdbcExpression> m_sectionExpressions;

   public Evaluator m_evaluator;

   public PathPattern(String name, String pattern) {
      m_name = name;
      m_pattern = pattern;
      m_sectionNames = new ArrayList<String>();
      m_sectionExpressions = new ArrayList<WdbcExpression>();
      
      parse(m_pattern, m_sectionNames, m_sectionExpressions);

      m_evaluator = new Evaluator(m_sectionNames.get(m_sectionNames.size() - 1));
   }

   public Object evaluate(WdbcContext context) {
      return m_evaluator.evaluate(context);
   }

   public String getName() {
      return m_name;
   }

   public List<WdbcExpression> getSectionExpressions() {
      return m_sectionExpressions;
   }

   public List<String> getSectionNames() {
      return m_sectionNames;
   }

   public boolean isAllChildren() {
      return m_evaluator.isAllChildren();
   }

   public boolean isAllText() {
      return m_evaluator.isAllText();
   }

   public boolean matchesEvent(WdbcEventType eventType) {
      return m_evaluator.matchesEvent(eventType);
   }

   private void parse(String pattern, List<String> names, List<WdbcExpression> expressions) {
      int size = (pattern == null ? 0 : pattern.length());
      boolean inName = true;
      boolean inBracket = false;
      StringBuffer name = new StringBuffer(32);
      StringBuffer expr = new StringBuffer(32);

      for (int i = 0; i <= size; i++) {
         char ch = (i == size ? '.' : pattern.charAt(i));

         switch (ch) {
         case '[':
            inBracket = true;
            inName = false;
            break;
         case ']':
            inBracket = false;
            break;
         case '.':
            if (inBracket) {
               expr.append(ch);
               break;
            }

            String n = name.toString();
            String e = expr.toString().trim();

            if (n.length() == 0) {
               throw new IllegalArgumentException("Invalid pattern: " + pattern);
            }

            names.add(name.toString());

            if (e.length() == 0) {
               expressions.add(new Expression("1")); // means first position
            } else {
               expressions.add(new Expression(expr.toString().trim()));
            }

            name.setLength(0);
            expr.setLength(0);
            inName = true;
            break;
         default:
            if (inName) {
               name.append(ch);
            } else if (inBracket) {
               expr.append(ch);
            }
         }
      }

      if (names.size() > 0) {
         int len = names.size();
         String last = names.get(len - 1);

         // Not an attribute or text or comment
         if (last.charAt(0) != PATTERN_ATTRIBUTE_STARTING && !last.equals(PATTERN_COMMENT)
               && !last.equals(PATTERN_TEXT) && !last.equals(PATTERN_ALL_TEXT) && !last.equals(PATTERN_ALL_NODE)) {
            // Treat it as TEXT by default
            names.add(PATTERN_TEXT);
            expressions.add(new Expression("1"));
         }
      }
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer(256);

      sb.append("PathPattern[").append(m_name);
      sb.append(',').append(m_pattern).append(']');

      return sb.toString();
   }
}
