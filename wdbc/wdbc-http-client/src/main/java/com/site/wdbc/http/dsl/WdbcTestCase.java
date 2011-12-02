package com.site.wdbc.http.dsl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;

import com.site.lookup.ComponentTestCase;
import com.site.lookup.configuration.Component;
import com.site.lookup.configuration.Configurators;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Flow;
import com.site.wdbc.http.dsl.WdbcFlowConfigurator.FindQueryPlan;
import com.site.wdbc.http.dsl.WdbcFlowConfigurator.SelectQueryPlan;

public abstract class WdbcTestCase extends ComponentTestCase {
   private AbstractConfigurator m_configurator;

   public WdbcTestCase(AbstractConfigurator configurator) {
      m_configurator = configurator;
      m_configurator.configure();
   }

   protected static void main(WdbcTestCase crawler) {
      try {
         crawler.setUp();
         crawler.executeFlow();
         crawler.tearDown();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   protected void executeFindQuery(String selectedQuery) throws Exception {
      List<FindQueryPlan> plans = WdbcFlowConfigurator.with(m_configurator).getFindQueryPlans();
      WdbcEngine engine = lookup(WdbcEngine.class);

      for (FindQueryPlan plan : plans) {
         if (selectedQuery == null || selectedQuery.equals(plan.getName())) {
            WdbcResult result = engine.execute(plan.getQuery(), plan.getSource());

            System.out.println("FindQuery: " + plan.getName() + ", " + result);
         }
      }
   }

   protected void executeFlow() throws Exception {
      Flow flow = lookup(Flow.class);

      flow.execute();
   }

   protected void executeSelectQuery(String selectedQuery) throws Exception {
      List<SelectQueryPlan> plans = WdbcFlowConfigurator.with(m_configurator).getSelectQueryPlans();
      WdbcEngine engine = lookup(WdbcEngine.class);

      for (SelectQueryPlan plan : plans) {
         String queryName = plan.getName();

         if (selectedQuery == null || selectedQuery.equals(queryName)) {
            WdbcQuery query = lookup(WdbcQuery.class, queryName);
            WdbcResult result = engine.execute(query, plan.getSource());

            System.out.println("SelectQuery: " + plan.getName() + ", " + result);
         }
      }
   }

   @SuppressWarnings("unchecked")
   protected <T extends AbstractConfigurator> T getConfigurator() {
      return (T) m_configurator;
   }

   @Override
   protected String getCustomConfigurationName() {
      try {
         File componentDescriptor = new File("target/" + getClass().getSimpleName() + ".xml");
         String configuration = componentDescriptor.getAbsolutePath();
         List<Component> all = getRuntimeComponents();

         componentDescriptor.getParentFile().mkdirs();
         
         FileUtils.fileWrite(configuration, Configurators.forPlexus().generateXmlConfiguration(all));
         return configuration;
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   protected String getPlexusConfiguration() {
      List<Component> components = getRuntimeComponents();

      return Configurators.forPlexus().generateXmlConfiguration(components);
   }

   protected List<Component> getRuntimeComponents() {
      return WdbcFlowConfigurator.with(m_configurator).getRuntimeComponents();
   }
}
