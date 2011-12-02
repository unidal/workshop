package com.site.kernel.dal.db;

import javax.sql.ConnectionPoolDataSource;

import com.site.kernel.dal.datasource.DataSource;
import com.site.kernel.dal.datasource.DataSourceBo;
import com.site.kernel.dal.datasource.DataSourceException;

public class JdbcDataSource extends DataSource {
   private ConnectionPoolDataSource m_cpds;

   @Override
   public void initialize(DataSourceBo ds) {
      try {
//         DriverAdapterCPDS cpds = new DriverAdapterCPDS();
//         Properties props = ds.getConfigPropertiesBo().getProperties();
//         String connectionProperties = props.getProperty("connectionProperties");
//
//         cpds.setDriver(props.getProperty("driver"));
//
//         if (connectionProperties != null && connectionProperties.length() > 0) {
//            cpds.setUrl(props.getProperty("URL") + "?" + connectionProperties);
//         } else {
//            cpds.setUrl(props.getProperty("URL"));
//         }
//
//         cpds.setUser(props.getProperty("user"));
//         cpds.setPassword(props.getProperty("password"));
//
//         cpds.setMaxIdle(ds.getMaximumPoolSize());
//         cpds.setMaxActive(ds.getMaximumPoolSize());
//         cpds.setLoginTimeout((int) ds.getConnectionTimeout());
//         cpds.setMinEvictableIdleTimeMillis((int) ds.getIdleTimeout());
//
//         m_cpds = cpds;
      } catch (Exception e) {
         throw new DataSourceException("Error initializing data source: " + ds.getName() + ", message: " + e.toString(), e);
      }
   }

   public ConnectionPoolDataSource getDataSource() {
      return m_cpds;
   }
}
