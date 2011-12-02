package com.site.dal.jdbc.configuration;

import java.util.List;

import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.annotation.Entity;
import com.site.dal.jdbc.datasource.DataSource;
import com.site.dal.jdbc.datasource.JdbcDataSource;
import com.site.dal.jdbc.datasource.JdbcDataSourceConfigurationManager;
import com.site.dal.jdbc.mapping.SimpleTableProvider;
import com.site.dal.jdbc.mapping.TableProvider;
import com.site.dal.jdbc.raw.RawDao;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public abstract class AbstractJdbcResourceConfigurator extends AbstractResourceConfigurator {
   private boolean m_jdbcDataSourceConfigurationManagerDefined;

   protected void defineDaoComponents(List<Component> all, Class<?>[] daoClasses) {
      all.add(C(RawDao.class).req(QueryEngine.class));

      for (Class<?> daoClass : daoClasses) {
         all.add(C(daoClass).req(QueryEngine.class));
      }
   }

   protected Component defineJdbcDataSourceComponent(String dataSource, String driver, String url, String user,
            String password, String connectionProperties) {
      if (!m_jdbcDataSourceConfigurationManagerDefined) {
         throw new RuntimeException("defineJdbcDataSourceConfigurationManagerComponent(...) should be called first.");
      }

      return C(DataSource.class, dataSource, JdbcDataSource.class).req(JdbcDataSourceConfigurationManager.class)
               .config(E("id").value(dataSource),
                        E("maximum-pool-size").value("3"),
                        E("connection-timeout").value("1s"),
                        E("idle-timeout").value("10m"),
                        E("statement-cache-size").value("1000"),
                        E("properties").add(E("driver").value(driver), E("URL").value(url), E("user").value(user),
                                 E("password").value(password), E("connectionProperties").value(connectionProperties)));
   }

   protected Component defineJdbcDataSourceConfigurationManagerComponent(String datasourceFile) {
      m_jdbcDataSourceConfigurationManagerDefined = true;
      return C(JdbcDataSourceConfigurationManager.class).config(E("datasourceFile").value(datasourceFile));
   }

   protected Component defineSimpleTableProviderComponent(String dataSource, String logicalTableName) {
      String physicalTableName = logicalTableName.replace('-', '_');

      return defineSimpleTableProviderComponent(dataSource, logicalTableName, physicalTableName);
   }

   protected Component defineSimpleTableProviderComponent(String dataSource, String logicalTableName,
            String physicalTableName) {
      return C(TableProvider.class, logicalTableName, SimpleTableProvider.class).config(
               E("logical-table-name").value(logicalTableName), E("physical-table-name").value(physicalTableName),
               E("data-source-name").value(dataSource));
   }

   protected void defineSimpleTableProviderComponents(List<Component> all, String dataSource, Class<?>[] entitiClasses) {
      all.add(defineSimpleTableProviderComponent(dataSource, "raw", "dual"));

      for (Class<?> entityClass : entitiClasses) {
         Entity entityMeta = entityClass.getAnnotation(Entity.class);
         String logicalTableName = entityMeta.logicalName();
         String physicalTableName = logicalTableName.replace('-', '_');

         all.add(defineSimpleTableProviderComponent(dataSource, logicalTableName, physicalTableName));
      }
   }
}
