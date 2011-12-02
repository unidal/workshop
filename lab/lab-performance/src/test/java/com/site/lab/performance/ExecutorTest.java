package com.site.lab.performance;

import java.io.File;

import javax.xml.transform.stream.StreamResult;

import org.junit.Test;

import com.site.dal.xml.XmlAdapter;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.lab.performance.cases.FootprintMeasure;
import com.site.lab.performance.cases.JavaLangStringType;
import com.site.lab.performance.cases.JavaPrimaryType;
import com.site.lab.performance.cases.TimeMeasure;
import com.site.lab.performance.model.Benchmark;
import com.site.lookup.ComponentTestCase;

public class ExecutorTest extends ComponentTestCase {
   private static final Benchmark s_benchmark = new Benchmark();

   @Test
   public void testPrimaryTypes() throws Exception {
      new Executor(JavaPrimaryType.class, s_benchmark).execute();
   }

   @Test
   public void testTimeMeasure() throws Exception {
      new Executor(TimeMeasure.class, s_benchmark).execute();
   }

   @Test
   public void testStringType() throws Exception {
      new Executor(JavaLangStringType.class, s_benchmark).execute();
   }

   @Test
   public void testFootprintMeasure() throws Exception {
      new Executor(FootprintMeasure.class, s_benchmark).execute();
   }

   @Test
   public void testGenerateResult() throws Exception {
      lookup(XmlRegistry.class).register(Benchmark.class);
      XmlAdapter adapter = lookup(XmlAdapter.class);
      File file = new File("target/benchmark_report.xml");
      StreamResult result = new StreamResult(file);

      adapter.marshal(result, s_benchmark);
   }
}
