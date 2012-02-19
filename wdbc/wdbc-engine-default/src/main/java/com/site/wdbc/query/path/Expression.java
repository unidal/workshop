package com.site.wdbc.query.path;

import java.util.Map;

public class Expression implements WdbcExpression {
   private String m_expression;

   private boolean m_all;

   private int m_value;

   private Range m_range;

   private String m_attrName;

   private String m_attrValue;

   public Expression(String expression) {
      m_expression = expression;
      m_all = "*".equals(expression);
      m_value = Integer.MIN_VALUE;

      try {
         m_value = Integer.parseInt(expression);
      } catch (NumberFormatException e) {
         // ignore it
      }

      if (expression.startsWith("@")) {
         int pos = expression.indexOf('=');

         if (pos > 0) {
            m_attrName = expression.substring(1, pos);
            m_attrValue = expression.substring(pos + 1);
         } else {
            m_attrName = expression.substring(1);
         }
      } else {
         try {
            m_range = new Range(expression);
         } catch (NumberFormatException e) {
            // ignore it
         }

         if (m_range == null) {
            throw new RuntimeException(String.format("Unsupported expression(%s)!", expression));
         }
      }
   }

   public boolean matches(int value, Map<String, String> attributes) {
      if (m_all) {
         return true;
      } else if (m_value == value) {
         return true;
      } else if (m_range != null) {
         return m_range.inRange(value);
      } else if (m_attrName != null) {
         if (m_attrValue == null) {
            return attributes.containsKey(m_attrName);
         } else {
            return m_attrValue.equals(attributes.get(m_attrName));
         }
      }

      return false;
   }

   @Override
   public String toString() {
      return m_expression;
   }
}
