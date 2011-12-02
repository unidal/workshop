package com.site.wdbc.jctrans.carsinfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.wdbc.http.Session;
import com.site.wdbc.jctrans.Configuration;

public class Processor implements com.site.wdbc.http.Processor, LogEnabled {
   private Configuration m_configuration;

   private Logger m_logger;

   private boolean DEBUG = true;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      XmlPlexusConfiguration root = new XmlPlexusConfiguration("root");
      String id = prop.get("list:id");

      root.addChild("id", id);
      root.addChild("company", "<![CDATA[" + prop.get("details:company") + "]]>");
      root.addChild("title", "<![CDATA[" + prop.get("details:title") + "]]>");
      root.addChild("date", "<![CDATA[" + prop.get("list:date") + "]]>");
      root.addChild("type", "<![CDATA[" + prop.get("details:type") + "]]>");
      root.addChild("service", "<![CDATA[" + prop.get("details:service") + "]]>");
      root.addChild("vechiel", "<![CDATA[" + prop.get("details:vechiel") + "]]>");
      root.addChild("length", "<![CDATA[" + prop.get("details:length") + "]]>");
      root.addChild("weight", "<![CDATA[" + prop.get("details:weight") + "]]>");
      root.addChild("location", "<![CDATA[" + prop.get("details:location") + "]]>");
      root.addChild("range", "<![CDATA[" + prop.get("details:range") + "]]>");
      root.addChild("price", "<![CDATA[" + prop.get("details:price") + "]]>");
      root.addChild("payment", "<![CDATA[" + prop.get("details:payment") + "]]>");
      root.addChild("expiry", "<![CDATA[" + prop.get("details:expiry") + "]]>");
      root.addChild("notes", "<![CDATA[" + prop.get("details:notes") + "]]>");

      saveToFile(root, new File(m_configuration.getDataDir() + "/cars/" + id + ".xml"));

      if (DEBUG) {
         Map<String, String> record = new LinkedHashMap<String, String>();

         record.put("id", id);
         record.put("company", prop.get("details:company"));
         record.put("title", prop.get("details:title"));
         record.put("date", prop.get("list:date"));
         record.put("type", prop.get("details:type"));
         record.put("service", prop.get("details:service"));
         record.put("vechiel", prop.get("details:vechiel"));
         record.put("length", prop.get("details:length"));
         record.put("weight", prop.get("details:weight"));
         record.put("location", prop.get("details:location"));
         record.put("range", prop.get("details:range"));
         record.put("price", prop.get("details:price"));
         record.put("payment", prop.get("details:payment"));
         record.put("expiry", prop.get("details:expiry"));
         record.put("notes", prop.get("details:notes"));
         record.put("sourceUrl", session.getLastUrl().toExternalForm());

         System.out.println(record);
      }
   }

   private void saveToFile(XmlPlexusConfiguration root, File file) {
      file.getParentFile().mkdirs();

      try {
         Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");

         writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
         writer.write(root.toString());
         writer.close();

         m_logger.info("Result saved to file: " + file);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
