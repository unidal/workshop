package com.site.wdbc.query.path;

import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ALL_NODE;
import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ALL_TEXT;
import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ATTRIBUTE_STARTING;
import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_COMMENT;
import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_TEXT;

import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;

public class Evaluator {
   private String m_pattern;

   private boolean m_attribute; // @...

   private String m_attributeName;

   private boolean m_text; // #text

   private boolean m_comment; // #comment

   private boolean m_allText; // *text

   private boolean m_allChildren; // *

   public Evaluator(String pattern) {
      m_pattern = pattern;

      parse(pattern);
   }

   public Object evaluate(WdbcContext context) {
      Object data;

      if (m_text) {
         data = context.getText();
      } else if (m_comment) {
         data = context.getComment();
      } else if (m_attribute) {
         data = context.getAttribute(m_attributeName);
      } else if (m_allText) {
         data = context.getAllText();
      } else if (m_allChildren) {
         data = context.getAllChildren();
      } else {
         throw new IllegalStateException("Internal error: should not happen");
      }

      return data;
   }

   public boolean isAllChildren() {
      return m_allChildren;
   }

   public boolean isAllText() {
      return m_allText;
   }

   public boolean matchesEvent(WdbcEventType eventType) {
      switch (eventType) {
      case TEXT:
         return m_text;
      case START_TAG:
         return m_attribute || m_allText || m_allChildren;
      case END_TAG:
         return m_allText || m_allChildren;
      case COMMENT:
         return m_comment;
      default:
         return false;
      }
   }

   private void parse(String pattern) {
      if (pattern == null || pattern.length() == 0) {
         throw new IllegalArgumentException("No pattern specified for evaluate");
      }

      char ch = pattern.charAt(0);

      if (ch == PATTERN_ATTRIBUTE_STARTING) {
         m_attribute = true;
         m_attributeName = pattern.substring(1);
      } else if (pattern.equals(PATTERN_TEXT)) {
         m_text = true;
      } else if (pattern.equals(PATTERN_COMMENT)) {
         m_comment = true;
      } else if (pattern.equals(PATTERN_ALL_TEXT)) {
         m_allText = true;
      } else if (pattern.equals(PATTERN_ALL_NODE)) {
         m_allChildren = true;
      } else {
         throw new IllegalArgumentException("Invalid pattern specifed for evaluate: " + pattern);
      }
   }

   @Override
   public String toString() {
      return m_pattern;
   }
}
