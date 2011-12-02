package com.site.wdbc.http.impl;

import static com.site.wdbc.http.impl.HttpMethod.GET;
import static com.site.wdbc.http.impl.HttpMethod.POST;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URLUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.codehaus.plexus.configuration.PlexusConfiguration;

import com.site.lookup.ContainerHolder;
import com.site.lookup.annotation.Inject;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.cache.CacheManager;

public class DefaultSession extends ContainerHolder implements Session {
   @Inject
   private CacheManager m_cacheManager;

   private DefaultHttpClient m_client;

   private List<Header> m_headers;

   private URL m_lastUrl;

   private WdbcSource m_source;

   private Stack<Map<String, Object>> m_propertiesStack;

   public DefaultSession() {
      m_client = new DefaultHttpClient();
      m_headers = new ArrayList<Header>();
      m_propertiesStack = new Stack<Map<String, Object>>();

      m_client.getParams().setParameter("http.socket.timeout", new Integer(5000));
   }

   public Request createRequest(String url) throws MalformedURLException {
      URL remote = new URL(m_lastUrl, url);
      FormRequest request = lookup(FormRequest.class);

      request.setAction(remote.toExternalForm());
      return request;
   }

   public CacheManager getCacheManager() {
      return m_cacheManager;
   }

   public List<Header> getHeaders() {
      return m_headers;
   }

   public HttpClient getHttpClient() {
      return m_client;
   }

   public URL getLastUrl() {
      return m_lastUrl;
   }

   public Map<String, String> getProperties() {
      Map<String, String> properties = new HashMap<String, String>();

      for (Map<String, Object> e : m_propertiesStack) {
         for (Map.Entry<String, Object> entry : e.entrySet()) {
            properties.put(entry.getKey(), String.valueOf(entry.getValue()));
         }
      }

      return properties;
   }

   public Object getProperty(String name, Object defaultValue) {
      Object value = m_propertiesStack.peek().get(name);

      return value == null ? defaultValue : value;
   }

   public WdbcSource getResponseSource() {
      return m_source;
   }

   private String parseValue(Map<String, String> prop, String rawData) {
      if (rawData == null || rawData.length() == 0) {
         return "";
      }

      int len = rawData.length();
      StringBuffer data = new StringBuffer(len);
      StringBuffer var = new StringBuffer(len);
      boolean flag = false;

      for (int i = 0; i < len; i++) {
         char ch = rawData.charAt(i);

         switch (ch) {
         case '$':
            if (i + 1 < len && rawData.charAt(i + 1) == '{') { // variable start
               i++;
               flag = true;
            } else {
               data.append(ch);
            }

            break;
         case '}':
            if (flag) { // variable ends
               String name = var.toString().trim();
               String value = prop.get(name);

               if (value != null) {
                  data.append(value);
               }

               flag = false;
               var.setLength(0);

               break;
            }
         default:
            if (flag) {
               var.append(ch);
            } else {
               data.append(ch);
            }
         }
      }

      return data.toString();
   }

   public String parseValue(String value) {
      if (value.indexOf('$') < 0) {
         return value;
      } else {
         return parseValue(getProperties(), value);
      }
   }

   public void popProperties() {
      m_propertiesStack.pop();
   }

   public void pushProperties() {
      m_propertiesStack.push(new HashMap<String, Object>());
   }

   public URL resolveUrl(Request request) throws MalformedURLException {
      String action = parseValue(request.getAction());
      HttpMethod method = request.getMethod();

      if (method == GET) {
         StringBuffer sb = new StringBuffer(1024);
         NameValuePair[] oldInputs = request.getInputs();
         NameValuePair[] inputs = new NameValuePair[oldInputs.length];
         int index = 0;

         for (NameValuePair input : oldInputs) {
            inputs[index++] = new BasicNameValuePair(input.getName(), parseValue(input.getValue()));
         }

         sb.append(action);

         if (inputs.length > 0) {
            if (action.indexOf('?') > 0) {
               sb.append('&');
            } else {
               sb.append('?');
            }

            sb.append(URLUtils.simpleFormUrlEncode(inputs, HTTP.UTF_8));
         }

         return new URL(sb.toString());
      } else if (method == POST) {
         return new URL(action);
      } else {
         throw new UnsupportedOperationException("Unsupported HTTP method: " + method);
      }
   }

   public void setHeader(String name, String value) {
      m_headers.add(new BasicHeader(name, value));
   }

   public void setHeaders(PlexusConfiguration configuration) {
      PlexusConfiguration[] headers = configuration.getChildren("header");
      int count = headers.length;

      for (int i = 0; i < count; i++) {
         PlexusConfiguration child = headers[i];
         String name = child.getAttribute("name", "");
         String value = child.getValue("");

         if (name.length() == 0) {
            throw new IllegalArgumentException("Name attribute required for header");
         }

         m_headers.add(new BasicHeader(name, value));
      }
   }

   public void setLastUrl(URL lastUrl) {
      m_lastUrl = lastUrl;
   }

   public void setProperty(String name, Object value) {
      m_propertiesStack.peek().put(name, value == null ? "" : value);
   }

   public void setResponseSource(WdbcSource source) {
      m_source = source;
   }
}
