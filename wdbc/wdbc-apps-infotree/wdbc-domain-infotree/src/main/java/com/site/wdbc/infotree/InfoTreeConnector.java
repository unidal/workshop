package com.site.wdbc.infotree;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.StringOutputStream;

import com.site.wdbc.http.impl.HttpResponseSource;

public class InfoTreeConnector implements LogEnabled {
   private static final boolean DEBUG = false;

   private Logger m_logger;

   public void publish(String action, InfoTreeMessage message) throws URISyntaxException, IOException, HttpException {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpPost post = new HttpPost(action);
      HttpEntity entity = new UrlEncodedFormEntity(message.toNameValuePairs());

      post.setEntity(entity);

      StringOutputStream sos = new StringOutputStream();
      entity.writeTo(sos);

      m_logger.info("Post data to " + action);
      m_logger.info("Parameters: " + sos.toString());

      HttpResponse response = client.execute(post);

      if (DEBUG) {
         System.out.println(new HttpResponseSource(response).getContent());
      }

      m_logger.info("Response status line: " + response.getStatusLine());
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
