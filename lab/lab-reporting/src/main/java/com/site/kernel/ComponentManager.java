package com.site.kernel;

import java.util.HashMap;
import java.util.Map;

import com.site.kernel.dal.db.DatabaseDataSourceManager;
import com.site.kernel.dal.db.helpers.JdbcDataSourceManager;
import com.site.kernel.dal.db.helpers.TokenResolver;
import com.site.kernel.dal.db.helpers.token.MysqlTokenResolver;

public class ComponentManager {
   private static Map<String, Object> s_components = new HashMap<String, Object>();

   private static boolean s_initialized;

   private static void initialize() {
      SystemRegistry.register(Object.class, DatabaseDataSourceManager.NAME, JdbcDataSourceManager.class);
      SystemRegistry.register(Object.class, TokenResolver.NAME, MysqlTokenResolver.class);
      s_initialized = true;
   }

   public static Object lookup(String componentName) {
      if (!s_initialized) {
         initialize();
      }

      Object component = s_components.get(componentName);

      if (component == null) {
         component = SystemRegistry.newInstance(Object.class, componentName);

         s_components.put(componentName, component);
      }

      return component;
   }

   public static void shutdown() {
      SystemRegistry.register(DatabaseDataSourceManager.class, DatabaseDataSourceManager.NAME, null);
      SystemRegistry.register(TokenResolver.class, TokenResolver.NAME, null);
   }
}
