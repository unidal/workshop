package com.site.lab.performance.cases;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.configurator.ComponentConfigurator;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;
import com.site.lookup.ContainerLoader;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/lookup.xml")
public final class Lookup {
   @MemoryMeta
   @CpuMeta
   public void loadContainer() {
      ContainerLoader.getDefaultContainer();
   }

   @MemoryMeta
   @CpuMeta
   public Object doLookup() throws ComponentLookupException {
      PlexusContainer container = ContainerLoader.getDefaultContainer();
      Object instance = container.lookup(ComponentConfigurator.class);

      assert (instance != null);

      return instance;
   }

   @MemoryMeta
   @CpuMeta
   public void doLookupFail() {
      PlexusContainer container = ContainerLoader.getDefaultContainer();

      try {
         container.lookup(ComponentConfigurator.class, "unknown");
      } catch (ComponentLookupException e) {
         // ignore it
      }
   }

}
