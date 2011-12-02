package com.site.wdbc.query;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParserCallback extends DefaultHandler {
   private WdbcHandler m_handler;

   private StringBuffer m_text;

   public XmlParserCallback(WdbcHandler handler) {
      m_handler = handler;
      m_text = new StringBuffer(2048);
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      m_text.append(ch, start, length);
   }

   @Override
   public void endDocument() throws SAXException {
      m_handler.handleEndDocument();
   }

   @Override
   public void endElement(String uri, String localName, String qName)
         throws SAXException {
      flushText();
      m_handler.handleEndTag(qName);
   }

   private void flushText() {
      String text = m_text.toString().trim();

      if (text.length() > 0) {
         m_handler.handleText(text);
      }

      m_text.setLength(0);
   }

   @Override
   public void error(SAXParseException e) throws SAXException {
      m_handler.handleError("Error happened when parsing XML", e);
   }

   @Override
   public void fatalError(SAXParseException e) throws SAXException {
      m_handler.handleError("Error happened when parsing XML", e);
   }

   @Override
   public void startDocument() throws SAXException {
      m_handler.handleStartDocument();
   }

   @Override
   public void startElement(String uri, String localName, String qName,
         Attributes attributeSet) throws SAXException {
      flushText();
      
      int size = attributeSet.getLength();
      Map<String, String> attributes = new HashMap<String, String>(size * 2);

      for (int i = 0; i < size; i++) {
         String name = attributeSet.getLocalName(i);
         String value = attributeSet.getValue(i);

         attributes.put(name, value);
      }

      m_handler.handleStartTag(qName, attributes);
   }
}
