package com.site.wdbc.http.impl;

import static com.site.wdbc.http.impl.HttpMethod.GET;
import static com.site.wdbc.http.impl.HttpMethod.POST;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.RoutedRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.UrlEncodedFormEntity;
import org.apache.http.conn.HttpRoute;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.lookup.ContainerHolder;
import com.site.lookup.annotation.Inject;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Response;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.cache.CacheManager;

public class FormRequest extends ContainerHolder implements Request, LogEnabled {
   @Inject
   private Request m_prerequest;

   private boolean m_prerequestDone;

   private String m_action;

   private String m_postData;

   private HttpMethod m_method = GET;

   private NameValuePair[] m_inputs;

   private Header[] m_headers;

   private Logger m_logger;

   private int m_maxRetries = 3;

   private boolean m_ignoreEmptyAction;

   private Response doRequest(Session session, URL url) throws HttpException, IOException, ConnectException {
      RoutedRequest request = getRoutedRequest(session, url);
      HttpResponse response = null;
      int retried = 0;

      while (true) {
         try {
            m_logger.info("Invoking request to " + request.getRoute().getTargetHost());
            m_logger.info("Request line: " + request.getRequest().getRequestLine());
            response = session.getHttpClient().execute(request);

            break;
         } catch (ConnectException e) {
            m_logger.warn(e.toString() + ", try again");
            retried++;

            if (retried >= m_maxRetries) {
               throw e;
            }
         }
      }

      session.setLastUrl(url);

      if (response != null) {
         m_logger.info("Response status line: " + response.getStatusLine());
      }

      return new DefaultResponse(response, m_logger);
   }

   private void doRequirement(Session session) throws HttpException, IOException {
      if (m_prerequest != null) {
         if (!m_prerequestDone) {
            m_prerequest.execute(session);
            m_prerequestDone = true;
         }
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public Response execute(Session session) throws HttpException, IOException {
      doRequirement(session);

      if (m_ignoreEmptyAction) {
         String action = getAction(session);

         if (action == null || action.length() == 0) {
            return null;
         }
      }

      URL url = session.resolveUrl(this);
      CacheManager cacheManager = session.getCacheManager();
      Response response;

      if (cacheManager != null && cacheManager.supportCache(url)) {
         response = cacheManager.getCache(url);

         if (response == null) {
            response = doRequest(session, url);
            cacheManager.setCache(url, response);
         } else {
            m_logger.info("Page loaded from cache.");
         }
      } else {
         response = doRequest(session, url);
      }

      return response;
   }

   public String getAction() {
      return m_action;
   }

   private String getAction(Session session) {
      return session.parseValue(m_action);
   }

   private Header[] getHeaders(Session session) {
      List<Header> headers = new ArrayList<Header>();
      Set<String> added = new HashSet<String>();

      if (m_headers != null) {
         for (Header header : m_headers) {
            headers.add(new BasicHeader(header.getName(), session.parseValue(header.getValue())));
            added.add(header.getName().toLowerCase());
         }
      }

      for (Header header : session.getHeaders()) {
         if (!added.contains(header.getName().toLowerCase())) {
            headers.add(header);
            added.add(header.getName().toLowerCase());
         }
      }

      return headers.toArray(new Header[0]);
   }

   public NameValuePair[] getInputs() {
      return m_inputs == null ? new NameValuePair[0] : m_inputs;
   }

   private NameValuePair[] getInputs(Session session) {
      int len = (m_inputs == null ? 0 : m_inputs.length);
      List<NameValuePair> inputs = new ArrayList<NameValuePair>();

      for (int i = 0; i < len; i++) {
         NameValuePair pair = m_inputs[i];

         inputs.add(new BasicNameValuePair(pair.getName(), session.parseValue(pair.getValue())));
      }

      if (m_method == HttpMethod.POST && m_postData != null) {
         String postData = session.parseValue(m_postData);
         String[] nameValues = postData.split("&");
         int count = nameValues.length;

         for (int i = 0; i < count; i++) {
            String nameValue = nameValues[i];
            int index = nameValue.indexOf('=');
            String name;
            String value;

            if (index >= 0) {
               name = nameValue.substring(0, index);
               value = nameValue.substring(index + 1);
            } else {
               name = nameValue;
               value = "";
            }

            inputs.add(new BasicNameValuePair(name, value));
         }
      }

      return inputs.toArray(new NameValuePair[0]);
   }

   public HttpMethod getMethod() {
      return m_method;
   }

   protected RoutedRequest getRoutedRequest(Session session, URL url) throws HttpException {
      RoutedRequest request;

      try {
         HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
         HttpRoute route = new HttpRoute(targetHost, null, url.getProtocol().toLowerCase().endsWith("s")); // https
         String relativeUrl = replaceBadCharacters(url.getPath() + (url.getQuery() == null ? "" : "?" + url.getQuery()));

         if (m_method == GET) {
            HttpGet get = new HttpGet(relativeUrl);

            request = new RoutedRequest.Impl(get, route);
         } else if (m_method == POST) {
            HttpPost post = new HttpPost(relativeUrl);

            post.setEntity(new UrlEncodedFormEntity(getInputs(session)));
            request = new RoutedRequest.Impl(post, route);
         } else {
            throw new UnsupportedOperationException("No implementation for method: " + m_method);
         }

         for (Header header : getHeaders(session)) {
            request.getRequest().addHeader(header);
         }
      } catch (URISyntaxException e) {
         throw new HttpException("URI syntax error: " + getAction(session), e);
      }

      return request;
   }

   private String replaceBadCharacters(String link) {
      int len = link.length();
      StringBuffer sb = new StringBuffer(len + 16);

      for (int i = 0; i < len; i++) {
         char ch = link.charAt(i);
         switch (ch) {
         case '|':
            sb.append("%7C");
            break;
         case '^':
            sb.append("%5E");
            break;
         default:
            sb.append(ch);
         }
      }

      return sb.toString();
   }

   public void setAction(String action) {
      m_action = action;
   }

   public void setHeaders(PlexusConfiguration configuration) {
      PlexusConfiguration[] headers = configuration.getChildren("header");
      int count = headers.length;
      m_headers = new Header[count];

      for (int i = 0; i < count; i++) {
         PlexusConfiguration child = headers[i];
         String name = child.getAttribute("name", "");
         String value = child.getValue("");

         if (name.length() == 0) {
            throw new IllegalArgumentException("Name attribute required for header");
         }

         m_headers[i] = new BasicHeader(name, value);
      }
   }

   public void setIgnoreEmptyAction(boolean ignoreEmptyAction) {
      m_ignoreEmptyAction = ignoreEmptyAction;
   }

   public void setInputs(PlexusConfiguration configuration) {
      PlexusConfiguration[] inputs = configuration.getChildren("input");
      int count = inputs.length;
      m_inputs = new NameValuePair[count];

      for (int i = 0; i < count; i++) {
         PlexusConfiguration child = inputs[i];
         String name = child.getAttribute("name", "");
         String value = child.getValue("");

         if (name.length() == 0) {
            throw new IllegalArgumentException("Name attribute required for input");
         }

         m_inputs[i] = new BasicNameValuePair(name, value);
      }
   }

   public void setMaxRetries(int maxRetries) {
      m_maxRetries = maxRetries;
   }

   public void setMethod(String method) {
      m_method = HttpMethod.valueOf(method);
   }

   public void setPostData(String postData) {
      m_postData = postData;
   }
}
