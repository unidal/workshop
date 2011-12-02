package com.site.dal.xml.builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.plexus.util.FileUtils;
import org.xml.sax.SAXException;

public class StreamXmlWriterTest extends AbstractXmlWriterTest {
   public void testInvalidArgument() {
      StreamXmlWriter handler = new StreamXmlWriter();
      
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
      
      try {
         handler.setResult(new SAXResult());
         fail("Invalid Result type, exception should be thrown.");
      } catch (RuntimeException e) {
         // expected
      }
   }
   
   public void testOutputStream() throws SAXException {
      StreamXmlWriter handler = new StreamXmlWriter();
      ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
      
      handler.setResult(new StreamResult(inputStream));
      
      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);
      
      assertEquals(asString(
               "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n",
               "<game>\r\n",
               "   <tasks count=\"0\">no tasks</tasks>\r\n",
               "</game>",
               null), 
               inputStream.toString());
   }
   
   public void testSystemId() throws SAXException, IOException {
      StreamXmlWriter handler = new StreamXmlWriter();
      File file = new File("target/file.xml");
      
      handler.setResult(new StreamResult(file.toURI().toURL().toExternalForm()));
      
      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);
      
      String fileContent = FileUtils.fileRead(file);
      
      file.delete();
      assertEquals(asString(
               "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n",
               "<game>\r\n",
               "   <tasks count=\"0\">no tasks</tasks>\r\n",
               "</game>",
               null), 
               fileContent);
   }
   
   public void testWithIndent() throws SAXException {
      StreamXmlWriter handler = new StreamXmlWriter();
      StringWriter writer = new StringWriter();

      handler.setResult(new StreamResult(writer));

      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks");
      startElement(handler, "task", "id", "1");
      characters(handler, "task 1");
      endElement(handler, "task");
      startElement(handler, "task", "id", "2", "description", "task 2");
      endElement(handler, "task");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      assertEquals(asString(
               "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n",
               "<game>\r\n",
               "   <tasks>\r\n",
               "      <task id=\"1\">task 1</task>\r\n",
               "      <task id=\"2\" description=\"task 2\" />\r\n",
               "   </tasks>\r\n",
               "</game>",
               null), 
               writer.toString());
   }
   
   public void testWithoutIndent() throws SAXException {
      StreamXmlWriter handler = new StreamXmlWriter();
      StringWriter writer = new StringWriter();
      
      handler.setResult(new StreamResult(writer));
      handler.setCompact(true);
      
      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks");
      startElement(handler, "task", "id", "1");
      characters(handler, "task 1");
      endElement(handler, "task");
      startElement(handler, "task", "id", "2", "description", "task 2");
      endElement(handler, "task");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);
      
      assertEquals(asString(
               "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n",
               "<game>\r\n",
               "<tasks>\r\n",
               "<task id=\"1\">task 1</task>\r\n",
               "<task id=\"2\" description=\"task 2\" />\r\n",
               "</tasks>\r\n",
               "</game>",
               null), 
               writer.toString());
   }
   
   public void testWriter() throws SAXException {
      StreamXmlWriter handler = new StreamXmlWriter();
      StringWriter writer = new StringWriter();
      
      handler.setResult(new StreamResult(writer));
      
      startDocument(handler);
      startElement(handler, "game");
      startElement(handler, "tasks", "count", "0");
      characters(handler, "no tasks");
      endElement(handler, "tasks");
      endElement(handler, "game");
      endDocument(handler);

      assertEquals(asString(
               "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n",
               "<game>\r\n",
               "   <tasks count=\"0\">no tasks</tasks>\r\n",
               "</game>",
               null), 
               writer.toString());
   }
}
