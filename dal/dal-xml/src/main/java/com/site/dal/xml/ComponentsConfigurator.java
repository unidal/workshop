package com.site.dal.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import com.site.dal.xml.builder.DefaultXmlBuilder;
import com.site.dal.xml.builder.DomXmlWriter;
import com.site.dal.xml.builder.SaxXmlWriter;
import com.site.dal.xml.builder.StreamXmlWriter;
import com.site.dal.xml.builder.XmlBuilder;
import com.site.dal.xml.builder.XmlWriter;
import com.site.dal.xml.builder.dom.DefaultNodeFactory;
import com.site.dal.xml.builder.dom.NodeFactory;
import com.site.dal.xml.formatter.DateFormatter;
import com.site.dal.xml.formatter.Formatter;
import com.site.dal.xml.parser.DefaultElementComposition;
import com.site.dal.xml.parser.DefaultXmlParser;
import com.site.dal.xml.parser.DefaultXmlParserPolicy;
import com.site.dal.xml.parser.ElementComposition;
import com.site.dal.xml.parser.XmlParser;
import com.site.dal.xml.parser.XmlParserPolicy;
import com.site.dal.xml.registry.DefaultXmlRegistry;
import com.site.dal.xml.registry.DefaultXmlRegistryFilter;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.dal.xml.registry.XmlRegistryFilter;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      // XML registry
      all.add(C(XmlRegistryFilter.class, DefaultXmlRegistryFilter.class));
      all.add(C(XmlRegistry.class, DefaultXmlRegistry.class).req(XmlRegistryFilter.class));
      
      // XML parser
      all.add(C(XmlParserPolicy.class, DefaultXmlParserPolicy.class));
      all.add(C(XmlBuilder.class, DefaultXmlBuilder.class).is(PER_LOOKUP)
               .req(XmlRegistry.class, XmlParserPolicy.class));
      all.add(C(XmlParser.class, DefaultXmlParser.class).is(PER_LOOKUP)
               .req(XmlRegistry.class, XmlParserPolicy.class, ElementComposition.class));
      all.add(C(ElementComposition.class, DefaultElementComposition.class)
               .req(XmlRegistry.class, XmlParserPolicy.class));
      all.add(C(XmlAdapter.class, DefaultXmlAdapter.class));

      // formatter
      all.add(C(Formatter.class, Date.class.getName(), DateFormatter.class));

      // XML writer
      all.add(C(XmlWriter.class, SAXResult.class.getName(), SaxXmlWriter.class).is(PER_LOOKUP));
      all.add(C(XmlWriter.class, DOMResult.class.getName(), DomXmlWriter.class).is(PER_LOOKUP).req(NodeFactory.class));
      all.add(C(XmlWriter.class, StreamResult.class.getName(), StreamXmlWriter.class).is(PER_LOOKUP));

      // Node Factory
      all.add(C(NodeFactory.class, DefaultNodeFactory.class));
      
      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
