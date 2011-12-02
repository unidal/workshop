package com.ebay.eunit.mock.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;

import com.ebay.eunit.mock.servlet.HttpServletRequestMock;
import com.ebay.eunit.mock.servlet.HttpServletResponseMock;

public class HttpServerMocks {
   public static ProtocolBinder forBinding() {
      return ProtocolBinder.INSTANCE;
   }

   static enum HandlerRegistry {
      INSTANCE;

      private Map<String, IHttpHandler> m_handlers = new HashMap<String, IHttpHandler>();

      public IHttpHandler getHandler(String protocol) {
         IHttpHandler handler = m_handlers.get(protocol);

         return handler;
      }

      public void registerHandler(String protocol, IHttpHandler handler) {
         m_handlers.put(protocol, handler);
      }

      public void unregisterHandler(String protocol) {
         m_handlers.remove(protocol);
      }
   }

   static enum ListValueMapHelper {
      INSTANCE;

      public Enumeration<String> getNames(Map<String, List<String>> map) {
         return Collections.enumeration(map.keySet());
      }

      public String getValue(Map<String, List<String>> map, String name) {
         List<String> values = map.get(name);

         return values == null || values.isEmpty() ? null : values.get(0);
      }

      public String[] getValuesArray(Map<String, List<String>> map, String name) {
         List<String> values = map.get(name);
         int len = values == null ? 0 : values.size();
         String[] array = new String[len];

         for (int i = 0; i < len; i++) {
            array[i] = values.get(i);
         }

         return array;
      }

      public Enumeration<String> getValuesEnumeration(Map<String, List<String>> map, String name) {
         List<String> values = map.get(name);

         if (values == null || values.isEmpty()) {
            return Collections.enumeration(Collections.<String> emptyList());
         } else {
            return Collections.enumeration(values);
         }
      }

      public void setValue(Map<String, List<String>> map, String name, List<String> value) {
         map.put(name, value);
      }

      public void setValue(Map<String, List<String>> map, String name, String value) {
         List<String> values = map.get(name);

         if (values == null) {
            values = new ArrayList<String>(1);
            map.put(name, values);
         }

         values.add(value);
      }
   }

   static class MockHttpServletRequest extends HttpServletRequestMock {

      private Map<String, List<String>> m_headers = new HashMap<String, List<String>>();

      private Map<String, List<String>> m_parameters = new HashMap<String, List<String>>();

      private ListValueMapHelper m_helper = ListValueMapHelper.INSTANCE;

      private ByteArrayOutputStream m_formData;

      private String m_method;

      public MockHttpServletRequest(ByteArrayOutputStream formData) {
         m_formData = formData;
      }

      @Override
      public String getHeader(String name) {
         return m_helper.getValue(m_headers, name);
      }

      @Override
      public Enumeration<String> getHeaderNames() {
         return m_helper.getNames(m_headers);
      }

      @Override
      public Enumeration<String> getHeaders(String name) {
         return m_helper.getValuesEnumeration(m_headers, name);
      }

      @Override
      public ServletInputStream getInputStream() throws IOException {
         return new ServletInputStream() {
            private InputStream m_in = new ByteArrayInputStream(m_formData.toByteArray());

            @Override
            public int read() throws IOException {
               return m_in.read();
            }
         };
      }

      @Override
      public int getIntHeader(String name) {
         String value = getHeader(name);

         return value == null ? -1 : Integer.parseInt(value);
      }

      @Override
      public String getMethod() {
         return m_method;
      }

      @Override
      public String getParameter(String name) {
         return m_helper.getValue(m_parameters, name);
      }

      @Override
      public Map<String, String> getParameterMap() {
         Map<String, String> map = new HashMap<String, String>();

         for (Map.Entry<String, List<String>> e : m_parameters.entrySet()) {
            String key = e.getKey();
            List<String> values = e.getValue();
            StringBuilder sb = new StringBuilder(32);

            for (String value : values) {
               if (sb.length() > 0) {
                  sb.append('&');
               }

               sb.append(key).append('=').append(value == null ? "" : value);
            }
         }

         return map;
      }

      @Override
      public Enumeration<String> getParameterNames() {
         return m_helper.getNames(m_parameters);
      }

      @Override
      public String[] getParameterValues(String name) {
         return m_helper.getValuesArray(m_parameters, name);
      }

      @Override
      public void setHeader(String name, List<String> value) {
         m_helper.setValue(m_headers, name, value);
      }

      @Override
      public void setHeader(String name, String value) {
         m_helper.setValue(m_headers, name, value);
      }

      @Override
      public void setMethod(String method) {
         m_method = method;
      }

      @Override
      public void setParameter(String name, String value) {
         m_helper.setValue(m_parameters, name, value);
      }

   }

   static class MockHttpServletResponse extends HttpServletResponseMock {
      private MockHttpServletResponse(OutputStream outputStream) {
         super(outputStream);
      }
   }

   static class MockHttpURLConnection extends HttpURLConnection {
      private IHttpHandler m_handler;

      private MockHttpServletRequest m_request;

      private MockHttpServletResponse m_response;

      private ByteArrayOutputStream m_formData;

      private ByteArrayOutputStream m_result;

      private String m_method;

      public MockHttpURLConnection(URL url, IHttpHandler handler) {
         super(url);

         m_handler = handler;
         m_result = new ByteArrayOutputStream(2048);
         m_formData = new ByteArrayOutputStream(1024);
         m_request = new MockHttpServletRequest(m_formData);
         m_response = new MockHttpServletResponse(m_result);
      }

      @Override
      public void addRequestProperty(String name, String value) {
         List<String> values = new ArrayList<String>();
         Enumeration<String> oldValues = m_request.getHeaders(name);

         if (oldValues != null) {
            values.addAll(Collections.list(oldValues));
         }

         values.add(value);
         m_request.setHeader(name, values);
      }

      @Override
      public void connect() throws IOException {
         // do nothing here
      }

      @Override
      public void disconnect() {
         // do nothing here
      }

      @Override
      public String getContentEncoding() {
         return m_response.getCharacterEncoding();
      }

      @Override
      public int getContentLength() {
         return m_response.getContentLength();
      }

      @Override
      public String getContentType() {
         return m_response.getContentType();
      }

      @SuppressWarnings("unchecked")
      @Override
      public Map<String, List<String>> getHeaderFields() {
         HashMap<String, List<String>> headers = new HashMap<String, List<String>>();

         headers.put("Content-Type", Arrays.asList(m_response.getContentType()));
         headers.put("Content-Length", Arrays.asList(String.valueOf(m_response.getContentLength())));

         for (Map.Entry<String, Object> e : m_response.getHeaders().entrySet()) {
            Object value = e.getValue();
            List<String> values = new ArrayList<String>();

            if (value instanceof List) {
               for (Object item : (List<Object>) value) {
                  values.add(String.valueOf(item));
               }
            } else {
               values.add(String.valueOf(value));
            }

            headers.put(e.getKey(), values);
         }

         return headers;
      }

      @Override
      public InputStream getInputStream() throws IOException {
         prepareRequest();

         try {
            m_handler.service(m_request, m_response);
         } catch (ServletException e) {
            throw new IOException("Error when handle request: " + url + "!", e);
         }

         return new ByteArrayInputStream(m_result.toByteArray());
      }

      @Override
      public OutputStream getOutputStream() throws IOException {
         return m_formData;
      }

      @Override
      public String getRequestMethod() {
         return m_method;
      }

      @Override
      public int getResponseCode() throws IOException {
         return m_response.getStatus();
      }

      private void prepareParameters(String nameValues) {
         String[] pairs = nameValues.split(Pattern.quote("&"));

         for (String pair : pairs) {
            int pos = pair.indexOf('=');

            if (pos < 0) {
               if (pair.length() > 0) {
                  m_request.setParameter(pair, null);
               }
            } else {
               m_request.setParameter(pair.substring(0, pos), pair.substring(pos + 1));
            }
         }
      }

      private void prepareRequest() {
         m_request.setRequestURI(url.getPath());

         String qs = url.getQuery();

         if (qs != null) {
            m_request.setQueryString(qs);

            prepareParameters(qs);
         }

         if ("POST".equalsIgnoreCase(m_request.getMethod())) {
            prepareParameters(m_formData.toString());
         }
      }

      @Override
      public void setRequestMethod(String method) throws ProtocolException {
         m_method = method;
         m_request.setMethod(m_method);
      }

      @Override
      public void setRequestProperty(String name, String value) {
         m_request.setHeader(name, Arrays.asList(value));
      }

      @Override
      public boolean usingProxy() {
         return false;
      }
   }

   static enum MockURLStreamHandlerFactory implements URLStreamHandlerFactory {
      INSTANCE;

      private boolean m_initialized;

      @Override
      public URLStreamHandler createURLStreamHandler(String protocol) {
         final IHttpHandler handler = HandlerRegistry.INSTANCE.getHandler(protocol);

         if (handler != null) {
            return new URLStreamHandler() {
               @Override
               protected URLConnection openConnection(final URL url) throws IOException {
                  return new MockHttpURLConnection(url, handler);
               }
            };
         }

         return null;
      }

      public void register(String protocol, IHttpHandler handler) {
         if (!m_initialized) {
            m_initialized = true;
            URL.setURLStreamHandlerFactory(INSTANCE);
         }

         HandlerRegistry.INSTANCE.registerHandler(protocol, handler);
      }

      public void unregister(String protocol) {
         HandlerRegistry.INSTANCE.unregisterHandler(protocol);
      }
   }

   public static enum ProtocolBinder {
      INSTANCE;

      public void bind(IHttpHandler handler) {
         MockURLStreamHandlerFactory.INSTANCE.register(handler.getProtocol(), handler);
      }

      public void unbind(IHttpHandler handler) {
         MockURLStreamHandlerFactory.INSTANCE.unregister(handler.getProtocol());
      }
   }
}
