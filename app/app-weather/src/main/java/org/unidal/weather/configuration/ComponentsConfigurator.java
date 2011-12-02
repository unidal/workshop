package org.unidal.weather.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.weather.biz.WeatherPage;
import org.unidal.weather.web.WebModule;

import com.site.lookup.configuration.Component;
import com.site.web.configuration.AbstractWebComponentsConfigurator;

class ComponentsConfigurator extends AbstractWebComponentsConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(new DatabaseConfigurator().defineComponents());
      all.addAll(defineComponent(WeatherPage.class));

      all.add(defineModuleRegistryComponent(WebModule.class));

      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
