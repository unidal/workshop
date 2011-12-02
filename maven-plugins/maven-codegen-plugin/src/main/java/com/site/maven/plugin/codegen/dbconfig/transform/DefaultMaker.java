package com.site.maven.plugin.codegen.dbconfig.transform;

import static com.site.maven.plugin.codegen.dbconfig.Constants.ATTR_ID;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ATTR_NAME;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ATTR_PATTERN;

import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_CONNECTION_TIMEOUT;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_CONNECTIONPROPERTIES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_DRIVER;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_IDLE_TIMEOUT;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_MAXIMUM_POOL_SIZE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_PASSWORD;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_STATEMENT_CACHE_SIZE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_URL;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_USER;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public class DefaultMaker implements IMaker<Node> {

   @Override
   public Config buildConfig(Node node) {
      return new Config();
   }

   @Override
   public DataSource buildDataSource(Node node) {
      String id = getAttribute(node, ATTR_ID);
      String maximumPoolSize = getText(getChildTagNode(node, ELEMENT_MAXIMUM_POOL_SIZE));
      String connectionTimeout = getText(getChildTagNode(node, ELEMENT_CONNECTION_TIMEOUT));
      String idleTimeout = getText(getChildTagNode(node, ELEMENT_IDLE_TIMEOUT));
      String statementCacheSize = getText(getChildTagNode(node, ELEMENT_STATEMENT_CACHE_SIZE));

      DataSource dataSource = new DataSource();

      if (id != null) {
         dataSource.setId(id);
      }

      if (maximumPoolSize != null) {
         dataSource.setMaximumPoolSize(Integer.valueOf(maximumPoolSize));
      }

      if (connectionTimeout != null) {
         dataSource.setConnectionTimeout(connectionTimeout);
      }

      if (idleTimeout != null) {
         dataSource.setIdleTimeout(idleTimeout);
      }

      if (statementCacheSize != null) {
         dataSource.setStatementCacheSize(Integer.valueOf(statementCacheSize));
      }

      return dataSource;
   }

   @Override
   public String buildExclude(Node node) {
      return getText(node);
   }

   @Override
   public Excludes buildExcludes(Node node) {
      Excludes excludes = new Excludes();

      return excludes;
   }

   @Override
   public String buildInclude(Node node) {
      return getText(node);
   }

   @Override
   public Includes buildIncludes(Node node) {
      Includes includes = new Includes();

      return includes;
   }

   @Override
   public Mapping buildMapping(Node node) {
      String name = getAttribute(node, ATTR_NAME);
      String pattern = getAttribute(node, ATTR_PATTERN);

      Mapping mapping = new Mapping();

      if (name != null) {
         mapping.setName(name);
      }

      if (pattern != null) {
         mapping.setPattern(pattern);
      }

      return mapping;
   }

   @Override
   public Properties buildProperties(Node node) {
      String driver = getText(getChildTagNode(node, ELEMENT_DRIVER));
      String url = getText(getChildTagNode(node, ELEMENT_URL));
      String user = getText(getChildTagNode(node, ELEMENT_USER));
      String password = getText(getChildTagNode(node, ELEMENT_PASSWORD));
      String connectionProperties = getText(getChildTagNode(node, ELEMENT_CONNECTIONPROPERTIES));

      Properties properties = new Properties();

      if (driver != null) {
         properties.setDriver(driver);
      }

      if (url != null) {
         properties.setUrl(url);
      }

      if (user != null) {
         properties.setUser(user);
      }

      if (password != null) {
         properties.setPassword(Integer.valueOf(password));
      }

      if (connectionProperties != null) {
         properties.setConnectionProperties(connectionProperties);
      }

      return properties;
   }

   protected String getAttribute(Node node, String name) {
      Node attribute = node.getAttributes().getNamedItem(name);

      return attribute == null ? null : attribute.getNodeValue();
   }

   protected Node getChildTagNode(Node parent, String name) {
      NodeList children = parent.getChildNodes();
      int len = children.getLength();

      for (int i = 0; i < len; i++) {
         Node child = children.item(i);

         if (child.getNodeType() == Node.ELEMENT_NODE) {
            if (child.getNodeName().equals(name)) {
               return child;
            }
         }
      }

      return null;
   }

   protected String getText(Node node) {
      if (node != null) {
         StringBuilder sb = new StringBuilder();
         NodeList children = node.getChildNodes();
         int len = children.getLength();

         for (int i = 0; i < len; i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
               sb.append(child.getNodeValue());
            }
         }

         if (sb.length() != 0) {
            return sb.toString();
         }
      }

      return null;
   }
}
