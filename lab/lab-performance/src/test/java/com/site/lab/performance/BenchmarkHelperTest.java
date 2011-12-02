package com.site.lab.performance;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.Test;

import com.site.dal.xml.XmlException;
import com.site.lab.performance.model.Benchmark;

public class BenchmarkHelperTest {
   @Test
   public void testMarshal() throws ComponentLookupException, XmlException {
      Benchmark benchmark = new Benchmark();
      StringWriter writer = new StringWriter(1024);
      Class<?> clazz = getClass();

      BenchmarkHelper.newSuite(benchmark, clazz);
      BenchmarkHelper.newSuite(benchmark, clazz);
      BenchmarkHelper.newSuite(benchmark, clazz);
      BenchmarkHelper.marshal(benchmark, writer);

      Assert.assertEquals(
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n" + 
            "<benchmark>\r\n" + 
            "   <suite type=\"" + clazz.getName() + "\" />\r\n" + 
            "   <suite type=\"" + clazz.getName() + "\" />\r\n" + 
            "   <suite type=\"" + clazz.getName() + "\" />\r\n" + 
            "</benchmark>", 
            writer.toString());
   }

   @Test
   public void testUnmarshal() throws ComponentLookupException, XmlException, IOException {
      Class<?> clazz = getClass();
      StringReader reader = new StringReader(
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n" + 
            "<benchmark>\r\n" + 
            "   <suite type=\"" + clazz.getName() + "\" />\r\n" + 
            "   <suite type=\"" + clazz.getName() + "\" />\r\n" + 
            "   <suite type=\"" + clazz.getName() + "\" />\r\n" + 
            "</benchmark>");
      Benchmark benchmark = BenchmarkHelper.unmarshal(reader);

      Assert.assertEquals(3, benchmark.getSuiteList().size());
   }
}
