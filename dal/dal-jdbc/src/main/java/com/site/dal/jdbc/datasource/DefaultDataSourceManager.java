package com.site.dal.jdbc.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.lookup.ContainerHolder;

public class DefaultDataSourceManager extends ContainerHolder implements DataSourceManager {
   private Map<String, DataSource> m_dataSources = new HashMap<String, DataSource>();

   public DataSource getDataSource(String name) {
      DataSource dataSource = m_dataSources.get(name);

      if (dataSource == null) {
         dataSource = lookup(DataSource.class, name);
         m_dataSources.put(name, dataSource);
      }

      return dataSource;
   }

   @Override
   public List<String> getActiveDataSourceNames() {
      List<String> list = new ArrayList<String>(m_dataSources.keySet());

      Collections.sort(list);
      return list;
   }
}
