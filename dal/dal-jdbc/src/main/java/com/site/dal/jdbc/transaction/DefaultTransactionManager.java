package com.site.dal.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.PooledConnection;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.datasource.DataSource;
import com.site.dal.jdbc.datasource.DataSourceManager;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.mapping.TableProvider;
import com.site.dal.jdbc.mapping.TableProviderManager;

public class DefaultTransactionManager implements TransactionManager, LogEnabled {

   private static ThreadLocalTransactionInfo m_threadLocalData = new ThreadLocalTransactionInfo();

   private TableProviderManager m_tableProviderManager;

   private DataSourceManager m_dataSourceManager;

   private Logger m_logger;

   public void closeConnection() {
      TransactionInfo trxInfo = m_threadLocalData.get();

      if (trxInfo.isInTransaction()) {
         // do nothing when in transaction
      } else {
         try {
            trxInfo.reset();
         } catch (SQLException e) {
            m_logger.warn("Error when closing Connection, message: " + e, e);
         }
      }
   }

   public void commitTransaction(QueryContext ctx) {
      TransactionInfo trxInfo = m_threadLocalData.get();

      if (!trxInfo.isInTransaction()) {
         throw new DalRuntimeException("There is no active transaction open, can't commit");
      }

      try {
         if (trxInfo.getConnection() != null) {
            trxInfo.getConnection().commit();
         }

         trxInfo.reset();
      } catch (SQLException e) {
         throw new DalRuntimeException("Unable to commit transaction, message: " + e, e);
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public Connection getConnection(QueryContext ctx) {
      TableProvider tableProvider = m_tableProviderManager.getTableProvider(ctx.getEntityInfo().getLogicalName());
      String dataSourceName = tableProvider.getDataSourceName(ctx.getQueryHints());
      TransactionInfo trxInfo = m_threadLocalData.get();

      if (trxInfo.isInTransaction()) {
         if (dataSourceName.equals(trxInfo.getDataSourceName())) {
            return trxInfo.getConnection();
         } else {
            throw new DalRuntimeException("Only one datasource can participate in a transaction. Now: "
                     + trxInfo.getDataSourceName() + ", you provided: " + dataSourceName);
         }
      } else { // Not in transaction
         DataSource dataSource = m_dataSourceManager.getDataSource(dataSourceName);
         PooledConnection pooledConnection = null;
         Connection connection = null;
         SQLException exception = null;

         try {
            if (dataSourceName.equals(trxInfo.getDataSourceName())) {
               pooledConnection = trxInfo.getPooledConnection();

               if (pooledConnection == null) {
                  pooledConnection = dataSource.getPooledConnection();
               }
            } else {
               pooledConnection = dataSource.getPooledConnection();
            }

            connection = pooledConnection.getConnection();
            connection.setAutoCommit(true);
         } catch (SQLException e) {
            exception = e;
         }

         // retry once if pooled connection is closed by server side
         if (exception != null) {
            if (pooledConnection != null) {
               try {
                  pooledConnection.close();
               } catch (SQLException e) {
                  // ignore it
               }
            }

            m_logger.warn(String.format("Iffy database(%s) connection closed, try to reconnect.", dataSourceName),
                     exception);

            try {
               pooledConnection = dataSource.getPooledConnection();
               connection = pooledConnection.getConnection();
               connection.setAutoCommit(true);
            } catch (SQLException e) {
               m_logger.warn(String.format("Unable to reconnect to database(%s).", dataSourceName), e);
            }
         }

         if (exception != null) {
            throw new DalRuntimeException("Error when getting connection from DataSource(" + dataSourceName
                     + "), message: " + exception, exception);
         } else {
            trxInfo.setPooledConnection(pooledConnection);
            trxInfo.setConnection(connection);
            trxInfo.setDataSourceName(dataSourceName);
            trxInfo.setInTransaction(false);
            return connection;
         }
      }
   }

   public boolean isInTransaction() {
      TransactionInfo trxInfo = m_threadLocalData.get();

      return trxInfo.isInTransaction();
   }

   public void rollbackTransaction(QueryContext ctx) {
      TransactionInfo trxInfo = m_threadLocalData.get();

      if (!trxInfo.isInTransaction()) {
         throw new DalRuntimeException("There is no active transaction open, can't rollback");
      }

      try {
         if (trxInfo.getConnection() != null) {
            trxInfo.getConnection().rollback();
         }

         trxInfo.reset();
      } catch (SQLException e) {
         throw new DalRuntimeException("Unable to rollback transaction, message: " + e, e);
      }
   }

   public void startTransaction(QueryContext ctx) {
      TableProvider tableProvider = m_tableProviderManager.getTableProvider(ctx.getEntityInfo().getLogicalName());
      String dataSourceName = tableProvider.getDataSourceName(ctx.getQueryHints());
      TransactionInfo trxInfo = m_threadLocalData.get();

      if (trxInfo.isInTransaction()) {
         throw new DalRuntimeException(
                  "Can't start transaction while another transaction is not committed or rollbacked");
      } else {
         DataSource dataSource = m_dataSourceManager.getDataSource(dataSourceName);

         try {
            PooledConnection pooledConnection = dataSource.getPooledConnection();
            Connection connection = pooledConnection.getConnection();

            connection.setAutoCommit(false);
            trxInfo.setPooledConnection(pooledConnection);
            trxInfo.setConnection(connection);
            trxInfo.setDataSourceName(dataSourceName);
            trxInfo.setInTransaction(true);
         } catch (SQLException e) {
            throw new DalRuntimeException("Error when getting connection from DataSource(" + dataSourceName
                     + "), message: " + e, e);
         }
      }
   }

   static class ThreadLocalTransactionInfo extends ThreadLocal<TransactionInfo> {
      @Override
      protected TransactionInfo initialValue() {
         return new TransactionInfo();
      }
   }

   static class TransactionInfo {
      private String m_dataSourceName;

      private PooledConnection m_pooledConnection;

      private Connection m_connection;

      private boolean m_inTransaction;

      public Connection getConnection() {
         return m_connection;
      }

      public String getDataSourceName() {
         return m_dataSourceName;
      }

      public PooledConnection getPooledConnection() {
         return m_pooledConnection;
      }

      public boolean isInTransaction() {
         return m_inTransaction;
      }

      public void reset() throws SQLException {
         if (m_connection != null) {
            m_connection.close();
         }

         m_connection = null;
         m_dataSourceName = null;
         m_inTransaction = false;
      }

      public void setConnection(Connection connection) {
         m_connection = connection;
      }

      public void setDataSourceName(String dataSourceName) {
         m_dataSourceName = dataSourceName;
      }

      public void setInTransaction(boolean inTransaction) {
         m_inTransaction = inTransaction;
      }

      public void setPooledConnection(PooledConnection pooledConnection) {
         m_pooledConnection = pooledConnection;
      }

   }
}
