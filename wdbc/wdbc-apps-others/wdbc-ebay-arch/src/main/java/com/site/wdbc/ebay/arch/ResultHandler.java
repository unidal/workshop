package com.site.wdbc.ebay.arch;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.site.lookup.annotation.Inject;

public class ResultHandler implements Initializable {
   @Inject
   private Configuration m_configuration;

   private Element m_body;

   public Element getBody() {
      return m_body;
   }

   private String buildXml(Document doc) throws IOException {
      StringWriter writer = new StringWriter();
      XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setIndent("   "));

      outputter.output(doc, writer);
      writer.close();

      return writer.toString();
   }

   private Element createElement(Element parent, String name) {
      Element element = new Element(name);

      parent.addContent(element);
      return element;
   }

   private Element createElement(Element parent, String name, String text) {
      Element element = createElement(parent, name);

      element.addContent(text);
      return element;
   }

   public void initialize() throws InitializationException {
      m_body = new Element("body");
   }

   public void handleCourse(String title) {
      Document doc = new Document();
      Element html = new Element("html");
      Element header = createElement(html, "header");

      doc.addContent(html);
      createElement(header, "meta") //
            .setAttribute("http-equiv", "Content-Type")//
            .setAttribute("content", "text/html; charset=utf-8");
      createElement(header, "title", title);
      html.addContent(m_body);

      File file = new File(m_configuration.getOutputDir(), title + ".html");
      String path = file.getAbsolutePath();

      try {
         FileUtils.fileWrite(path, buildXml(doc));
         System.out.println(String.format("File(%s) generated successfully.", path));
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // reset for next course
         m_body = new Element("body");
      }
   }
}
