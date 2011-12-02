package com.site.wdbc.whycools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.WdbcFilter;
import com.site.wdbc.query.path.TagTree;
import com.site.wdbc.query.path.WdbcTagTree;

public class PageLinksQuery implements WdbcQuery {
   private WdbcFilter m_filter;

   private Configuration m_configuration;

   private String m_name;

   private String m_uriPattern;

   public WdbcTagTree buildTagTree() {
      return TagTree.buildTree(null, false);
   }

   public String getName() {
      return m_name;
   }

   public void handleEvent(WdbcContext context, WdbcResult result, WdbcEventType eventType) {
      switch (eventType) {
      case START_DOCUMENT:
         String[] columns = new String[] { "link" };

         result.begin(columns);
         break;
      case END_DOCUMENT:
         result.applyLastRow();

         if (m_filter != null) {
            m_filter.doFilter(result);
         }

         break;
      default:
         if (result.getRowSize() == 0) { // only add once
            MessageFormat format = new MessageFormat(m_uriPattern);
            String city = m_configuration.getCityId();
            String category = m_configuration.getCategory();
            int maxPages = m_configuration.getMaxPages();
            String type = m_configuration.getType();
            String provinceName = encode(m_configuration.getProvinceName());

            for (int page = 1; page <= maxPages; page++) {
               String link = format.format(new Object[] { category, (page - 1) * 30, city, type, provinceName });

               result.addValue("link", link);
            }
         }
      }
   }

   private String encode(String str) {
      try {
         return URLEncoder.encode(str, "GBK");
      } catch (UnsupportedEncodingException e) {
         return str;
      }
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setUriPattern(String uriPattern) {
      m_uriPattern = uriPattern;
   }
}
