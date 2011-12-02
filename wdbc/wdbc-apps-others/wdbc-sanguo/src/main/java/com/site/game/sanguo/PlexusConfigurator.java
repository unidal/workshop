package com.site.game.sanguo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.logging.LoggerManager;

import com.site.lookup.configuration.Component;
import com.site.lookup.logger.TimedConsoleLoggerManager;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;

public class PlexusConfigurator extends AbstractWdbcComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new PlexusConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(LoggerManager.class, TimedConsoleLoggerManager.class).is(PER_LOOKUP)
            .config(E("dateFormat").value("MM-dd HH:mm:ss")));

      return all;
   }

   @Override
   protected File getConfigurationFile() {
      return new File("src/main/resources/META-INF/plexus/plexus.xml");
   }
}
