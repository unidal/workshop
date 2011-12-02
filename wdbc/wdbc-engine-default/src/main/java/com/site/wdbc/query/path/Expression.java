package com.site.wdbc.query.path;

public class Expression implements WdbcExpression {
   private String m_expression;

   private boolean m_all;

   private int m_value;

   private Range m_range;

   public Expression(String expression) {
      m_expression = expression;
      m_all = "*".equals(expression);

      try {
         m_value = Integer.parseInt(expression);
      } catch (NumberFormatException e) {
         m_value = Integer.MIN_VALUE;
         m_range = new Range(expression);
      }
   }

   public boolean matches(int value) {
      if (m_all) {
         return true;
      } else if (m_value == value) {
         return true;
      } else if (m_range != null) {
         return m_range.inRange(value);
      }

      return false;
   }

   @Override
   public String toString() {
      return m_expression;
   }
}
