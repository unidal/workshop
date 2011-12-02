package com.site.wdbc.query;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;

public class HtmlParserCallback extends HTMLEditorKit.ParserCallback {
   private WdbcHandler m_handler;

   private boolean m_started;

   private boolean m_afterSimpleTag;

   public HtmlParserCallback(WdbcHandler handler) {
      m_handler = handler;
   }

   @Override
   public void handleComment(char[] ch, int pos) {
      m_handler.handleComment(new String(ch));
   }

   @Override
   public void handleEndTag(Tag tag, int pos) {
      m_handler.handleEndTag(tag.toString());
      m_afterSimpleTag = false;
   }

   @Override
   public void handleError(String message, int pos) {
      m_handler.handleError(message, null);
   }

   @Override
   public void handleSimpleTag(Tag tag, MutableAttributeSet attributeSet, int pos) {
      handleStartTag(tag, attributeSet, pos);
      handleEndTag(tag, pos);
      m_afterSimpleTag = true;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void handleStartTag(Tag tag, MutableAttributeSet attributeSet, int pos) {
      if (!m_started && tag == Tag.HTML) {
         m_started = true;
         m_handler.handleStartDocument();
      }

      int size = attributeSet.getAttributeCount();
      Map<String, String> attributes = new HashMap<String, String>(size * 2);
      Enumeration<String> names = (Enumeration<String>) attributeSet.getAttributeNames();

      while (names.hasMoreElements()) {
         Object name = names.nextElement();
         Object value = attributeSet.getAttribute(name);

         attributes.put(name.toString(), value.toString());
      }

      m_handler.handleStartTag(tag.toString(), attributes);
   }

   @Override
   public void handleText(char[] ch, int pos) {
      for (int i = 0; i < ch.length; i++) {
         if (ch[i] == 160) {
            ch[i] = ' ';
         }
      }

      String text = new String(ch);

      if (m_afterSimpleTag && text.startsWith(">")) {
         text = text.substring(1);
      }

      m_handler.handleText(text);
   }

   @Override
   public void handleEndOfLineString(String eol) {
      if (!m_started) {
         m_started = true;
         m_handler.handleStartDocument();
      }

      m_handler.handleEndDocument();
   }

}
