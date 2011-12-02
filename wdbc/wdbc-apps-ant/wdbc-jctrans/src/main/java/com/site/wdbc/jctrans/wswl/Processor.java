package com.site.wdbc.jctrans.wswl;

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
      String id = prop.get("list:id");
      String title = prop.get("list:title");
      String date = prop.get("details:date");
      String from = prop.get("details:from");
      String body = prop.get("details:body");

      XmlPlexusConfiguration root = new XmlPlexusConfiguration("root");

      root.addChild("id", id);
      root.addChild("title", "<![CDATA[" + title + "]]>");
      root.addChild("date", date);
      root.addChild("from", "<![CDATA[" + from + "]]>");
      root.addChild("body", "<![CDATA[" + body + "]]>");

      saveToFile(root, new File(m_configuration.getDataDir() + "/wswl/" + id + ".xml"));

      if (DEBUG) {
         Map<String, String> record = new LinkedHashMap<String, String>();

         record.put("id", id);
         record.put("title", title);
         record.put("date", date);
         record.put("from", from);
         record.put("body", body.replace("\r\n", "\\r\\n"));
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
