package com.site.dal.xml.builder;

import java.lang.reflect.Field;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.site.lookup.ComponentTestCase;

public abstract class AbstractXmlWriterTest extends ComponentTestCase {
   protected String asString(String... lines) {
      StringBuilder sb = new StringBuilder(1024);

      for (String line : lines) {
         if (line != null) {
            sb.append(line);
         }
      }

      return sb.toString();
   }

   protected void characters(ContentHandler handler, String data) throws SAXException {
      char[] chars = data.toCharArray();

      handler.characters(chars, 0, chars.length);
   }

   protected void endDocument(ContentHandler handler) throws SAXException {
      handler.endDocument();
   }

   protected void endElement(ContentHandler handler, String tagName) throws SAXException {
      handler.endElement(null, null, tagName);
   }

   protected void injectField(Object instance, String name, Object value) {
      if (instance != null) {
         try {
            Class<?> clazz = instance.getClass();
            Field field = clazz.getDeclaredField(name);

            field.setAccessible(true);
            field.set(instance, value);
         } catch (Exception e) {
            throw new RuntimeException("Can't inject field(" + name + ") of instance(" + instance + ") with value("
                     + value + ")", e);
         }
      }
   }

   protected void startDocument(ContentHandler handler) throws SAXException {
      handler.startDocument();
   }

   protected void startElement(ContentHandler handler, String tagName, String... attributeNameValuePairs)
            throws SAXException {
      AttributesImpl attributes = new AttributesImpl();
      int len = attributeNameValuePairs.length;

      if (len % 2 == 1) {
         throw new RuntimeException("Attributes name values must be paired.");
      }

      for (int i = 0; i < len; i += 2) {
         String name = attributeNameValuePairs[i];
         String value = attributeNameValuePairs[i + 1];

         attributes.addAttribute(null, null, name, "CDATA", value);
      }

      handler.startElement(null, null, tagName, attributes);
   }
}
