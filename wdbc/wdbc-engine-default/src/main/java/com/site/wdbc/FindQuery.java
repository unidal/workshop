package com.site.wdbc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.configuration.PlexusConfiguration;

import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.WdbcFilter;
import com.site.wdbc.query.path.TagTree;
import com.site.wdbc.query.path.WdbcTagTree;
import com.site.wdbc.query.path.WordPattern;

public class FindQuery implements WdbcQuery {
   private WdbcFilter m_filter;

   private String m_name;

   private List<WordPattern> m_patterns;

   private WdbcTagTree m_tagTree;

   public FindQuery() {
      m_tagTree = TagTree.buildTree(null, true);
   }

   public WdbcTagTree buildTagTree() {
      return m_tagTree;
   }

   public String getName() {
      return m_name;
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

         for (int i = 0; i < size; i++) {
            WordPattern pattern = m_patterns.get(i);

            result.addValue(i, pattern.getWord());
         }
         break;
      case END_DOCUMENT:
         result.applyLastRow();

         if (m_filter != null) {
            m_filter.doFilter(result);
         }

         break;
      default:
         for (int i = 0; i < size; i++) {
            WordPattern pattern = m_patterns.get(i);

            if (pattern.matchesEvent(eventType) && pattern.matchesWord(context)) {
               String path = context.getTrailPath(pattern);

               result.addValue(i, path);
            }
         }
      }
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setWords(PlexusConfiguration configuration) {
      PlexusConfiguration[] words = configuration.getChildren("word");

      m_patterns = new ArrayList<WordPattern>();

      for (PlexusConfiguration word : words) {
         String name = word.getAttribute("name", null);
         String pattern = word.getValue(null);

         m_patterns.add(new WordPattern(name, pattern));
      }
   }
}
