package com.site.dal.jdbc.mapping;

import java.util.HashMap;
import java.util.Map;

import com.site.lookup.ContainerHolder;

public class DefaultTableProviderManager extends ContainerHolder implements TableProviderManager {
   private Map<String, TableProvider> m_tableProviders = new HashMap<String, TableProvider>();

   public TableProvider getTableProvider(String logicalName) {
      TableProvider tableProvider = m_tableProviders.get(logicalName);

      if (tableProvider == null) {
         tableProvider = lookup(TableProvider.class, logicalName);
         m_tableProviders.put(logicalName, tableProvider);
      }

      return tableProvider;
   }
}
