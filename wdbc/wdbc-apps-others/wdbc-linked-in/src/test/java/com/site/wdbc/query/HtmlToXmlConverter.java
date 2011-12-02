package com.site.wdbc.query;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.parser.ParserDelegator;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class HtmlToXmlConverter extends HTMLEditorKit.ParserCallback {
   private Element m_doc = null;

   private Stack<Element> m_elements = new Stack<Element>();

   @Override
   public void handleEndTag(Tag tag, int pos) {
      if (!m_elements.isEmpty()) {
         m_elements.pop();
      }
   }

   @Override
   public void handleSimpleTag(Tag tag, MutableAttributeSet attributeSet, int pos) {
      handleStartTag(tag, attributeSet, pos);
      handleEndTag(tag, pos);
   }

   @SuppressWarnings("unchecked")
   @Override
   public void handleStartTag(Tag tag, MutableAttributeSet attributeSet, int pos) {
      Element element = new Element(tag.toString());
      Enumeration names = attributeSet.getAttributeNames();

      while (names.hasMoreElements()) {
         Object name = names.nextElement();
         Object value = attributeSet.getAttribute(name);

         element.setAttribute(name.toString(), value.toString());
      }

      if (m_doc == null) {
         m_doc = element;
      }

      if (m_elements.isEmpty()) {
         m_elements.add(element);
      } else {
         m_elements.peek().addContent(element);
         m_elements.add(element);
      }
   }

   @Override
   public void handleText(char[] ch, int pos) {
      if (!m_elements.isEmpty()) {
         m_elements.peek().setText(new String(ch));
      }
   }

   public String convert(Reader reader) throws IOException {
      StringWriter writer = new StringWriter();

      new ParserDelegator().parse(reader, this, true);
      new XMLOutputter(Format.getPrettyFormat()).output(m_doc, writer);

      return writer.toString();
   }
}
