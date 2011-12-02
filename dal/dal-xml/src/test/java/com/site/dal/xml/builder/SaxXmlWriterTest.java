package com.site.dal.xml.builder;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxXmlWriterTest extends AbstractXmlWriterTest {
   public void testInvalidArgument() {
      SaxXmlWriter handler = new SaxXmlWriter();

      try {
         handler.setResult(new SAXResult());
         fail("Invalid StreamResult instance, exception should be thrown.");
      } catch (RuntimeException e) {
         // expected
      }

      try {
         handler.setResult(new StreamResult());
         fail("Invalid Result type, exception should be thrown.");
      } catch (RuntimeException e) {
         // expected
      }

      try {
         handler.setResult(new DOMResult());
         fail("Invalid Result type, exception should be thrown.");
      } catch (RuntimeException e) {
         // expected
      }
   }

   public void testWriter() throws SAXException {
      SaxXmlWriter handler = new SaxXmlWriter();
      MyHandler myHandler = new MyHandler();

      handler.setResult(new SAXResult(myHandler));

      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      assertEquals(asString(
               "start: game\r\n", 
               "start: tasks count=0\r\n", 
               "text: no tasks\r\n", 
               "end: tasks\r\n",
               "end: game\r\n", null), myHandler.getOutput());
   }

   static final class MyHandler extends DefaultHandler {
      private StringBuilder m_out = new StringBuilder(1024);

      public String getOutput() {
         return m_out.toString();
      }

      @Override
      public void characters(char[] ch, int start, int length) throws SAXException {
         m_out.append("text: ").append(new String(ch, start, length)).append("\r\n");
      }

      @Override
      public void endElement(String uri, String localName, String name) throws SAXException {
         m_out.append("end: ").append(name).append("\r\n");
      }

      @Override
      public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
         m_out.append("start: ").append(name);

         int len = attributes.getLength();
         for (int i = 0; i < len; i++) {
            m_out.append(' ').append(attributes.getQName(i)).append("=").append(attributes.getValue(i));
         }

         m_out.append("\r\n");
      }
   }
}
