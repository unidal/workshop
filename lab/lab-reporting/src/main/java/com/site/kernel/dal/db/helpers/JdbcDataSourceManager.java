package com.site.kernel.dal.db.helpers;

import javax.sql.ConnectionPoolDataSource;

import com.site.kernel.SystemRegistry;
import com.site.kernel.dal.datasource.DataSource;
import com.site.kernel.dal.datasource.DataSourceType;
import com.site.kernel.dal.db.DatabaseDataSourceManager;
import com.site.kernel.dal.db.JdbcDataSource;

/**
 * @author qwu
 */
public class JdbcDataSourceManager implements DatabaseDataSourceManager {
   public ConnectionPoolDataSource getDataSource(String logicalName) {
      JdbcDataSource jdbcDataSource = (JdbcDataSource) SystemRegistry.lookup(DataSource.class, DataSourceType.JDBC + ":" + logicalName);
      return jdbcDataSource.getDataSource();
   }
}
