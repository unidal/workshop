package com.site.wdbc.whois;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.WdbcFilter;
import com.site.wdbc.query.path.TagTree;
import com.site.wdbc.query.path.WdbcTagTree;

public class DomainNamesQuery implements WdbcQuery {
   private WdbcFilter m_filter;

   private int m_batchSize = 100000;

   private File m_domainFile = new File("domains.txt");

   private int m_length = 3;

   private String m_name;

   private String m_suffix = ".com";

   private String m_uriPattern;

   private boolean m_includeNumber = false;

   public WdbcTagTree buildTagTree() {
      return TagTree.buildTree(null, false);
   }

   private Set<String> getDomainNames() {
      Set<String> done = loadDomainNamesFromFile();
      Set<String> domains = new HashSet<String>();
      StringBuffer sb = new StringBuffer();
      int count = 0;

      int maxSize = 1;
      for (int i = 0; i < m_length; i++) {
         maxSize *= (m_includeNumber ? 36 : 26);
      }

      while (count < m_batchSize && count < maxSize - done.size()) {
         sb.setLength(0);

         for (int j = 0; j < m_length; j++) {
            int num = (int) (Math.random() * (m_includeNumber ? 36 : 26));

            if (num < 26) {
               sb.append((char) ('a' + num));
            } else {
               sb.append((char) ('0' + (num - 26)));
            }
         }

         sb.append(m_suffix);

         String domain = sb.toString();

         if (!done.contains(domain) && !domains.contains(domain)) {
            domains.add(domain);
            count++;
         }
      }

      return domains;
   }

   public String getName() {
      return m_name;
   }

   public void handleEvent(WdbcContext context, WdbcResult result, WdbcEventType eventType) {
      switch (eventType) {
      case START_DOCUMENT:
         String[] columns = new String[] { "name", "link" };

         result.begin(columns);
         break;
      case END_DOCUMENT:
         result.applyLastRow();

         if (m_filter != null) {
            m_filter.doFilter(result);
         }

         break;
      default:
         if (result.getRowSize() == 0) { // only add once
            MessageFormat format = new MessageFormat(m_uriPattern);
            Set<String> domains = getDomainNames();

            for (String domain : domains) {
               String link = format.format(new Object[] { domain });

               result.addValue("name", domain);
               result.addValue("link", link);
            }
         }
      }
   }

   private Set<String> loadDomainNamesFromFile() {
      Set<String> domains = new HashSet<String>();

      try {
         BufferedReader reader = new BufferedReader(new FileReader(m_domainFile));

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            String[] parts = line.split("\t");

            if (parts[0].length() > 0) {
               domains.add(parts[0]);
            }
         }

         reader.close();
      } catch (IOException e) {
         // ignore it
      }

      return domains;
   }

   public void setBatchSize(int batchSize) {
      m_batchSize = batchSize;
   }

   public void setDomainFile(File domainFile) {
      m_domainFile = domainFile;
   }

   public void setLength(int length) {
      m_length = length;
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setSuffix(String suffix) {
      m_suffix = suffix;
   }

   public void setUriPattern(String uriPattern) {
      m_uriPattern = uriPattern;
   }

}
