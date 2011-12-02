package com.site.dal.xml.builder;

import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxXmlWriter extends DefaultHandler implements XmlWriter {
   private ContentHandler m_handler;

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      m_handler.characters(ch, start, length);
   }

   @Override
   public void endDocument() throws SAXException {
      m_handler.endDocument();
   }

   @Override
   public void endElement(String uri, String localName, String name) throws SAXException {
      m_handler.endElement(uri, localName, name);
   }

   @Override
   public void endPrefixMapping(String prefix) throws SAXException {
      m_handler.endPrefixMapping(prefix);
   }

   @Override
   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      m_handler.ignorableWhitespace(ch, start, length);
   }

   @Override
   public void processingInstruction(String target, String data) throws SAXException {
      m_handler.processingInstruction(target, data);
   }

   @Override
   public void setDocumentLocator(Locator locator) {
      m_handler.setDocumentLocator(locator);
   }

   public void setResult(Result result) {
      if (result instanceof SAXResult) {
         SAXResult r = (SAXResult) result;

         m_handler = r.getHandler();
      }

      if (m_handler == null) {
         throw new RuntimeException("Invalid Result instance: " + result);
      }
   }

   @Override
   public void skippedEntity(String name) throws SAXException {
      m_handler.skippedEntity(name);
   }

   @Override
   public void startDocument() throws SAXException {
      m_handler.startDocument();
   }

   @Override
   public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
      m_handler.startElement(uri, localName, name, atts);
   }

   @Override
   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      m_handler.startPrefixMapping(prefix, uri);
   }
}
