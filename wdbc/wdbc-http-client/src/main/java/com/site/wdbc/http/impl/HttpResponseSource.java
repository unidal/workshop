package com.site.wdbc.http.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.codehaus.plexus.util.IOUtil;

import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Response;

public class HttpResponseSource implements WdbcSource {
   private Response m_response;

   public HttpResponseSource(HttpResponse response) throws IOException {
      m_response = new DefaultResponse(response, null);
   }

   public HttpResponseSource(Response response) throws IOException {
      m_response = response;
   }

   public String getContent() throws IOException {
      return IOUtil.toString(getReader());
   }

   public Reader getReader() throws IOException {
      return new InputStreamReader(m_response.getContent(), m_response.getCharset());
   }

   public WdbcSourceType getType() {
      return m_response.getContentType();
   }
}
