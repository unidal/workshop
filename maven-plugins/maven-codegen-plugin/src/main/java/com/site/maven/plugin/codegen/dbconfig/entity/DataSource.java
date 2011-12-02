package com.site.maven.plugin.codegen.dbconfig.entity;

import com.site.maven.plugin.codegen.dbconfig.BaseEntity;
import com.site.maven.plugin.codegen.dbconfig.IVisitor;

public class DataSource extends BaseEntity<DataSource> {
   private String m_id;

   private Integer m_maximumPoolSize;

   private String m_connectionTimeout;

   private String m_idleTimeout;

   private Integer m_statementCacheSize;

   private Properties m_properties;

   public DataSource() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitDataSource(this);
   }

   public String getConnectionTimeout() {
      return m_connectionTimeout;
   }

   public String getId() {
      return m_id;
   }

   public String getIdleTimeout() {
      return m_idleTimeout;
   }

   public Integer getMaximumPoolSize() {
      return m_maximumPoolSize;
   }

   public Properties getProperties() {
      return m_properties;
   }

   public Integer getStatementCacheSize() {
      return m_statementCacheSize;
   }

   @Override
   public void mergeAttributes(DataSource other) {
      if (other.getId() != null) {
         m_id = other.getId();
      }
   }

   public DataSource setConnectionTimeout(String connectionTimeout) {
      m_connectionTimeout=connectionTimeout;
      return this;
   }

   public DataSource setId(String id) {
      m_id=id;
      return this;
   }

   public DataSource setIdleTimeout(String idleTimeout) {
      m_idleTimeout=idleTimeout;
      return this;
   }

   public DataSource setMaximumPoolSize(Integer maximumPoolSize) {
      m_maximumPoolSize=maximumPoolSize;
      return this;
   }

   public DataSource setProperties(Properties properties) {
      m_properties=properties;
      return this;
   }

   public DataSource setStatementCacheSize(Integer statementCacheSize) {
      m_statementCacheSize=statementCacheSize;
      return this;
   }

}
