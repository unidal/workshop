package org.unidal.configuration;

import java.util.ArrayList;
import java.util.List;

import com.lietou.myapp.dal.IpsDao;
import com.lietou.myapp.dal.WebUserDao;
import com.lietou.myapp.dal._INDEX;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.lookup.configuration.Component;

final class DatabaseConfigurator extends AbstractJdbcResourceConfigurator {
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(defineJdbcDataSourceConfigurationManagerComponent("datasource.xml"));
      all.add(defineJdbcDataSourceComponent("jdbc-lietou", "${jdbc.driver}", "${jdbc.url}", "${jdbc.user}", "${jdbc.password}",
            "<![CDATA[${jdbc.connectionProperties}]]>"));

      defineSimpleTableProviderComponents(all, "jdbc-lietou", _INDEX.getEntityClasses());

      all.add(C(IpsDao.class).req(QueryEngine.class));
      all.add(C(WebUserDao.class).req(QueryEngine.class));

      return all;
   }
}