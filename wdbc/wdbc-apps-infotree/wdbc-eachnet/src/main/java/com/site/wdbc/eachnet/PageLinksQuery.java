package com.site.wdbc.eachnet;

import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.WdbcFilter;
import com.site.wdbc.query.path.TagTree;
import com.site.wdbc.query.path.WdbcTagTree;

public class PageLinksQuery implements WdbcQuery {
   private WdbcFilter m_filter;

   private Configuration m_configuration;

   private Session m_session;

   private String m_name;

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
            String link = m_configuration.getCategoryLink();
            int maxPages = m_configuration.getMaxPages();

            for (int i = 0; i < maxPages; i++) {
               result.addValue("link", link);
            }
            
            m_session.setProperty("next-page:link", link);
         }
      }
   }

   public void setName(String name) {
      m_name = name;
   }
}
