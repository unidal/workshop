package com.site.dal.jdbc.datasource;

import com.site.lookup.ComponentTestCase;

public class DataSourceTest extends ComponentTestCase {
   public void testJdbcDataSource() throws Exception {
      try {
         DataSource dataSource = lookup(DataSource.class, "jdbc-dal");

         assertNotNull(dataSource.getPooledConnection().getConnection());
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            System.out.println("Can't connect to database via JDBC, gave up");
         } else {
            throw e;
         }
      }
   }

   public void testJndiDataSource() throws Exception {
      try {
         DataSource dataSource = lookup(DataSource.class, "jndi-dal");

         assertNotNull(dataSource.getPooledConnection().getConnection());
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            // ignore it since we don't have an InitialContext setup
            System.out.println("Can't connect to database via JNDI, gave up");
         } else {
            throw e;
         }
      }
   }
}
