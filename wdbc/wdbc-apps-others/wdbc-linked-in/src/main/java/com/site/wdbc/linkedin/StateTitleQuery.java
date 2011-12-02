package com.site.wdbc.linkedin;

import java.sql.SQLException;

import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.WdbcFilter;
import com.site.wdbc.query.path.TagTree;
import com.site.wdbc.query.path.WdbcTagTree;

public class StateTitleQuery implements WdbcQuery {
   private DatabaseAccess m_databaseAccess;

   private WdbcFilter m_filter;

   private String m_name;

   private String m_state;

   private String[] m_titles;

   public WdbcTagTree buildTagTree() {
      return TagTree.buildTree(null, false);
   }

   public String getName() {
      return m_name;
   }

   public void handleEvent(WdbcContext context, WdbcResult result, WdbcEventType eventType) {
      switch (eventType) {
      case START_DOCUMENT:
         String[] columns = new String[] { "zipcode", "title" };

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
            try {
               String[] zipcodes = m_databaseAccess.getZipcodeInPending(m_state);

               for (String zipcode : zipcodes) {
                  for (String title : m_titles) {
                     if (!m_databaseAccess.isDone(zipcode, title)) {
                        result.addValue(0, zipcode);
                        result.addValue(1, title);
                     }
                  }
               }
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setState(String state) {
      m_state = state;
   }

   public void setTitles(String[] titles) {
      m_titles = titles;
   }
}
