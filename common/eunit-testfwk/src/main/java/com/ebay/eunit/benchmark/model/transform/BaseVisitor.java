package com.ebay.eunit.benchmark.model.transform;

import com.ebay.eunit.benchmark.model.IVisitor;
import com.ebay.eunit.benchmark.model.entity.BenchmarkEntity;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.CpuEntity;
import com.ebay.eunit.benchmark.model.entity.MemoryEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;

public abstract class BaseVisitor implements IVisitor {
   @Override
   public void visitBenchmark(BenchmarkEntity benchmark) {
      for (SuiteEntity suite : benchmark.getSuites()) {
         visitSuite(suite);
      }
   }

   @Override
   public void visitCase(CaseEntity _case) {
      if (_case.getCpu() != null) {
         visitCpu(_case.getCpu());
      }

      if (_case.getMemory() != null) {
         visitMemory(_case.getMemory());
      }
   }

   @Override
   public void visitCpu(CpuEntity cpu) {
   }

   @Override
   public void visitMemory(MemoryEntity memory) {
   }

   @Override
   public void visitSuite(SuiteEntity suite) {
      for (CaseEntity _case : suite.getCases()) {
         visitCase(_case);
      }

      if (suite.getCpu() != null) {
         visitCpu(suite.getCpu());
      }

      if (suite.getMemory() != null) {
         visitMemory(suite.getMemory());
      }
   }
}
