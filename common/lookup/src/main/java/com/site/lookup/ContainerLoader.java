package com.site.lookup;

import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;

public class ContainerLoader {
   private static volatile PlexusContainer s_container = null;

   public static PlexusContainer getDefaultContainer() {
      return getDefaultContainer(new DefaultContainerConfiguration());
   }

   public static PlexusContainer getDefaultContainer(ContainerConfiguration configuration) {
      if (s_container == null) {
         synchronized (ContainerLoader.class) {
            if (s_container == null) {
               try {
                  s_container = new DefaultPlexusContainer(configuration);
               } catch (PlexusContainerException e) {
                  throw new RuntimeException("Unable to create PlexusContainer", e);
               }
            }
         }
      }

      return s_container;
   }
}
