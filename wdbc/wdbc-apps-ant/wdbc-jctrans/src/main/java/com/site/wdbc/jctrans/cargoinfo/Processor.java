package com.site.wdbc.jctrans.cargoinfo;

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
      root.addChild("title", "<![CDATA[" + prop.get("details:title") + "]]>");
      root.addChild("date", prop.get("list:date"));
      root.addChild("goods", "<![CDATA[" + prop.get("details:goods") + "]]>");
      root.addChild("type", "<![CDATA[" + prop.get("details:type") + "]]>");
      root.addChild("quantity", prop.get("details:quantity"));
      root.addChild("package", "<![CDATA[" + prop.get("details:package") + "]]>");
      root.addChild("source", "<![CDATA[" + prop.get("details:source") + "]]>");
      root.addChild("target", "<![CDATA[" + prop.get("details:target") + "]]>");
      root.addChild("payment", "<![CDATA[" + prop.get("details:payment") + "]]>");
      root.addChild("deadline", "<![CDATA[" + prop.get("details:deadline") + "]]>");
      root.addChild("expiry", "<![CDATA[" + prop.get("details:expiry") + "]]>");
      root.addChild("notes", "<![CDATA[" + prop.get("details:notes") + "]]>");

      saveToFile(root, new File(m_configuration.getDataDir() + "/cargo/" + id + ".xml"));

      if (DEBUG) {
         Map<String, String> record = new LinkedHashMap<String, String>();

         record.put("id", id);
         record.put("title", prop.get("details:title"));
         record.put("date", prop.get("list:date"));
         record.put("goods", prop.get("details:goods"));
         record.put("type", prop.get("details:type"));
         record.put("quantity", prop.get("details:quantity"));
         record.put("package", prop.get("details:package"));
         record.put("source", prop.get("details:source"));
         record.put("target", prop.get("details:target"));
         record.put("payment", prop.get("details:payment"));
         record.put("deadline", prop.get("details:deadline"));
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
