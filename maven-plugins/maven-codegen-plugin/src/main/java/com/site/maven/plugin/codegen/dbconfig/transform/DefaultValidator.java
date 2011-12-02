package com.site.maven.plugin.codegen.dbconfig.transform;

import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_CONFIG;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_DATA_SOURCE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_DATA_SOURCES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_MAPPINGS;

import java.util.Stack;

import com.site.maven.plugin.codegen.dbconfig.IVisitor;
import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitConfig(Config config) {
      m_path.down(ENTITY_CONFIG);

      visitConfigChildren(config);

      m_path.up(ENTITY_CONFIG);
   }

   protected void visitConfigChildren(Config config) {
      m_path.down(ENTITY_MAPPINGS);

      for (Mapping mapping : config.getMappings()) {
         visitMapping(mapping);
      }

      m_path.up(ENTITY_MAPPINGS);

      if (config.getIncludes() != null) {
         visitIncludes(config.getIncludes());
      }

      if (config.getExcludes() != null) {
         visitExcludes(config.getExcludes());
      }

      m_path.down(ENTITY_DATA_SOURCES);

      for (DataSource dataSource : config.getDataSources()) {
         visitDataSource(dataSource);
      }

      m_path.up(ENTITY_DATA_SOURCES);
   }

   @Override
   public void visitDataSource(DataSource dataSource) {
      m_path.down(ENTITY_DATA_SOURCE);

      visitDataSourceChildren(dataSource);

      m_path.up(ENTITY_DATA_SOURCE);
   }

   protected void visitDataSourceChildren(DataSource dataSource) {
      if (dataSource.getProperties() != null) {
         visitProperties(dataSource.getProperties());
      }
   }

   @Override
   public void visitExcludes(Excludes excludes) {
   }

   @Override
   public void visitIncludes(Includes includes) {
   }

   @Override
   public void visitMapping(Mapping mapping) {
   }

   @Override
   public void visitProperties(Properties properties) {
   }

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
