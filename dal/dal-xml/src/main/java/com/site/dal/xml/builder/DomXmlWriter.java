package com.site.dal.xml.builder;

import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.site.dal.xml.builder.dom.NodeFactory;

public class DomXmlWriter extends DefaultHandler implements XmlWriter {
   private NodeFactory m_factory;

   private Node m_current;

   private Node m_nextSibling;

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      m_factory.createText(m_current, new String(ch, start, length));
   }

   @Override
   public void endElement(String uri, String localName, String name) throws SAXException {
      m_current = m_current.getParentNode();
   }

   public void setResult(Result result) {
      if (result instanceof DOMResult) {
         DOMResult r = (DOMResult) result;

         if (r.getNode() == null) {
            r.setNode(m_factory.createDocument());
         }

         m_nextSibling = r.getNextSibling();
         m_current = r.getNode();
      }

      if (m_current == null) {
         throw new RuntimeException("Invalid Result instance: " + result);
      }
   }

   @Override
   public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
      Node element = m_factory.createElement(m_current, name, attributes, m_nextSibling);

      m_current = element;
      m_nextSibling = null;
   }
}
