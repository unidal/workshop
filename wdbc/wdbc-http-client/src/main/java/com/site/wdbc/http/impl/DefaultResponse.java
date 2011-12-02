package com.site.wdbc.http.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;

import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Response;

public class DefaultResponse implements Response {
   private String m_charset;

   private WdbcSourceType m_contentType;

   private byte[] m_content;

   public DefaultResponse(HttpResponse response, Logger logger) throws IOException {
      HttpEntity entity = response.getEntity();

      if (entity == null) {
         if (logger == null) {
            System.out.println("[INFO] Content is empty");
         } else {
            logger.info("Content is empty");
         }
      } else {
         int contentLength = (int) entity.getContentLength();
         ByteArrayOutputStream baos = new ByteArrayOutputStream(contentLength <= 0 ? 64 * 1024 : contentLength);
         Header contentTypeHeader = entity.getContentType();
         String contentType = (contentTypeHeader == null ? "" : contentTypeHeader.getValue());
         String[] parts = contentType.split("(;|=)");
         String mimeType = parts[0].trim();

         if (mimeType.equalsIgnoreCase("text/html")) {
            m_contentType = WdbcSourceType.HTML;
         } else if (mimeType.equalsIgnoreCase("text/xml")) {
            m_contentType = WdbcSourceType.XML;
         } else {
            m_contentType = WdbcSourceType.HTML;
         }

         m_charset = (parts.length == 3 ? parts[2] : null);

         IOUtil.copy(entity.getContent(), baos);
         entity.consumeContent();
         m_content = baos.toByteArray();

         if (m_charset == null) {
            m_charset = findCharsetFromMetadata(new String(m_content, 0, Math.min(m_content.length, 4096))
                  .toLowerCase());
         }

         if (logger == null) {
            System.out.println(String.format("[INFO] Response length:%s, mime-type:%s, charset:%s.", m_content.length,
                  mimeType, m_charset));
         } else {
            logger.info(String.format("Response length:%s, mime-type:%s, charset:%s.", m_content.length, mimeType,
                  m_charset));
         }
      }
   }

   // <meta http-equiv="content-type" content="text/html; charset=gb2312" />
   // <meta http-equiv="content-type" content="text/html; charset=gb2312" >
   // <meta http-equiv=content-type content=text/html; charset=gb2312 />
   // <meta http-equiv=content-type content=text/html; charset=gb2312 >
   private static String findCharsetFromMetadata(String content) {
      int pos1 = content.indexOf("meta ");
      int pos2 = content.indexOf("content-type", pos1 + 1);
      int pos3 = content.indexOf("charset=", pos2 + 1);
      int pos4 = content.indexOf('>', pos3 + 1);

      if (0 < pos1 && pos1 < pos2 && pos2 < pos3 && pos3 < pos4) {
         String str = content.substring(pos3 + 8, pos4).trim();
         int len = str.length();

         if (str.charAt(len - 1) == '/') {
            str = str.substring(0, len - 1).trim();
            len = str.length();
         }

         if (str.charAt(len - 1) == '"') {
            return str.substring(0, len - 1);
         } else {
            return str;
         }
      } else {
         return null;
      }
   }

   public String getCharset() {
      return m_charset == null ? "utf-8" : m_charset;
   }

   public InputStream getContent() throws IOException {
      return new ByteArrayInputStream(m_content);
   }

   public WdbcSourceType getContentType() {
      return m_contentType;
   }
}
