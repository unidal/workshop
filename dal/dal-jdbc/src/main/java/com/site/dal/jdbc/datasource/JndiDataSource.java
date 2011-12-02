package com.site.dal.jdbc.datasource;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class JndiDataSource implements DataSource, Initializable {
   private String m_jndiName;

   private ConnectionPoolDataSource m_cpds;

   public PooledConnection getPooledConnection() throws SQLException {
      return m_cpds.getPooledConnection();
   }

   public void initialize() throws InitializationException {
      Object obj;

      try {
         obj = new InitialContext().lookup(m_jndiName);
      } catch (NamingException e) {
         throw new DataSourceException("No JNDI entry(" + m_jndiName + ") defined for DataSource, message: " + e, e);
      }

      if (obj instanceof ConnectionPoolDataSource) {
         m_cpds = (ConnectionPoolDataSource) obj;
      } else {
         throw new DataSourceException("Error when looking up data source(" + m_jndiName
               + "), expected: javax.sql.ConnectionPoolDataSource. got: " + obj.getClass().getName());
      }

      makeFirstConnection();
   }

   protected void makeFirstConnection() {
      try {
         PooledConnection connection = m_cpds.getPooledConnection();

         connection.close();
      } catch (SQLException e) {
         throw new DataSourceException("Error when connecting to data source(" + m_jndiName + "), message: " + e, e);
      }
   }

   public void setJndiName(String jndiName) {
      m_jndiName = jndiName;
   }
}
