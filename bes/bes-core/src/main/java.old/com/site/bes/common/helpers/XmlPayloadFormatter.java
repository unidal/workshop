package com.site.bes.common.helpers;

import java.io.StringReader;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.site.bes.EventPayload;
import com.site.bes.EventPayloadException;
import com.site.bes.EventPayloadFormat;
import com.site.kernel.common.BaseXmlHandler;
import com.site.kernel.dal.DataObjectField;

public class XmlPayloadFormatter extends EventPayloadFormatter {
   private static final String ROOT = "payload";

   @Override
   public String format(EventPayload payload) {
      List<DataObjectField> fields = payload.getMetaData().getFields();
      StringBuffer sb = new StringBuffer(1024);

      synchronized (sb) {
         sb.append("<").append(ROOT).append(">\r\n");

         for (DataObjectField field : fields) {
            if (payload.isFieldUsed(field)) {
               String name = field.getName();
               Object obj = payload.getFieldValue(field);
               String value = formatFieldValue(field, obj);

               sb.append('<').append(name).append('>');

               if (needsCDATA(value)) {
                  sb.append("<![CDATA[").append(value).append("]]>");
               } else {
                  sb.append(value);
               }

               sb.append("</").append(name).append(">\r\n");
            }
         }

         sb.append("</").append(ROOT).append(">\r\n");
      }

      return sb.toString();
   }

   public EventPayloadFormat getPayloadType() {
      return EventPayloadFormat.XML;
   }

   private boolean needsCDATA(String text) {
      int len = text == null ? 0 : text.length();

      for (int i = 0; i < len; i++) {
         char ch = text.charAt(i);

         if (ch == '&' || ch == '<' || ch == '>') {
            return true;
         }
      }

      return false;
   }

   public void parse(EventPayload payload, String data) {
      XmlParser parser = new XmlParser(this, payload);

      try {
         parser.parse(new InputSource(new StringReader(data)));
      } catch (Exception e) {
         throw new EventPayloadException("Can't parse XML payload: \r\n" + data, e);
      }
   }

   private static final class XmlParser extends BaseXmlHandler {
      private XmlPayloadFormatter m_formatter;

      private EventPayload m_payload;

      private Stack<String> m_tags;

      private StringBuffer m_text;

      public XmlParser(XmlPayloadFormatter formatter, EventPayload payload) {
         m_formatter = formatter;
         m_payload = payload;
         m_tags = new Stack<String>();
         m_text = new StringBuffer(1024);
      }

      @Override
      public void characters(char[] ch, int start, int length) throws SAXException {
         m_text.append(ch, start, length);
      }

      @Override
      public void endElement(String uri, String localName, String qName) throws SAXException {
         String tag = m_tags.pop();
         String text = m_text.toString();

         handleText(tag, text);
         m_text.setLength(0);
      }

      private void handleText(String tag, String text) throws SAXException {
         if (m_tags.isEmpty() && !tag.equals(ROOT)) {
            throw new SAXException("Root element in EventPayload must be: " + ROOT);
         }

         if (!m_tags.isEmpty()) {
            String parent = m_tags.peek();

            if (parent.equals(ROOT)) {
               m_formatter.injectFieldValue(m_payload, tag, text);
            }
         }
      }

      @Override
      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         m_tags.push(localName);
         m_text.setLength(0);
      }
   }
}
