package com.site.kernel;

import com.site.kernel.dal.datasource.DataSource;
import com.site.kernel.dal.datasource.DataSourceType;
import com.site.kernel.dal.db.JdbcDataSource;
import com.site.kernel.dal.db.JndiDataSource;
import com.site.kernel.initialization.BaseModule;
import com.site.kernel.initialization.ModuleContext;

public class Module extends BaseModule {
   public static final Module FULL = defineFull();

   private Module(BaseModule[] modules) {
      super(modules);
   }

   private static Module defineFull() {
      final BaseModule[] DEPENDENT_MODULES = {};

      return new Module(DEPENDENT_MODULES);
   }

   protected void initialize(ModuleContext ctx) {
      SystemRegistry.register(DataSource.class, DataSourceType.JDBC, JdbcDataSource.class);
      SystemRegistry.register(DataSource.class, DataSourceType.JNDI, JndiDataSource.class);
      
//      DataSourceInitializer.initialize();
   }

   protected void shutdown() {
   }
}
