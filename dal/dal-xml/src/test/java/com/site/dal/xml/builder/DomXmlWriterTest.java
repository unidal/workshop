package com.site.dal.xml.builder;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.site.dal.xml.builder.dom.DefaultNodeFactory;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;

public class DomXmlWriterTest extends AbstractXmlWriterTest {
   public void testInvalidArgument() {
      DomXmlWriter handler = new DomXmlWriter();

      try {
         handler.setResult(new StreamResult());
         fail("Invalid Result type, exception should be thrown.");
      } catch (RuntimeException e) {
         // expected
      }

      try {
         handler.setResult(new SAXResult());
         fail("Invalid Result type, exception should be thrown.");
      } catch (RuntimeException e) {
         // expected
      }
   }

   public void testWithoutNode() throws SAXException {
      DomXmlWriter handler = new DomXmlWriter();
      DOMResult result = new DOMResult();

      injectField(handler, "m_factory", new DefaultNodeFactory());
      handler.setResult(result);

      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      Node root = result.getNode();
      Node game = root.getFirstChild();

      assertEquals("game", game.getNodeName());
      assertEquals(1, game.getChildNodes().getLength());
      assertEquals("tasks", game.getFirstChild().getNodeName());
      assertEquals("0", game.getFirstChild().getAttributes().getNamedItem("count").getNodeValue());
      assertEquals("no tasks", game.getFirstChild().getFirstChild().getNodeValue());
   }

   public void testWithNode1() throws SAXException {
      DomXmlWriter handler = new DomXmlWriter();
      DOMResult result = new DOMResult();
      Node root = new DocumentImpl();

      result.setNode(root);
      injectField(handler, "m_factory", new DefaultNodeFactory());
      handler.setResult(result);

      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      assertEquals("game", root.getFirstChild().getNodeName());
      assertEquals(1, root.getFirstChild().getChildNodes().getLength());
      assertEquals("tasks", root.getFirstChild().getFirstChild().getNodeName());
      assertEquals("0", root.getFirstChild().getFirstChild().getAttributes().getNamedItem("count").getNodeValue());
      assertEquals("no tasks", root.getFirstChild().getFirstChild().getFirstChild().getNodeValue());
   }

   public void testWithNode2() throws SAXException {
      DomXmlWriter handler = new DomXmlWriter();
      DOMResult result = new DOMResult();
      Document doc = new DocumentImpl();
      Node root = doc.createElement("root");

      doc.appendChild(root);
      result.setNode(root);
      injectField(handler, "m_factory", new DefaultNodeFactory());
      handler.setResult(result);

      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      assertEquals("root", doc.getFirstChild().getNodeName());

      Node game = root.getFirstChild();

      assertEquals("game", game.getNodeName());
      assertEquals(1, game.getChildNodes().getLength());
      assertEquals("tasks", game.getFirstChild().getNodeName());
      assertEquals("0", game.getFirstChild().getAttributes().getNamedItem("count").getNodeValue());
      assertEquals("no tasks", game.getFirstChild().getFirstChild().getNodeValue());
   }

   public void testWithNodeAndSibling() throws SAXException {
      DomXmlWriter handler = new DomXmlWriter();
      DOMResult result = new DOMResult();
      Document doc = new DocumentImpl();
      Node root = doc.createElement("root");
      Node firstSibling = doc.createElement("firstSibling");
      Node nextSibling = doc.createElement("nextSibling");

      doc.appendChild(root);
      root.appendChild(firstSibling);
      root.appendChild(nextSibling);
      result.setNode(root);
      result.setNextSibling(nextSibling);
      injectField(handler, "m_factory", new DefaultNodeFactory());
      handler.setResult(result);

      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      assertEquals("root", doc.getFirstChild().getNodeName());
      assertEquals("firstSibling", root.getFirstChild().getNodeName());
      assertEquals("nextSibling", root.getLastChild().getNodeName());

      Node game = root.getFirstChild().getNextSibling();

      assertEquals("game", game.getNodeName());
      assertEquals(1, game.getChildNodes().getLength());
      assertEquals("tasks", game.getFirstChild().getNodeName());
      assertEquals("0", game.getFirstChild().getAttributes().getNamedItem("count").getNodeValue());
      assertEquals("no tasks", game.getFirstChild().getFirstChild().getNodeValue());
   }
}
