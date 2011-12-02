package com.site.dal.xml.builder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StreamXmlWriter extends DefaultHandler implements XmlWriter {
   private Writer m_writer;

   private int m_indents;

   private boolean m_compact;

   private StringBuilder m_sb;

   private Step m_lastStep;

   @Override
   public void characters(char[] chars, int start, int length) throws SAXException {
      StringBuilder sb = new StringBuilder(length + 256);
      int end = start + length;

      for (int i = start; i < end; i++) {
         char ch = chars[i];

         switch (ch) {
         case '<':
            sb.append("&lt;");
            break;
         case '>':
            sb.append("&gt;");
            break;
         case '&':
            sb.append("&amp;");
            break;
         default:
            sb.append(ch);
            break;
         }
      }

      m_sb.append(sb.toString());
      m_lastStep = Step.TEXT;
   }

   @Override
   public void endDocument() throws SAXException {
      try {
         m_writer.write(m_sb.toString());
      } catch (IOException e) {
         throw new SAXException("Can't output to StreamResult.", e);
      } finally {
         try {
            m_writer.close();
         } catch (IOException e) {
            // ignore it
         }
      }
   }

   @Override
   public void endElement(String uri, String localName, String name) throws SAXException {
      m_indents--;

      switch (m_lastStep) {
      case TEXT:
         m_sb.append("</").append(name).append(">");
         break;
      case START_ELEMENT:
         m_sb.insert(m_sb.length() - 1, " /");
         break;
      case END_ELEMENT:
         m_sb.append("\r\n");
         indent();
         m_sb.append("</").append(name).append(">");
         break;
      }

      m_lastStep = Step.END_ELEMENT;
   }

   private void indent() {
      if (!m_compact) {
         for (int i = m_indents; i > 0; i--) {
            m_sb.append("   ");
         }
      }
   }

   public void setCompact(boolean compact) {
      m_compact = compact;
   }

   public void setResult(Result result) {
      try {
         if (result instanceof StreamResult) {
            StreamResult r = (StreamResult) result;

            if (r.getWriter() != null) {
               m_writer = r.getWriter();
            } else if (r.getOutputStream() != null) {
               m_writer = new OutputStreamWriter(r.getOutputStream(), "utf-8");
            } else if (r.getSystemId() != null) {
               String systemId = r.getSystemId();
               URL url = new URL(systemId);

               if (systemId.startsWith("file:")) {
                  m_writer = new OutputStreamWriter(new FileOutputStream(url.getFile()), "utf-8");
               } else {
                  m_writer = new OutputStreamWriter(url.openConnection().getOutputStream(), "utf-8");
               }
            }
         }
      } catch (Exception e) {
         throw new RuntimeException("Invalid StreamResult: " + result, e);
      }

      if (m_writer == null) {
         throw new RuntimeException("Unknown Result instance: " + result);
      }
   }

   @Override
   public void startDocument() throws SAXException {
      m_sb = new StringBuilder(8092);
      m_sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
      m_lastStep = Step.END_ELEMENT;
   }

   @Override
   public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
      m_sb.append("\r\n");
      indent();
      m_sb.append('<').append(name);

      int len = attributes.getLength();

      for (int i = 0; i < len; i++) {
         m_sb.append(' ').append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append('"');
      }

      m_sb.append('>');
      m_indents++;
      m_lastStep = Step.START_ELEMENT;
   }

   private static enum Step {
      START_ELEMENT, TEXT, END_ELEMENT;
   }
}
