package com.site.wdbc.ebay.arch;

import java.util.Map;

import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.jdom.Element;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;

public class DetailsProcessor implements Processor {
   @Inject
   private ResultHandler m_resultHandler;

   @Inject
   private ImageHandler m_imageHandler;

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

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String title = prop.get("details:title");
      PlexusConfiguration content = (PlexusConfiguration) session.getProperty("details:content", null);

      if (content != null) {
         Element body = m_resultHandler.getBody();

         createElement(body, "h2", title);

         try {
            processChildren(body, content);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   private void processChildren(Element parent, PlexusConfiguration node) throws PlexusConfigurationException {
      for (PlexusConfiguration child : node.getChildren()) {
         String name = child.getName();

         if (name.equals("#text")) {
            parent.addContent(child.getValue());
         } else if (name.equals("#comment")) {
            // ignore it
            // System.err.println("comment: " + child.getValue());
         } else {
            try {
               Element childElement = createElement(parent, name);

               for (String attrName : child.getAttributeNames()) {
                  String attrValue = child.getAttribute(attrName);

                  childElement.setAttribute(attrName, attrValue);
               }

               processChildren(childElement, child);
               m_imageHandler.handleElement(childElement);
            } catch (Exception e) {
               e.printStackTrace();
               System.err.println(String.format("Invalid element name(%s)", name));
            }
         }
      }
   }
}
