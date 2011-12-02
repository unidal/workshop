package com.site.lab.performance.cases;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.runner.RunWith;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/javax.xml.xml")
public class JavaxXml {
   @CpuMeta(loops = 20000)
   @MemoryMeta(loops = 5000)
   public SAXParser newSAXParser0() throws ParserConfigurationException, SAXException {
      SAXParserFactory factory = SAXParserFactory.newInstance();

      factory.setNamespaceAware(true);
      return factory.newSAXParser();
   }

   @CpuMeta(loops = 20000)
   public SAXParser newSAXParser1() throws ParserConfigurationException, SAXException {
      System.setProperty("javax.xml.parsers.SAXParserFactory",
            "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
      SAXParserFactory factory = SAXParserFactory.newInstance();

      factory.setNamespaceAware(true);
      return factory.newSAXParser();
   }

   @CpuMeta(loops = 20000)
   public void parse() throws ParserConfigurationException, SAXException, IOException {
      System.setProperty("javax.xml.parsers.SAXParserFactory",
            "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
      SAXParserFactory factory = SAXParserFactory.newInstance();

      factory.setNamespaceAware(true);
      factory.newSAXParser().parse(
            new InputSource(new StringReader(
                  "<html><head><title>this is title</title></head><body>this is body</body></html>")),
            new DefaultHandler());
   }
}
