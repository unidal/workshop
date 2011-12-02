package com.site.app.tracking.counter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class DefaultCategoryMapping implements CategoryMapping, Initializable, LogEnabled {
   Configuration m_configuration;

   Map<Integer, Integer> m_cachedMapping;

   Logger m_logger;

   private Fetcher m_fetcher;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public int getCategory1(int category2) {
      Integer category1 = (m_cachedMapping == null ? null : m_cachedMapping.get(category2));

      if (category1 != null) {
         return category1.intValue();
      } else {
         return category2;
      }
   }

   public void initialize() throws InitializationException {
      if (m_configuration.getCategorySourceUrl() == null) {
         m_logger.warn("No init-param(catagory-source) configured in the web.xml file");
      } else {
         m_fetcher = new Fetcher();

         final Timer timer = new Timer("DefaultCategoryMapping.Fetcher", true);
         timer.scheduleAtFixedRate(m_fetcher, 0, m_configuration.getRefreshInterval());

         Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
               timer.cancel();
            }
         });
      }
   }

   final class Fetcher extends TimerTask {
      @Override
      public void run() {
         URL categorySourceUrl = m_configuration.getCategorySourceUrl();

         try {
            m_cachedMapping = fetchMapping(categorySourceUrl);
         } catch (Exception e) {
            m_logger.warn("Can't read category mapping from " + categorySourceUrl, e);
         }
      }

      private Map<Integer, Integer> fetchMapping(URL categorySourceUrl) throws IOException {
         BufferedReader reader = new BufferedReader(new InputStreamReader(categorySourceUrl.openStream()));
         Map<Integer, Integer> mapping = new HashMap<Integer, Integer>(512);

         try {
            while (true) {
               String line = reader.readLine();

               if (line == null) {
                  break;
               } else if (line.trim().length() == 0) {
                  continue;
               }

               String[] parts = line.split("\t");

               if (parts.length == 2) {
                  int category2 = Integer.parseInt(parts[0].trim());
                  int category1 = Integer.parseInt(parts[1].trim());

                  mapping.put(category2, category1);
               }
            }
         } finally {
            reader.close();
         }

         return mapping;
      }

   }
}
