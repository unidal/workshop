package org.unidal.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.EbayModule;
import org.unidal.ezsell.configuration.EbayComponentsConfigurator;
import org.unidal.xml.XmlModule;
import org.unidal.xml.configuration.XmlComponentsConfigurator;

import com.site.lookup.configuration.Component;
import com.site.web.configuration.AbstractWebComponentsConfigurator;

class ComponentsConfigurator extends AbstractWebComponentsConfigurator {
   @SuppressWarnings("unchecked")
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(new XmlComponentsConfigurator().defineComponents());
      all.addAll(new EbayComponentsConfigurator().defineComponents());

      defineModuleRegistry(all, EbayModule.class, EbayModule.class, XmlModule.class);

      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
