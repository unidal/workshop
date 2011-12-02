package com.site.lab.performance;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.xml.sax.InputSource;

import com.site.dal.xml.XmlAdapter;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.lab.performance.model.Benchmark;
import com.site.lab.performance.model.Case;
import com.site.lab.performance.model.Suite;
import com.site.lookup.ContainerLoader;

public class BenchmarkHelper {
   public static final Suite newSuite(final Benchmark benchmark, final Class<?> clazz) {
      final Suite suite = new Suite();

      suite.setType(clazz.getName());
      benchmark.addSuite(suite);

      return suite;
   }

   public static Case newCase(final Suite suite, final String name) {
      final Map<String, Case> cases = suite.getCaseMap();
      Case kase = cases.get(name);

      if (kase == null) {
         kase = new Case();
         kase.setName(name);

         suite.addCase(kase);
      }

      return kase;
   }

   public static void marshal(Benchmark benchmark, Writer writer) throws ComponentLookupException, XmlException {
      PlexusContainer container = ContainerLoader.getDefaultContainer();
      XmlRegistry register = (XmlRegistry) container.lookup(XmlRegistry.class);
      XmlAdapter adapter = (XmlAdapter) container.lookup(XmlAdapter.class);
      StreamResult result = new StreamResult(writer);

      register.register(Benchmark.class);
      adapter.marshal(result, benchmark);
   }

   public static Benchmark unmarshal(Reader reader) throws ComponentLookupException, XmlException, IOException {
      PlexusContainer container = ContainerLoader.getDefaultContainer();
      XmlRegistry register = (XmlRegistry) container.lookup(XmlRegistry.class);
      XmlAdapter adapter = (XmlAdapter) container.lookup(XmlAdapter.class);
      InputSource input = new InputSource(reader);

      register.register(Benchmark.class);
      return adapter.unmarshal(input);
   }
}
