package com.ebay.eunit.benchmark;

import org.junit.runners.model.InitializationError;

import com.ebay.eunit.EunitJUnit4Runner;
import com.ebay.eunit.benchmark.testfwk.junit.BenchmarkConfigurator;
import com.ebay.eunit.testfwk.spi.IConfigurator;

public class BenchmarkClassRunner extends EunitJUnit4Runner {
   public BenchmarkClassRunner(Class<?> clazz) throws InitializationError {
      super(clazz);
   }

   public BenchmarkClassRunner(Class<?> clazz, String methodName) throws InitializationError {
      super(clazz, methodName);
   }

   @Override
   protected IConfigurator getConfigurator() {
      return BenchmarkConfigurator.INSTANCE;
   }
}
