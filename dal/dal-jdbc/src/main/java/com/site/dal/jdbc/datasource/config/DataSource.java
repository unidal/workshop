package com.site.dal.jdbc.datasource.config;


import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "data-source")
public class DataSource {
   @XmlAttribute(name = "id")
   private String m_id;

   @XmlElement(name = "maximum-pool-size")
   private int m_maximumPoolSize;

   @XmlElement(name = "connection-timeout")
   private String m_connectionTimeout;

   @XmlElement(name = "idle-timeout")
   private String m_idleTimeout;

   @XmlElement(name = "statement-cache-size")
   private int m_statementCacheSize;

   @XmlElement(name = "properties")
   private Properties m_properties;

   public String getConnectionTimeout() {
      return m_connectionTimeout;
   }

   public String getId() {
      return m_id;
   }

   public String getIdleTimeout() {
      return m_idleTimeout;
   }

   public int getMaximumPoolSize() {
      return m_maximumPoolSize;
   }

   public Properties getProperties() {
      return m_properties;
   }

   public int getStatementCacheSize() {
      return m_statementCacheSize;
   }

   public void setConnectionTimeout(String connectionTimeout) {
      m_connectionTimeout = connectionTimeout;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setIdleTimeout(String idleTimeout) {
      m_idleTimeout = idleTimeout;
   }

   public void setMaximumPoolSize(int maximumPoolSize) {
      m_maximumPoolSize = maximumPoolSize;
   }

   public void setProperties(Properties properties) {
      m_properties = properties;
   }

   public void setStatementCacheSize(int statementCacheSize) {
      m_statementCacheSize = statementCacheSize;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("DataSource[");
      sb.append("id=").append(m_id);
      sb.append(",maximumPoolSize=").append(m_maximumPoolSize);
      sb.append(",connectionTimeout=").append(m_connectionTimeout);
      sb.append(",idleTimeout=").append(m_idleTimeout);
      sb.append(",statementCacheSize=").append(m_statementCacheSize);
      sb.append(",properties=").append(m_properties);
      sb.append("]");

      return sb.toString();
   }

}
