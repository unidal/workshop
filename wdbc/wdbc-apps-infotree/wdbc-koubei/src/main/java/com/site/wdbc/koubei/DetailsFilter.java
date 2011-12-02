package com.site.wdbc.koubei;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   private Session m_session;

   private MessageFormat m_descLinkInFormat = new MessageFormat(
         "{0}/store/include/detail/detail_introduce.jsp?storeid={1,number,#}{2}");

   private MessageFormat m_descLinkOutFormat = new MessageFormat(
         "/store/include/detail/detail_introduce.jsp?storeid={1,number,#}");

   @Override
   public void doFilter(WdbcResult result) {
      String descLink = aggregate(result, "desc-link", null);
      String rank = aggregate(result, "rank", "\r\n");
      String category = aggregate(result, "category", " > ");
      String picture = aggregate(result, "picture", null);
      String contact = aggregate(result, "contact", null);

      result.clear();
      result.addValue("desc-link", getAbsoluteUrl(getDescLink(descLink)));
      result.addValue("rank", rank.replace('>', ' '));
      result.addValue("contact", contact);
      result.addValue("category", category);
      result.addValue("picture", picture);
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

   private String getDescLink(String link) {
      try {
         Object[] parts = m_descLinkInFormat.parse(link);
         return m_descLinkOutFormat.format(parts);
      } catch (ParseException e) {
         return null;
      }
   }
}
