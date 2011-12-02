package com.site.dal.jdbc.datasource;

import java.sql.SQLException;

import javax.sql.PooledConnection;

public interface DataSource {
   public PooledConnection getPooledConnection() throws SQLException;
}
