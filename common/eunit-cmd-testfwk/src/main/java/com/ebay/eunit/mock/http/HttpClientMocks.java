package com.ebay.eunit.mock.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.ebay.eunit.helper.Files;
import com.ebay.eunit.mock.servlet.HttpServletResponseMock;

public class HttpClientMocks {
   public static HttpClient forGet(String url) {
      return new HttpClient(url);
   }

   public static HttpClient forPost(String url, String formData) {
      return new HttpClient(url, formData);
   }

   public static class HttpClient {
      private HttpRequest m_request;

      private HttpResponse m_response;

      public HttpClient(String url) {
         m_request = new HttpRequest(HttpMethod.GET, url);
         m_response = new HttpResponse();
      }

      public HttpClient(String url, String formData) {
         m_request = new HttpRequest(HttpMethod.POST, url, formData);
         m_response = new HttpResponse();
      }

      public HttpClient expectContentLength(int contentLength) {
         m_response.addHeader("Content-Length", String.valueOf(contentLength));
         return this;
      }

      public HttpClient expectContentType(String contentType) {
         m_response.addHeader("Content-Type", contentType);
         return this;
      }

      public HttpClient expectResponseBody(String body) {
         m_response.setBody(body);
         return this;
      }

      public HttpClient expectResponseCode(int responseCode) {
         m_response.setResponseCode(responseCode);
         return this;
      }

      public HttpClient expectResponseHeader(String name, String value) {
         m_response.addHeader(name, value);
         return this;
      }

      public HttpClient expectResponseHeaders(String... headers) {
         for (String header : headers) {
            m_response.addHeader(header, null); // check header name only
         }

         return this;
      }

      public void go() throws IOException {
         ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
         HttpServletResponseMock response = m_request.submitRequest(out);

         m_response.checkResponse(response, out.toByteArray());
      }

      public HttpClient withRequestHeader(String name, String value) {
         m_request.addHeader(name, value);
         return this;
      }
   }

   static enum HttpMethod {
      GET,

      POST;
   }

   static class HttpRequest {
      private HttpMethod m_method;

      private String m_url;

      private Map<String, List<String>> m_headers = new LinkedHashMap<String, List<String>>();

      private String m_formData;

      public HttpRequest(HttpMethod method, String url) {
         this(method, url, null);
      }

      public HttpRequest(HttpMethod method, String url, String formData) {
         if (formData != null && method != HttpMethod.POST) {
            throw new IllegalArgumentException("Can't add formData for non POST request! Method was :" + method);
         }

         m_method = method;
         m_url = url;
         m_formData = formData;
      }

      public void addHeader(String name, String value) {
         List<String> values = m_headers.get(name);

         if (values == null) {
            values = new ArrayList<String>(1);
            m_headers.put(name, values);
         }

         values.add(value);
      }

      public HttpServletResponseMock submitRequest(OutputStream out) throws IOException {
         URL url = new URL(m_url);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();

         if (!m_headers.isEmpty()) {
            for (Map.Entry<String, List<String>> e : m_headers.entrySet()) {
               for (String value : e.getValue()) {
                  conn.addRequestProperty(e.getKey(), value);
               }
            }
         }

         switch (m_method) {
         case GET:
            conn.setRequestMethod("GET");
            break;
         case POST:
            byte[] data = m_formData == null ? null : m_formData.getBytes("utf-8");

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-url-encoded");
            conn.setRequestProperty("Content-Length", String.valueOf(data == null ? 0 : data.length));
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            if (data != null) {
               os.write(data);
               os.close();
            }

            break;
         }

         InputStream is = conn.getInputStream();
         byte[] result = Files.forIO().readFrom(is);
         Map<String, List<String>> responseHeaders = conn.getHeaderFields();
         HttpServletResponseMock response = new HttpServletResponseMock(out);

         response.setStatus(conn.getResponseCode());
         response.setContentLength(conn.getContentLength());
         response.setContentType(conn.getContentType());
         response.setCharacterEncoding(conn.getContentEncoding());
         response.getOutputStream().write(result);

         for (Map.Entry<String, List<String>> e : responseHeaders.entrySet()) {
            List<String> values = e.getValue();

            for (String value : values) {
               response.addHeader(e.getKey(), value);
            }
         }

         return response;
      }
   }

   static class HttpResponse {
      private static final String UTF_8 = "utf-8";

      private int m_responseCode;

      private Map<String, List<String>> m_headers = new LinkedHashMap<String, List<String>>();

      private String m_body;

      public void addHeader(String name, String value) {
         List<String> values = m_headers.get(name);

         if (values == null) {
            values = new ArrayList<String>(1);
            m_headers.put(name, values);
         }

         if (value != null) {
            values.add(value);
         }
      }

      @SuppressWarnings("unchecked")
      public void checkResponse(HttpServletResponseMock response, byte[] actualContent) {
         if (m_responseCode > 0) {
            Assert.assertEquals("Response code mismatched!", m_responseCode, response.getStatus());
         }

         Map<String, List<String>> actualHeaders = new HashMap<String, List<String>>();

         for (Map.Entry<String, Object> e : response.getHeaders().entrySet()) {
            if (e.getKey() == null) {
               continue;
            }

            String key = e.getKey().toLowerCase();
            Object value = e.getValue();

            if (value instanceof List) {
               actualHeaders.put(key, (List<String>) value);
            } else {
               List<String> values = new ArrayList<String>();

               values.add(String.valueOf(value));
               actualHeaders.put(key, values);
            }
         }

         for (Map.Entry<String, List<String>> e : m_headers.entrySet()) {
            List<String> expectedValues = e.getValue();
            List<String> actualValues = actualHeaders.get(e.getKey().toLowerCase());

            if (expectedValues.isEmpty()) { // expect header name only
               boolean found = actualValues != null && actualValues.size() > 0;

               Assert.assertTrue(String.format("Response header(%s) not found!", e.getKey()), found);
            } else {
               Assert.assertEquals(String.format("Response header(%s) mismatched!", e.getKey()), String.valueOf(expectedValues),
                     String.valueOf(actualValues));
            }
         }

         if (m_body != null) {
            String actualBody;

            try {
               actualBody = new String(actualContent, UTF_8);
            } catch (UnsupportedEncodingException e) {
               actualBody = new String(actualContent);
            }

            Assert.assertEquals("Response body mismatched!", m_body, actualBody);
         }
      }

      public void setBody(String body) {
         m_body = body;
      }

      public void setResponseCode(int responseCode) {
         m_responseCode = responseCode;
      }
   }
}
