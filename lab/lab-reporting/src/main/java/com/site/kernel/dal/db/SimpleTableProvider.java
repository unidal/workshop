package com.site.kernel.dal.db;

import java.util.Map;

/**
 * @author qwu
 */
public class SimpleTableProvider implements TableProvider {
   private String m_logicalTableName;
   private String m_logicalDataSourceName;

   public SimpleTableProvider(String logicalDataSourceName, Entity entity) {
      m_logicalDataSourceName = logicalDataSourceName;
      m_logicalTableName = entity.getLogicalName();
   }

   public String getLogicalTableName() {
      return m_logicalTableName;
   }

   public String getLogicalDataSource(Map hints) {
      return m_logicalDataSourceName;
   }

   public String getPhysicalTableName(Map hints) {
      return m_logicalTableName;
   }

}
