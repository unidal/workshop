package com.site.app.tracking.counter;

import java.net.MalformedURLException;
import java.net.URL;

public class Configuration {
   // AdvancedProcessor checking interval to flush buffer data to database
   private long m_checkInterval = 15 * 1000L; // 15 seconds

   // DefaultCategoryMapping mapping refresh interval
   private long m_refreshInterval = 60 * 60 * 1000L; // 1 hour

   private URL m_categorySourceUrl;

   private String m_contextPath;

   private String m_servletPath;

   public URL getCategorySourceUrl() {
      return m_categorySourceUrl;
   }

   public long getCheckInterval() {
      return m_checkInterval;
   }

   public String getContextPath() {
      return m_contextPath;
   }

   public long getRefreshInterval() {
      return m_refreshInterval;
   }

   public String getServletPath() {
      return m_servletPath;
   }

   private long getTime(String str) {
      long time = 0;
      int len = str.length();

      int num = 0;
      for (int i = 0; i < len; i++) {
         char ch = str.charAt(i);

         switch (ch) {
         case 'd':
            time += num * 24 * 60 * 60;
            num = 0;
            break;
         case 'h':
            time += num * 60 * 60;
            num = 0;
            break;
         case 'm':
            time += num * 60;
            num = 0;
            break;
         case 's':
            time += num;
            num = 0;
            break;
         default:
            if (ch >= '0' && ch <= '9') {
               num = num * 10 + (ch - '0');
            } else {
               throw new IllegalArgumentException("Invalid character found: " + ch + " in '" + str
                     + "', should be one of [0-9][dhms]");
            }
         }
      }

      return time * 1000 + num;
   }

   public void setCategorySourceUrl(String categorySourceUrl) throws MalformedURLException {
      if (categorySourceUrl != null && categorySourceUrl.length() > 0) {
         m_categorySourceUrl = new URL(categorySourceUrl);
      }
   }

   public void setCheckInterval(String checkInterval) throws NumberFormatException {
      if (checkInterval != null) {
         m_checkInterval = getTime(checkInterval);
      }
   }

   public void setContextPath(String contextPath) {
      m_contextPath = contextPath;
   }

   public void setRefreshInterval(String checkInterval) throws NumberFormatException {
      if (checkInterval != null) {
         m_checkInterval = getTime(checkInterval);
      }
   }

   public void setServletPath(String servletPath) {
      m_servletPath = servletPath;
   }
}
