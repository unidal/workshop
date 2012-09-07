package com.site.dal.jdbc.transaction;

import java.sql.Connection;

import com.site.dal.jdbc.engine.QueryContext;

public interface TransactionManager {
   public void closeConnection();

   public void commitTransaction(QueryContext ctx);

   public Connection getConnection(QueryContext ctx);
   
   public boolean isInTransaction();

   public void rollbackTransaction(QueryContext ctx);

   public void startTransaction(QueryContext ctx);
}
