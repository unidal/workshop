package com.site.kernel.dal.model;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.site.kernel.common.BaseXmlHandler;
import com.site.kernel.dal.model.common.Event;
import com.site.kernel.dal.model.common.ParsingException;
import com.site.kernel.dal.model.helpers.FormatterContext;

public class DefaultHandler extends BaseXmlHandler {
   private FormatterContext m_context;

   private StringBuffer m_text;

   public DefaultHandler(Class rootModelClass) {
      m_context = new FormatterContext();
      m_text = new StringBuffer(1024);

      // push root parser at the beginning
      XmlModel model = (XmlModel) ModelRegistry.newInstance(rootModelClass);

      m_context.setCurrentFormatter(model.getFormatter());
   }

   public XmlModel getRootModel() {
      return m_context.getRootModel();
   }

   public void validateModel() {
      XmlModel rootModel = m_context.getRootModel();

      rootModel.validate(new Stack<XmlModel>());
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      m_text.append(ch, start, length);
   }

   public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs) throws SAXException {
      Event e = new Event(namespaceURI, localName, rawName, attrs);

      try {
         m_context.getLeafFormatter().handleStart(m_context, e);
      } catch (ParsingException pe) {
         throw new SAXParseException(pe.getMessage(), m_context.getLocator(), pe);
      }

      m_text.setLength(0);
   }

   public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
      // handle text before its end
      if (m_text.length() > 0) {
         String text = m_text.toString();

         if (m_context.getLeafFormatter().ignoreWhiteSpaces()) {
            text = text.trim();
         }

         if (text.length() > 0) {
            try {
               m_context.getLeafFormatter().handleText(m_context, new Event(text));
            } catch (ParsingException pe) {
               throw new SAXParseException(pe.getMessage(), m_context.getLocator(), pe);
            }
         }

         m_text.setLength(0);
      }

      Event e = new Event(namespaceURI, localName, rawName, null);

      try {
         m_context.getLeafFormatter().handleEnd(m_context, e);
      } catch (ParsingException pe) {
         throw new SAXParseException(pe.getMessage(), m_context.getLocator(), pe);
      }
   }
}
