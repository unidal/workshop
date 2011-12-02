package org.unidal.configuration;

import java.util.ArrayList;
import java.util.List;

import com.dianping.demo.dal.WpOptionsDao;
import com.dianping.demo.dal._INDEX;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.lookup.configuration.Component;

final class DatabaseConfigurator extends AbstractJdbcResourceConfigurator {
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(defineJdbcDataSourceConfigurationManagerComponent("datasource.xml"));
      all.add(defineJdbcDataSourceComponent("jdbc-dianping", "${jdbc.driver}", "${jdbc.url}", "${jdbc.user}",
            "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));

      defineSimpleTableProviderComponents(all, "jdbc-dianping", _INDEX.getEntityClasses());
      all.add(C(WpOptionsDao.class).req(QueryEngine.class));

      return all;
   }
}