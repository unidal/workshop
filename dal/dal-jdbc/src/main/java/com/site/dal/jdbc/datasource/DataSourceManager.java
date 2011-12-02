package com.site.dal.jdbc.datasource;

import java.util.List;

public interface DataSourceManager {
   public DataSource getDataSource(String dataSourceName);

   public List<String> getActiveDataSourceNames();
}
