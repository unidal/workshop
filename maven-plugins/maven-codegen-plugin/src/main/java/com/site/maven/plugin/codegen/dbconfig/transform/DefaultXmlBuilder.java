package com.site.maven.plugin.codegen.dbconfig.transform;

import static com.site.maven.plugin.codegen.dbconfig.Constants.ATTR_ID;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ATTR_NAME;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ATTR_PATTERN;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_CONNECTION_TIMEOUT;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_CONNECTIONPROPERTIES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_DRIVER;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_EXCLUDE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_IDLE_TIMEOUT;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_INCLUDE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_MAXIMUM_POOL_SIZE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_PASSWORD;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_STATEMENT_CACHE_SIZE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_URL;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_USER;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_CONFIG;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_DATA_SOURCE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_DATA_SOURCES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_EXCLUDES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_INCLUDES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_MAPPING;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_MAPPINGS;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_PROPERTIES;

import com.site.maven.plugin.codegen.dbconfig.IVisitor;
import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   private void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   private void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }

   private void startTag(String name, boolean closed, Object... nameValues) {
      startTag(name, null, closed, nameValues);
   }

   private void startTag(String name, Object... nameValues) {
      startTag(name, false, nameValues);
   }
   
   private void startTag(String name, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   private void tagWithText(String name, String text, Object... nameValues) {
      if (text == null) {
         return;
      }
      
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      m_sb.append(">");
      m_sb.append(text);
      m_sb.append("</").append(name).append(">\r\n");
   }

   @Override
   public void visitConfig(Config config) {
      startTag(ENTITY_CONFIG);

      if (!config.getMappings().isEmpty()) {
         startTag(ENTITY_MAPPINGS);

         for (Mapping mapping : config.getMappings()) {
            visitMapping(mapping);
         }

         endTag(ENTITY_MAPPINGS);
      }

      if (config.getIncludes() != null) {
         visitIncludes(config.getIncludes());
      }

      if (config.getExcludes() != null) {
         visitExcludes(config.getExcludes());
      }

      if (!config.getDataSources().isEmpty()) {
         startTag(ENTITY_DATA_SOURCES);

         for (DataSource dataSource : config.getDataSources()) {
            visitDataSource(dataSource);
         }

         endTag(ENTITY_DATA_SOURCES);
      }

      endTag(ENTITY_CONFIG);
   }

   @Override
   public void visitDataSource(DataSource dataSource) {
      startTag(ENTITY_DATA_SOURCE, ATTR_ID, dataSource.getId());

      tagWithText(ELEMENT_MAXIMUM_POOL_SIZE, dataSource.getMaximumPoolSize() == null ? "" : String.valueOf(dataSource.getMaximumPoolSize()));

      tagWithText(ELEMENT_CONNECTION_TIMEOUT, dataSource.getConnectionTimeout());

      tagWithText(ELEMENT_IDLE_TIMEOUT, dataSource.getIdleTimeout());

      tagWithText(ELEMENT_STATEMENT_CACHE_SIZE, dataSource.getStatementCacheSize() == null ? "" : String.valueOf(dataSource.getStatementCacheSize()));

      if (dataSource.getProperties() != null) {
         visitProperties(dataSource.getProperties());
      }

      endTag(ENTITY_DATA_SOURCE);
   }

   @Override
   public void visitExcludes(Excludes excludes) {
      startTag(ENTITY_EXCLUDES);

      if (!excludes.getExcludes().isEmpty()) {
         for (String exclude : excludes.getExcludes()) {
            tagWithText(ELEMENT_EXCLUDE, exclude);
         }
      }

      endTag(ENTITY_EXCLUDES);
   }

   @Override
   public void visitIncludes(Includes includes) {
      startTag(ENTITY_INCLUDES);

      if (!includes.getIncludes().isEmpty()) {
         for (String include : includes.getIncludes()) {
            tagWithText(ELEMENT_INCLUDE, include);
         }
      }

      endTag(ENTITY_INCLUDES);
   }

   @Override
   public void visitMapping(Mapping mapping) {
      startTag(ENTITY_MAPPING, true, ATTR_NAME, mapping.getName(), ATTR_PATTERN, mapping.getPattern());
   }

   @Override
   public void visitProperties(Properties properties) {
      startTag(ENTITY_PROPERTIES);

      tagWithText(ELEMENT_DRIVER, properties.getDriver());

      tagWithText(ELEMENT_URL, properties.getUrl());

      tagWithText(ELEMENT_USER, properties.getUser());

      tagWithText(ELEMENT_PASSWORD, properties.getPassword() == null ? "" : String.valueOf(properties.getPassword()));

      tagWithText(ELEMENT_CONNECTIONPROPERTIES, properties.getConnectionProperties());

      endTag(ENTITY_PROPERTIES);
   }
}
