package com.site.wdbc.ganji;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   private Session m_session;

   private MessageFormat m_contactFormat = new MessageFormat("{0}&q={1}");

   @Override
   public void doFilter(WdbcResult result) {
      String title = aggregate(result, "title", null);
      String body = aggregate(result, "body", null);
      String contactLink = aggregate(result, "contact-link", null);
      String category = aggregate(result, "category", " > ");
      String district = aggregate(result, "district", " - ");
      String picture = aggregate(result, "picture", null);

      String contact = getContact(getAbsoluteUrl(contactLink));
      String mobilephone = contact.startsWith("1") ? contact : null;

      result.clear();
      result.addValue("title", title);
      result.addValue("body", body.replace('>', ' '));
      result.addValue("contact", contact);
      result.addValue("category", category);
      result.addValue("district", district);
      result.addValue("picture", picture);
      result.addValue("mobilephone", mobilephone);
      result.applyLastRow();
   }

   private String getAbsoluteUrl(String link) {
      URL lastUrl = m_session.getLastUrl();

      try {
         return new URL(lastUrl, link).toExternalForm();
      } catch (MalformedURLException e) {
         return link;
      }
   }

   private String getContact(String contactLink) {
      try {
         HttpClient httpClient = m_session.getHttpClient();

         httpClient.getParams().setParameter(HttpClientParams.HANDLE_REDIRECTS, Boolean.FALSE);

         HttpResponse response = httpClient.execute(new HttpGet(contactLink));
         Header[] locations = response.getHeaders("location");

         response.getEntity().consumeContent();
         httpClient.getParams().setParameter(HttpClientParams.HANDLE_REDIRECTS, Boolean.TRUE);

         String location = (locations == null || locations.length == 0 ? null : locations[0].getValue());

         if (location != null) {
            return (String) m_contactFormat.parse(location)[1];
         }
      } catch (Exception e) {
         // ignore it
         System.err.println(e);
         e.printStackTrace();
      }

      return "";
   }

}
