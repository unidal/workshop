package com.site.wdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class ResourceSource implements WdbcSource {
   private WdbcSourceType m_type;

   private String m_resource;

   private String m_charset;

   public ResourceSource(WdbcSourceType type, Class<?> anchorClass, String resource) {
      this(type, anchorClass.getName(), resource, "utf-8");
   }

   public ResourceSource(WdbcSourceType type, Class<?> anchorClass, String resource, String charset) {
      this(type, anchorClass.getName(), resource, charset);
   }

   public ResourceSource(WdbcSourceType type, String resource) {
      this(type, detectAnchorClass(), resource, "utf-8");
   }

   public ResourceSource(WdbcSourceType type, String resource, String charset) {
      this(type, detectAnchorClass(), resource, charset);
   }

   public ResourceSource(WdbcSourceType type, String anchorClass, String resource, String charset) {
      m_type = type;
      m_charset = charset;

      if (!resource.startsWith("/")) {
         int pos = anchorClass.lastIndexOf('.');

         m_resource = "/" + anchorClass.substring(0, pos).replace('.', '/') + "/" + resource;
      } else {
         m_resource = resource;
      }
   }

   private static String detectAnchorClass() {
      StackTraceElement[] stacktrace = new Exception().getStackTrace();

      return stacktrace[2].getClassName();
   }

   public Reader getReader() throws IOException {
      InputStream in = getClass().getResourceAsStream(m_resource);

      if (in == null) {
         throw new FileNotFoundException("Resource(" + m_resource + ") not found");
      }

      try {
         return new InputStreamReader(in, m_charset);
      } catch (UnsupportedEncodingException e) {
         return new InputStreamReader(in);
      }
   }

   public WdbcSourceType getType() {
      return m_type;
   }
}
