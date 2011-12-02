package com.site.app.bes;

import java.util.ArrayList;
import java.util.List;

import com.site.app.bes.console.DefaultComponentsConfigurator;
import com.site.app.bes.dal.EventDao;
import com.site.app.bes.dal.EventPlusDao;
import com.site.dal.jdbc.AbstractJdbcResourceConfigurator;
import com.site.dal.jdbc.QueryEngine;
import com.site.lookup.configuration.Component;

class ComponentsConfigurator extends AbstractJdbcResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(defineDatabaseComponents());
      all.addAll(new DefaultComponentsConfigurator().defineComponents());

      return all;
   }

   private List<Component> defineDatabaseComponents() {
      List<Component> all = new ArrayList<Component>();
      String dataSource = "jdbc-bes";

      all.add(defineJdbcDataSourceComponent(dataSource, "${jdbc.driver}", "${jdbc.url}", "${jdbc.user}",
               "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));
      all.add(defineSimpleTableProviderComponent(dataSource, "event", "event"));
      all.add(defineSimpleTableProviderComponent(dataSource, "event-plus", "event_plus"));

      all.add(C(EventDao.class).req(QueryEngine.class));
      all.add(C(EventPlusDao.class).req(QueryEngine.class));

      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
