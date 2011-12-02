package com.site.app.tracking.counter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.site.app.tracking.counter.Configuration;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

class PayloadProviderTestConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(Configuration.class).config(E("contextPath").value("/t"), E("servletPath").value("/c")));

      return all;
   }

   @Override
   protected File getConfigurationFile() {
      return new File("src/test/resources/" + PayloadProviderTest.class.getName().replace('.', '/') + ".xml");
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new PayloadProviderTestConfigurator());
   }
}
