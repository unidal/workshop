package com.site.dal.jdbc.transaction;

import java.sql.Connection;

import com.site.dal.jdbc.engine.QueryContext;

public interface TransactionManager {
   public Connection getConnection(QueryContext ctx);

   public void startTransaction(QueryContext ctx);

   public void commitTransaction(QueryContext ctx);
   
   public boolean isInTransaction();

   public void rollbackTransaction(QueryContext ctx);

   public void closeConnection();
}
