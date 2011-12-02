package com.site.maven.plugin.codegen.dbconfig.entity;

import com.site.maven.plugin.codegen.dbconfig.BaseEntity;
import com.site.maven.plugin.codegen.dbconfig.IVisitor;

public class Properties extends BaseEntity<Properties> {
   private String m_driver;

   private String m_url;

   private String m_user;

   private Integer m_password;

   private String m_connectionProperties;

   public Properties() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitProperties(this);
   }

   public String getConnectionProperties() {
      return m_connectionProperties;
   }

   public String getDriver() {
      return m_driver;
   }

   public Integer getPassword() {
      return m_password;
   }

   public String getUrl() {
      return m_url;
   }

   public String getUser() {
      return m_user;
   }

   @Override
   public void mergeAttributes(Properties other) {
   }

   public Properties setConnectionProperties(String connectionProperties) {
      m_connectionProperties=connectionProperties;
      return this;
   }

   public Properties setDriver(String driver) {
      m_driver=driver;
      return this;
   }

   public Properties setPassword(Integer password) {
      m_password=password;
      return this;
   }

   public Properties setUrl(String url) {
      m_url=url;
      return this;
   }

   public Properties setUser(String user) {
      m_user=user;
      return this;
   }

}
