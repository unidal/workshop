package com.site.wdbc.jctrans.carsinfo;

import java.io.File;

public class QueryTestConfigurator extends ComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new QueryTestConfigurator());
   }

   @Override
   protected File getConfigurationFile() {
      return new File("src/test/resources/" + QueryTest.class.getName().replace('.', '/') + ".xml");
   }
}
