package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class EbayComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(new WebConfigurator().defineComponents());
      all.addAll(new CallbackConfigurator().defineComponents());
      all.addAll(new LogicConfigurator().defineComponents());
      all.addAll(new ApiConfigurator().defineComponents());
      all.addAll(new WdbcConfigurator().defineComponents());
      all.addAll(new DatabaseConfigurator().defineComponents());

      return all;
   }
}
