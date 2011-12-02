package com.site.dal.xml.parser;

import java.util.Stack;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.site.dal.xml.XmlException;
import com.site.dal.xml.registry.ElementEntry;
import com.site.dal.xml.registry.XmlRegistry;

public class DefaultXmlParser extends DefaultHandler implements XmlParser {
   private XmlRegistry m_registry;

   private XmlParserPolicy m_policy;

   private ElementComposition m_composition;

   private Stack<ElementNode> m_stack = new Stack<ElementNode>();

   private StringBuilder m_text = new StringBuilder(2048);

   public DefaultXmlParser() {
      m_stack.push(new ElementNode(true));
   }

   public Object parse(InputSource source) throws XmlException {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();

         factory.setNamespaceAware(true);
         factory.newSAXParser().parse(source, this);

         ElementNode root = m_stack.peek();

         return root.getRootElementValue();
      } catch (Exception e) {
         throw new XmlException("Error when parsing XML data. " + e.getMessage(), e);
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      m_text.append(ch, start, length);
   }

   @Override
   public void endElement(String uri, String localName, String name) throws SAXException {
      ElementNode current = m_stack.pop();
      ElementEntry currentEntry = current.getElementEntry();
      String elementName = localName;

      if (currentEntry != null) {
         current.setText(m_text.toString());

         Object value;

         try {
            value = m_composition.compose(current);
         } catch (Exception e) {
            e.printStackTrace();
            throw new SAXException("Error when composing element(" + elementName + "): " + current, e);
         }

         ElementNode parent = m_stack.peek();

         if (currentEntry.isInComponent()) {
            parent.addComponentValue(elementName, value);
         } else {
            parent.addElementValue(elementName, value);
         }
      }

      m_text.setLength(0);
   }

   @Override
   public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
      ElementNode parent = m_stack.peek();
      ElementNode current = new ElementNode(false);
      String elementName = localName;

      if (parent.isRoot()) { // Root element
         Class<?> rootClass = m_registry.findRootElementType(elementName);
         ElementEntry rootEntry = m_registry.findElementEntry(rootClass);

         if (rootEntry == null) {
            handleUnknownElement("No root element registered for '" + elementName + "'");
         } else {
            current.setElementEntry(rootEntry);
         }
      } else {
         ElementEntry parentEntry = parent.getElementEntry();

         if (parentEntry != null) {
            ElementEntry currentEntry = parentEntry.getElement(elementName);

            if (currentEntry == null) {
               parentEntry = m_registry.findElementEntry(parentEntry.getType());
               currentEntry = parentEntry.getComponentElement(elementName);

               if (currentEntry == null) {
                  currentEntry = parentEntry.getElement(elementName);
               }
            }

            if (currentEntry != null) {
               current.setElementEntry(currentEntry);
            } else {
               handleUnknownElement("No element(" + elementName + ") found in " + parentEntry.getType());
            }
         }
      }

      m_stack.push(current);
      m_text.setLength(0);
      handleAttributes(current, attributes);
   }

   private void handleAttributes(ElementNode current, Attributes attributes) {
      if (current.getElementEntry() == null) {
         return;
      }

      int len = attributes.getLength();

      for (int i = 0; i < len; i++) {
         String name = attributes.getQName(i);
         String value = attributes.getValue(i);

         current.setAttributeValue(name, value);
      }
   }

   private void handleUnknownElement(String message) throws SAXException {
      switch (m_policy.forUnknownElement()) {
      case IGNORE:
         // ignore it
         break;
      case WARNING:
         System.err.println(message);
         break;
      case ERROR:
         throw new SAXException(message);
      }
   }
}
