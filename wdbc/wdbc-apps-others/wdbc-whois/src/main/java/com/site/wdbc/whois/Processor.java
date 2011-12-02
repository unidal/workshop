package com.site.wdbc.whois;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.wdbc.http.Session;

public class Processor implements com.site.wdbc.http.Processor, Initializable {
   private File m_domainFile = new File("domains.txt");

   private Writer m_writer;

   private DateFormat m_format1 = new SimpleDateFormat("dd-MMM-yyyy");

   private DateFormat m_format2 = new SimpleDateFormat("yyyy-MM-dd");

   private Date m_now = new Date();

   private boolean check(String domain, String expiration) {
      int pos = expiration.indexOf(' ');

      if (pos > 0) {
         expiration = expiration.substring(0, pos);
      }

      Date date = null;

      try {
         date = m_format1.parse(expiration);
      } catch (ParseException e) {
         // ignore it
      }

      if (date == null) {
         try {
            date = m_format1.parse(expiration);
         } catch (ParseException e) {
            // ignore it
         }
      }

      String str = date == null ? expiration : m_format2.format(date);

      if (date != null && date.before(m_now)) {
         System.err.println(domain + " " + str);
         return true;
      } else {
         System.out.println(domain + " " + str);
         return false;
      }
   }

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String domain = prop.get("domain-names:name");
      String expiration = prop.get("details:expiration");

      try {
         if (check(domain, expiration)) {
            m_writer.write(domain + "\t" + expiration + " EXPIRED\n");
         } else {
            m_writer.write(domain + "\t" + expiration + "\n");
         }

         m_writer.flush();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public void initialize() throws InitializationException {
      try {
         m_writer = new FileWriter(m_domainFile, true);
      } catch (IOException e) {
         throw new InitializationException("Can't access file: " + m_domainFile, e);
      }
   }
}
