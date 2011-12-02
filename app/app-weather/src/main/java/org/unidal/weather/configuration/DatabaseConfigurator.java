package org.unidal.weather.configuration;

import java.util.ArrayList;
import java.util.List;

import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.lookup.configuration.Component;

final class DatabaseConfigurator extends AbstractJdbcResourceConfigurator {
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();
      String unidal = "jdbc-unidal";

      all.add(defineJdbcDataSourceConfigurationManagerComponent("datasource.xml"));
      all.add(defineJdbcDataSourceComponent(unidal, "${jdbc.driver}", "${jdbc.url.unidal}", "${jdbc.user}",
            "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));
      all.add(defineSimpleTableProviderComponent(unidal, "weather"));

      return all;
   }
}