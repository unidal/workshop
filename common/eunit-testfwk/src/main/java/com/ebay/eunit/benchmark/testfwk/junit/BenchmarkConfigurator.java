package com.ebay.eunit.benchmark.testfwk.junit;

import com.ebay.eunit.benchmark.handler.CpuHandler;
import com.ebay.eunit.benchmark.handler.MemoryHandler;
import com.ebay.eunit.benchmark.testfwk.BenchmarkEventListener;
import com.ebay.eunit.benchmark.testfwk.CpuTaskExecutor;
import com.ebay.eunit.benchmark.testfwk.MemoryTaskExecutor;
import com.ebay.eunit.benchmark.testfwk.ReportTaskExecutor;
import com.ebay.eunit.testfwk.junit.EunitJUnitConfigurator;
import com.ebay.eunit.testfwk.spi.Registry;

public class BenchmarkConfigurator extends EunitJUnitConfigurator {
   public static final BenchmarkConfigurator INSTANCE = new BenchmarkConfigurator();

   private BenchmarkConfigurator() {
   }

   public void configure(Registry registry) {
      super.configure(registry);

      // override event listener and test case builder
      registry.registerEventListener(BenchmarkEventListener.INSTANCE);
      registry.registerTestCaseBuilder(new BenchmarkJUnitTestCaseBuilder());

      registry.registerAnnotationHandler(CpuHandler.INSTANCE);
      registry.registerAnnotationHandler(MemoryHandler.INSTANCE);

      registry.registerTaskExecutors(CpuTaskExecutor.values());
      registry.registerTaskExecutors(MemoryTaskExecutor.values());
      registry.registerTaskExecutors(ReportTaskExecutor.values());
   }
}
