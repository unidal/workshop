package com.site.kernel.dal.db;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.ConnectionPoolDataSource;

import com.site.kernel.dal.datasource.DataSource;
import com.site.kernel.dal.datasource.DataSourceBo;
import com.site.kernel.dal.datasource.DataSourceException;

public class JndiDataSource extends DataSource {
   private ConnectionPoolDataSource m_cpds;

   @Override
   public void initialize(DataSourceBo ds) {
      try {
         Properties props = ds.getConfigPropertiesBo().getProperties();
         String jndiName = props.getProperty("driver");
         Object obj = new InitialContext().lookup(jndiName);

         if (obj instanceof ConnectionPoolDataSource) {
            m_cpds = (ConnectionPoolDataSource) obj;
         } else {
            throw new DataSourceException("Error looking up data source: " + ds.getName() + ", expected: javax.sql.ConnectionPoolDataSource. got: " + obj.getClass().getName());
         }
      } catch (NamingException e) {
         throw new DataSourceException("Error looking up data source: " + ds.getName() + ", message: " + e.toString(), e);
      } catch (RuntimeException e) {
         throw new DataSourceException("Error looking up data source: " + ds.getName() + ", message: " + e.toString(), e);
      }
   }

   public ConnectionPoolDataSource getDataSource() {
      return m_cpds;
   }
}
