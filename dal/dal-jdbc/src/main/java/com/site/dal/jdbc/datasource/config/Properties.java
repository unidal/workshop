package com.site.dal.jdbc.datasource.config;


import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "properties")
public class Properties {
   @XmlElement(name = "driver")
   private String m_driver;

   @XmlElement(name = "url")
   private String m_url;

   @XmlElement(name = "user")
   private String m_user;

   @XmlElement(name = "password")
   private String m_password;

   @XmlElement(name = "connectionProperties")
   private String m_connectionProperties;

   public String getConnectionProperties() {
      return m_connectionProperties;
   }

   public String getDriver() {
      return m_driver;
   }

   public String getPassword() {
      return m_password;
   }

   public String getUrl() {
      return m_url;
   }

   public String getUser() {
      return m_user;
   }

   public void setConnectionProperties(String connectionProperties) {
      m_connectionProperties = connectionProperties;
   }

   public void setDriver(String driver) {
      m_driver = driver;
   }

   public void setPassword(String password) {
      m_password = password;
   }

   public void setUrl(String url) {
      m_url = url;
   }

   public void setUser(String user) {
      m_user = user;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("Properties[");
      sb.append("driver=").append(m_driver);
      sb.append(",url=").append(m_url);
      sb.append(",user=").append(m_user);
      sb.append(",password=").append(m_password);
      sb.append(",connectionProperties=").append(m_connectionProperties);
      sb.append("]");

      return sb.toString();
   }

}
