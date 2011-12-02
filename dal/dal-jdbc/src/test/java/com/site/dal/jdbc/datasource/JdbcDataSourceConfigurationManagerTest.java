package com.site.dal.jdbc.datasource;

import com.site.lookup.ComponentTestCase;

public class JdbcDataSourceConfigurationManagerTest extends ComponentTestCase {
   public void testMarshal() throws Exception {
      JdbcDataSourceConfigurationManager manager = lookup(JdbcDataSourceConfigurationManager.class);
      JdbcDataSourceConfiguration userConfig = manager.getConfiguration("user");
      JdbcDataSourceConfiguration historyConfig = manager.getConfiguration("history");

      assertEquals("user", userConfig.getUser());
      assertEquals("jdbc:mysql://localhost:3306/user?useUnicode=true&autoReconnect=true", userConfig.getUrl());
      assertEquals("history", historyConfig.getUser());
      assertEquals("jdbc:mysql://localhost:3306/history?useUnicode=true&autoReconnect=true", historyConfig.getUrl());
   }
}
