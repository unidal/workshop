package com.site.kernel.dal.db;

import javax.sql.ConnectionPoolDataSource;

public interface DatabaseDataSourceManager {

	public static String NAME = DatabaseDataSourceManager.class.getName();

	public abstract ConnectionPoolDataSource getDataSource(String logicalName);
}