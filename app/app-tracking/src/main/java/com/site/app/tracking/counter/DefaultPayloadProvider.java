package com.site.app.tracking.counter;

import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class DefaultPayloadProvider implements PayloadProvider, Initializable, LogEnabled {
   private MessageFormat m_detailPageFormat = new MessageFormat("http://{0}/detail/{1,number,#}{2}");

   private MessageFormat m_pageUrlFormat = new MessageFormat("/detail/{1,number,#}.html");

   private MessageFormat m_categoryPageFormat = new MessageFormat("/{0}/{1,number,#}-{2,number,#}{3}");

   private MessageFormat m_categoryPageUrlFormat = new MessageFormat("/{0}/"); // 51,59,5159

   private CategoryMapping m_categoryMapping;

   private Configuration m_configuration;

   private String m_contextPath;

   private int m_prefixLength;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void initialize() throws InitializationException {
      if (m_contextPath == null) {
         m_contextPath = m_configuration.getContextPath() + m_configuration.getServletPath();
      }

      if (m_contextPath != null) {
         if (m_contextPath.endsWith("/")) {
            m_prefixLength = m_contextPath.length() - 1;
         } else {
            m_prefixLength = m_contextPath.length();
            m_contextPath += "/";
         }
      }
   }

   public Payload getPayload(String referer, String queryString) {
      Payload payload = null;
      String fromPage = "";
      String onTop = "0";
      String subCategory = null;
      String pageUrl = null;

      try {
         if (queryString != null) {
            Map<String, String> qsParams = parse(queryString);
            String t = qsParams.get("t");
            String r = qsParams.get("r");

            if (isNotEmpty(t)) {
               onTop = t;
            }

            if (isNotEmpty(r)) {
               URL url = new URL(r);
               String query = url.getQuery();

               fromPage = url.getPath();

               if (m_contextPath != null && fromPage != null && fromPage.startsWith(m_contextPath)) {
                  fromPage = fromPage.substring(m_prefixLength);
               }

               if (isNotEmpty(query)) {
                  Map<String, String> qp = parse(query);
                  String c = qp.get("cat");

                  if (isNotEmpty(c)) {
                     subCategory = c;
                  }
               }
            }
         }

         // Get current page url
         if (referer != null) {
            synchronized (this) {
               Object[] parts = m_detailPageFormat.parse(referer);

               pageUrl = m_pageUrlFormat.format(parts);
            }
         }
      } catch (Exception e) {
         m_logger.error("Invalid header Referer: " + referer, e);
      }

      // For from category page
      if (subCategory == null && fromPage != null
            && (fromPage.startsWith("/51/") || fromPage.startsWith("/59/") || fromPage.startsWith("/5159/"))) {
         try {
            synchronized (this) {
               Object[] parts = m_categoryPageFormat.parse(fromPage);

               subCategory = parts[2].toString();
               fromPage = m_categoryPageUrlFormat.format(parts);
            }
         } catch (ParseException e) {
            // ignore it
         }
      }

      if (pageUrl != null) {
         payload = new Payload();

         try {
            int category2 = subCategory == null ? 0 : Integer.parseInt(subCategory);
            int category1 = m_categoryMapping.getCategory1(category2);

            payload.setPageUrl(pageUrl);
            payload.setFromPage(fromPage);
            payload.setOnTop("1".equals(onTop));
            payload.setCategory1(category1);
            payload.setCategory2(category2);
         } catch (NumberFormatException e) {
            // ignore it
         }
      }

      return payload;
   }

   private boolean isNotEmpty(String str) {
      return str != null && str.length() > 0;
   }

   private Map<String, String> parse(String str) {
      Map<String, String> map = new HashMap<String, String>();
      StringBuilder name = new StringBuilder();
      StringBuilder value = new StringBuilder();
      boolean inName = true;
      int len = str == null ? 0 : str.length();

      for (int i = 0; i < len + 1; i++) {
         char ch = (i == len ? '&' : str.charAt(i));

         switch (ch) {
         case '&':
            if (name.length() > 0) {
               map.put(name.toString(), value.toString());
            }

            name.setLength(0);
            value.setLength(0);
            inName = true;
            break;
         case '=':
            if (inName) {
               inName = false;
            } else {
               value.append(ch);
            }

            break;
         case '%':
            if (i + 2 < len) {
               String hex = str.substring(i + 1, i + 3);
               ch = (char) (Integer.parseInt(hex, 16));
               i += 2;
            }
         default:
            if (inName) {
               name.append(ch);
            } else {
               value.append(ch);
            }
         }
      }

      return map;
   }
}
