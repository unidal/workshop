package com.ebay.eunit.benchmark.testfwk.junit;

import com.ebay.eunit.benchmark.model.entity.BenchmarkEntity;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.CpuEntity;
import com.ebay.eunit.benchmark.model.entity.MemoryEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;
import com.ebay.eunit.benchmark.testfwk.CpuTaskType;
import com.ebay.eunit.benchmark.testfwk.MemoryTaskType;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.junit.EunitJUnitTestCaseBuilder;
import com.ebay.eunit.testfwk.junit.JUnitCallback;
import com.ebay.eunit.testfwk.junit.JUnitTestCase;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IModelContext;
import com.ebay.eunit.testfwk.spi.ITestCase;

public class BenchmarkJUnitTestCaseBuilder extends EunitJUnitTestCaseBuilder {
   @Override
   public ITestCase<JUnitCallback> build(IClassContext classContext, EunitMethod eunitMethod) {
      JUnitTestCase testCase = new JUnitTestCase(eunitMethod);
      IModelContext<BenchmarkEntity> ctx = classContext.forModel();
      BenchmarkEntity benchmark = ctx.getModel();
      SuiteEntity suite = benchmark.findSuite(classContext.getTestClass());
      CaseEntity c = suite.findCase(eunitMethod.getName());

      if (c != null) {
         MemoryEntity memory = c.getMemory();
         CpuEntity cpu = c.getCpu();

         if (memory != null) {
            testCase.addTask(MemoryTaskType.START, eunitMethod);
            testCase.addTask(MemoryTaskType.WARMUP, eunitMethod, "loops", memory.getWarmups());
            testCase.addTask(MemoryTaskType.EXECUTE, eunitMethod, "loops", memory.getLoops());
            testCase.addTask(MemoryTaskType.END, eunitMethod);
         }

         if (cpu != null) {
            testCase.addTask(CpuTaskType.START, eunitMethod);
            testCase.addTask(CpuTaskType.WARMUP, eunitMethod, "loops", cpu.getWarmups());
            testCase.addTask(CpuTaskType.EXECUTE, eunitMethod, "loops", cpu.getLoops());
            testCase.addTask(CpuTaskType.END, eunitMethod);
         }
      } else {
         return super.build(classContext, eunitMethod);
      }

      return testCase;
   }
}
