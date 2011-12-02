package com.site.wdbc.http.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.codehaus.plexus.util.IOUtil;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Response;

public class FileCacheManager implements CacheManager {
   @Inject
   private Filter m_filter;

   private CacheHelper m_helper = new CacheHelper();

   private File m_baseDir = new File("target/cache");

   public Response getCache(URL url) throws IOException {
      File cacheFile = new File(m_baseDir, m_helper.resolvePath(url));

      if (cacheFile.exists() && cacheFile.isFile()) {
         return m_helper.loadCache(cacheFile);
      } else {
         return null;
      }
   }

   public void setBaseDir(File baseDir) {
      m_baseDir = baseDir;
   }

   public void setCache(URL url, Response response) throws IOException {
      File cacheFile = new File(m_baseDir, m_helper.resolvePath(url));

      m_helper.storeCache(cacheFile, response);
   }

   public boolean supportCache(URL url) {
      if (m_filter != null) {
         return m_filter.isSupport(url);
      } else {
         return true;
      }
   }

   public static interface Filter {
      public boolean isSupport(URL url);
   }

   static class CacheHelper {
      public Response loadCache(File file) throws IOException {
         ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

         try {
            CacheResponse response = (CacheResponse) ois.readObject();

            return response;
         } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
         }
      }

      public String resolvePath(URL url) {
         StringBuilder sb = new StringBuilder(64);
         String path = url.getPath();
         String qs = url.getQuery();

         if (path != null && path.length() > 1 && path.startsWith("/")) {
            sb.append(path.substring(1));
         } else {
            sb.append("index.html");
         }

         if (qs != null) {
            sb.append('!');
            sb.append(qs);
         }

         for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);

            switch (ch) {
            case '\\':
            case ':':
            case '|':
            case '*':
            case '?':
            case '"':
            case '<':
            case '>':
               sb.setCharAt(i, '_');
               break;
            }
         }

         return sb.toString();
      }

      public void storeCache(File file, Response response) throws IOException {
         CacheResponse cacheResponse = new CacheResponse(response);
         ByteArrayOutputStream baos = new ByteArrayOutputStream(64 * 1024);
         ObjectOutputStream oos = new ObjectOutputStream(baos);

         oos.writeObject(cacheResponse);
         file.getParentFile().mkdirs();

         FileOutputStream fos = new FileOutputStream(file);

         fos.write(baos.toByteArray());
         fos.close();
      }
   }

   static class CacheResponse implements Response, Externalizable {
      private String m_version = "1.0";

      private WdbcSourceType m_contentType;

      private String m_charset;

      private byte[] m_content;

      public CacheResponse() {
      }

      public CacheResponse(Response response) throws IOException {
         m_contentType = response.getContentType();
         m_charset = response.getCharset();
         m_content = IOUtil.toByteArray(response.getContent());
      }

      public CacheResponse(WdbcSourceType contentType, String charset, String content) {
         m_contentType = contentType;
         m_charset = charset;
         m_content = content.getBytes();
      }

      @Override
      public boolean equals(Object obj) {
         if (obj instanceof CacheResponse) {
            CacheResponse other = (CacheResponse) obj;

            if (m_contentType == other.m_contentType && m_charset.equals(other.m_charset)
                  && m_content.length == other.m_content.length) {
               return new String(m_content).equals(new String(other.m_content));
            }
         }

         return false;
      }

      public String getCharset() {
         return m_charset;
      }

      public InputStream getContent() throws IOException {
         return new ByteArrayInputStream(m_content);
      }

      public WdbcSourceType getContentType() {
         return m_contentType;
      }

      public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
         m_version = in.readUTF();
         m_contentType = WdbcSourceType.valueOf(in.readUTF());
         m_charset = in.readUTF();

         int len = in.readInt();

         m_content = new byte[len];
         in.readFully(m_content);
      }

      public void writeExternal(ObjectOutput out) throws IOException {
         out.writeUTF(m_version);
         out.writeUTF(m_contentType.name());
         out.writeUTF(m_charset);
         out.writeInt(m_content.length);
         out.write(m_content);
      }
   }
}
