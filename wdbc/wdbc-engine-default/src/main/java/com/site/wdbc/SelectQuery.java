package com.site.wdbc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.configuration.PlexusConfiguration;

import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.WdbcFilter;
import com.site.wdbc.query.path.PathPattern;
import com.site.wdbc.query.path.TagTree;
import com.site.wdbc.query.path.WdbcPathPattern;
import com.site.wdbc.query.path.WdbcTagTree;

public class SelectQuery implements WdbcQuery {
   private WdbcFilter m_filter;

   private List<PathPattern> m_patterns = new ArrayList<PathPattern>();

   private WdbcTagTree m_tagTree;

   public String m_name;

   public WdbcTagTree buildTagTree() {
      m_tagTree = TagTree.buildTree(m_patterns, false);

      return m_tagTree;
   }

   public String getName() {
      return m_name;
   }

   // For test only
   public List<PathPattern> getPatterns() {
      return m_patterns;
   }

   public void handleEvent(WdbcContext context, WdbcResult result, WdbcEventType eventType) {
      int size = m_patterns.size();

      switch (eventType) {
      case START_DOCUMENT:
         String[] columns = new String[size];
         for (int i = 0; i < size; i++) {
            String name = m_patterns.get(i).getName();

            if (name == null || name.length() == 0) {
               columns[i] = "c" + i;
            } else {
               columns[i] = name;
            }
         }

         result.begin(columns);
         break;
      case END_DOCUMENT:
         result.applyLastRow();

         if (m_filter != null) {
            m_filter.doFilter(result);
         }

         break;
      default:
         for (int i = 0; i < size; i++) {
            PathPattern pattern = m_patterns.get(i);

            if (pattern.matchesEvent(eventType)) {
               int matched = context.matchesPath(pattern);

               if (matched == WdbcPathPattern.FULL_WILD_MATCHED) {
                  if (pattern.isAllText()) {
                     if (eventType == WdbcEventType.START_TAG) {
                        context.startAllText();
                     } else if (eventType == WdbcEventType.END_TAG) {
                        Object value = pattern.evaluate(context);

                        result.addValue(i, value);
                        context.endAllText();
                     }
                  } else if (pattern.isAllChildren()) {
                     if (eventType == WdbcEventType.START_TAG) {
                        context.startAllChildren();
                     } else if (eventType == WdbcEventType.END_TAG) {
                        Object value = pattern.evaluate(context);

                        result.addValue(i, value);
                        context.endAllChildren();
                     }
                  }
               } else if (matched == WdbcPathPattern.FULL_MATCHED) {
                  Object value = pattern.evaluate(context);

                  result.addValue(i, value);
               }
            }
         }
      }
   }

   public void setFilter(WdbcFilter filter) {
      m_filter = filter;
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setPaths(PlexusConfiguration configuration) {
      PlexusConfiguration[] paths = configuration.getChildren("path");

      for (PlexusConfiguration path : paths) {
         String name = path.getAttribute("name", null);
         String pattern = path.getValue(null);
         PathPattern pathPattern = new PathPattern(name, pattern);

         m_patterns.add(pathPattern);
      }
   }
}
