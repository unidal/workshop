package com.ebay.eunit.benchmark.model;

import com.ebay.eunit.benchmark.model.entity.BenchmarkEntity;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.CpuEntity;
import com.ebay.eunit.benchmark.model.entity.MemoryEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;

public interface IVisitor {

   public void visitBenchmark(BenchmarkEntity benchmark);

   public void visitCase(CaseEntity _case);

   public void visitCpu(CpuEntity cpu);

   public void visitMemory(MemoryEntity memory);

   public void visitSuite(SuiteEntity suite);
}
