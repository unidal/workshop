package com.site.wdbc.jctrans;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;

import com.site.wdbc.http.Flow;

public class Crawler {
   public static void main(String[] args) {
      Crawler crawler = new Crawler();
      List<String> options = Arrays.asList(args);

      if (options.isEmpty() || options.contains("-wswl")) {
         try {
            System.out.println("Fetching wswl:");
            crawler.execute("/META-INF/plexus/components-wswl.xml");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      if (options.isEmpty() || options.contains("-cargoinfo")) {
         try {
            System.out.println("Fetching cargoinfo:");
            crawler.execute("/META-INF/plexus/components-cargoinfo.xml");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      if (options.isEmpty() || options.contains("-carsinfo")) {
         try {
            System.out.println("Fetching carsinfo:");
            crawler.execute("/META-INF/plexus/components-carsinfo.xml");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public void execute(String configuration) throws Exception {
      Flow flow = (Flow) getContainer(configuration).lookup(Flow.class);

      flow.execute();
   }

   protected PlexusContainer getContainer(String resource) throws PlexusContainerException {
      ContainerConfiguration config = new DefaultContainerConfiguration();

      config.setName("crawler").setContext(getContext());
      config.setContainerConfiguration(resource);
      return new DefaultPlexusContainer(config);
   }

   protected Map<String, String> getContext() {
      Map<String, String> context = new HashMap<String, String>();
      String basedir = System.getProperty("basedir");

      if (basedir == null) {
         basedir = new File(".").getAbsolutePath();
      }

      context.put("basedir", basedir);
      context.put("plexus.home", basedir);

      return context;
   }
}
