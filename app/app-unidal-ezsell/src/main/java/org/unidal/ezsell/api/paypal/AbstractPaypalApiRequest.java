package org.unidal.ezsell.api.paypal;

import static com.site.lookup.util.StringUtils.isEmpty;
import static com.site.web.jsp.function.Encoder.urlEncode;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RoutedRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpRoute;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.site.lookup.annotation.Inject;

public abstract class AbstractPaypalApiRequest implements Initializable, LogEnabled {
   @Inject
   private String m_endPoint;

   @Inject
   private String m_apiUsername;

   @Inject
   private String m_apiPassword;

   @Inject
   private String m_apiSignature;

   @Inject
   private int m_maxRetries = 2;

   private Logger m_logger;

   /**
    * 
    <?xml version="1.0" encoding="utf-8"?> <GeteBayOfficialTimeRequest
    * xmlns="urn:ebay:apis:eBLBaseComponents"> <RequesterCredentials>
    * <eBayAuthToken> Token goes here </eBayAuthToken> </RequesterCredentials>
    * <Version>383</Version> </GeteBayOfficialTimeRequest>
    * 
    * @param info
    * @param callSpecificInputFields
    * @return
    * @throws HttpException
    * @throws IOException
    */
   protected String doCallForNvp(RouteInfo info, JSONObject payload) throws HttpException, IOException {
      RoutedRequest request;

      try {
         request = makeNvpRequest(info, payload);
      } catch (Exception e) {
         throw new RuntimeException("Error when constructing request URL for " + info.getApiName() + " with " + payload
               + ".", e);
      }

      HttpClient httpClient = makeHttpClient();
      HttpResponse response = null;
      int retried = 0;
      long startTime;

      while (true) {
         startTime = System.currentTimeMillis();

         try {
            m_logger.info("Invoking request to " + request.getRoute().getTargetHost());
            m_logger.info("Request line: " + request.getRequest().getRequestLine());

            response = httpClient.execute(request);

            m_logger.info("Response status: " + response.getStatusLine());
            break;
         } catch (ConnectException e) {
            m_logger.warn(e.toString() + ", try again.");
            retried++;

            if (retried >= m_maxRetries) {
               throw e;
            }
         }
      }

      HttpEntity entity = response.getEntity();

      if (entity != null) {
         m_logger.info("Content-Length: " + entity.getContentLength() + ", Content-Type: " + entity.getContentType());

         String encoding = "utf-8";

         try {
            return IOUtil.toString(entity.getContent(), encoding);
         } catch (Exception e) {
            throw new RuntimeException("Error when retrieving and parsing response: " + e, e);
         } finally {
            entity.consumeContent();

            long endTime = System.currentTimeMillis();
            m_logger.info("Time elapsed: " + (endTime - startTime) + " ms");
         }
      }

      return null;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void initialize() throws InitializationException {
      if (m_endPoint == null || !m_endPoint.contains("paypal.com")) {
         throw new InitializationException("endPoint must be set correctly.");
      }
   }

   protected DefaultHttpClient makeHttpClient() {
      DefaultHttpClient client = new DefaultHttpClient();

      client.getParams().setParameter("http.socket.timeout", new Integer(60000));
      return client;
   }

   protected RoutedRequest makeNvpRequest(RouteInfo info, JSONObject payload) throws MalformedURLException,
         URISyntaxException {
      URL url = makeNvpUrl(info, payload);
      HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
      HttpRoute route = new HttpRoute(targetHost, null, url.getProtocol().toLowerCase().endsWith("s")); // https
      HttpGet get = new HttpGet(url.toURI());
      RoutedRequest.Impl request = new RoutedRequest.Impl(get, route);

      return request;
   }

   protected URL makeNvpUrl(RouteInfo info, JSONObject payload) throws MalformedURLException {
      if (isEmpty(m_apiUsername) || isEmpty(m_apiPassword) || isEmpty(m_apiSignature)) {
         throw new RuntimeException("apiUsername, apiPassword and apiSignature must be set correctly.");
      }

      StringBuilder sb = new StringBuilder(2048);

      sb.append(m_endPoint);

      if (m_endPoint.indexOf('?') > 0) {
         sb.append('&');
      } else {
         sb.append('?');
      }

      sb.append("METHOD=").append(info.getApiName());
      sb.append("&VERSION=").append(info.getVersion());
      sb.append("&USER=").append(m_apiUsername);
      sb.append("&PWD=").append(m_apiPassword);
      sb.append("&SIGNATURE=").append(m_apiSignature);

      if (payload != null) {
         JSONArray names = payload.names();
         int len = names.length();

         for (int i = 0; i < len; i++) {
            String name = names.optString(i);
            String value = payload.optString(name);

            sb.append('&').append(name.toUpperCase()).append('=').append(urlEncode(value));
         }
      }

      return new URL(sb.toString());
   }

   protected JSONObject newJSONObject(JSONObject parent, String key) throws JSONException {
      JSONObject child = new JSONObject();

      parent.put(key, child);
      return child;
   }

   public void setApiPassword(String apiPassword) {
      m_apiPassword = apiPassword;
   }

   public void setApiSignature(String apiSignature) {
      m_apiSignature = apiSignature;
   }

   public void setApiUsername(String apiUsername) {
      m_apiUsername = apiUsername;
   }

   public void setEndPoint(String endPoint) {
      m_endPoint = endPoint;
   }

   public void setMaxRetries(int maxRetries) {
      m_maxRetries = maxRetries;
   }

   protected static class RouteInfo {
      private String m_apiName;

      private String m_version;

      public RouteInfo(String apiName, String version) {
         m_apiName = apiName;
         m_version = version;
      }

      public String getApiName() {
         return m_apiName;
      }

      public String getVersion() {
         return m_version;
      }
   }
}
