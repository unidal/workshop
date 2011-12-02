package com.site.kernel.dal.db.helpers;

import java.util.HashMap;
import java.util.Map;

import com.site.kernel.dal.db.TableProvider;

/**
 * @author qwu
 */
public class TableProviderManager {
   private static final TableProviderManager s_instance = new TableProviderManager();

   // KEY logicalTableName => VALUE TableProvider
   private Map<String, TableProvider> m_tableProviderMap = new HashMap<String, TableProvider>();

   private TableProviderManager() {
   }

   public static TableProviderManager getInstance() {
      return s_instance;
   }

   public TableProvider getTableProvider(String logicalName) {
      return m_tableProviderMap.get(logicalName);
   }

   public void register(TableProvider provider) {
      m_tableProviderMap.put(provider.getLogicalTableName(), provider);
   }
}
