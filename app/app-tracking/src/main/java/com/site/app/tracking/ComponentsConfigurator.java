package com.site.app.tracking;

import java.util.ArrayList;
import java.util.List;

import com.site.app.tracking.analysis.AnalysisComponentsConfigurator;
import com.site.app.tracking.counter.CounterComponentsConfigurator;
import com.site.app.tracking.dal.IpTableDao;
import com.site.app.tracking.dal.PageVisitDao;
import com.site.app.tracking.dal.PageVisitLogDao;
import com.site.app.tracking.dal.PageVisitTrackDao;
import com.site.app.tracking.dal.StatsDao;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.lookup.configuration.Component;

class ComponentsConfigurator extends AbstractJdbcResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(defineDatabaseComponents());

      all.addAll(new CounterComponentsConfigurator().defineComponents());
      all.addAll(new AnalysisComponentsConfigurator().defineComponents());

      return all;
   }

   private List<Component> defineDatabaseComponents() {
      List<Component> all = new ArrayList<Component>();
      String tracking = "jdbc-tracking";
      String visitcount = "jdbc-visitcount";
      String stats = "jdbc-stats";

      all.add(defineJdbcDataSourceConfigurationManagerComponent("datasource.xml"));
      all.add(defineJdbcDataSourceComponent(tracking, "${jdbc.driver}", "${jdbc.url.tracking}", "${jdbc.user}",
               "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));
      all.add(defineJdbcDataSourceComponent(visitcount, "${jdbc.driver}", "${jdbc.url.visitcount}", "${jdbc.user}",
               "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));
      all.add(defineJdbcDataSourceComponent(stats, "${jdbc.driver}", "${jdbc.url.stats}", "${jdbc.user}",
               "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));
      all.add(defineSimpleTableProviderComponent(tracking, "page-visit", "page_visit"));
      all.add(defineSimpleTableProviderComponent(tracking, "page-visit-log", "page_visit_log"));
      all.add(defineSimpleTableProviderComponent(tracking, "page-visit-track", "page_visit_track"));
      all.add(defineSimpleTableProviderComponent(visitcount, "ip-table", "ipother"));
      all.add(defineSimpleTableProviderComponent(stats, "stats", "stats"));

      all.add(C(PageVisitDao.class).req(QueryEngine.class));
      all.add(C(PageVisitLogDao.class).req(QueryEngine.class));
      all.add(C(PageVisitTrackDao.class).req(QueryEngine.class));
      all.add(C(IpTableDao.class).req(QueryEngine.class));
      all.add(C(StatsDao.class).req(QueryEngine.class));

      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
