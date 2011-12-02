package com.ebay.eunit.benchmark.model.transform;

import static com.ebay.eunit.benchmark.model.Constants.ATTR_LOOPS;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_NAME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_TYPE;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_WARMUPS;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_BENCHMARK;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_CASE;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_CPU;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_MEMORY;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_SUITE;

import java.util.Stack;

import com.ebay.eunit.benchmark.model.IVisitor;
import com.ebay.eunit.benchmark.model.entity.BenchmarkEntity;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.CpuEntity;
import com.ebay.eunit.benchmark.model.entity.MemoryEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitBenchmark(BenchmarkEntity benchmark) {
      m_path.down(ENTITY_BENCHMARK);

      visitBenchmarkChildren(benchmark);

      m_path.up(ENTITY_BENCHMARK);
   }

   protected void visitBenchmarkChildren(BenchmarkEntity benchmark) {
      for (SuiteEntity suite : benchmark.getSuites()) {
         visitSuite(suite);
      }
   }

   @Override
   public void visitCase(CaseEntity _case) {
      m_path.down(ENTITY_CASE);

      assertRequired(ATTR_NAME, _case.getName());

      visitCaseChildren(_case);

      m_path.up(ENTITY_CASE);
   }

   protected void visitCaseChildren(CaseEntity _case) {
      if (_case.getCpu() != null) {
         visitCpu(_case.getCpu());
      }

      if (_case.getMemory() != null) {
         visitMemory(_case.getMemory());
      }
   }

   @Override
   public void visitCpu(CpuEntity cpu) {
      m_path.down(ENTITY_CPU);

      assertRequired(ATTR_LOOPS, cpu.getLoops());
      assertRequired(ATTR_WARMUPS, cpu.getWarmups());

      m_path.up(ENTITY_CPU);
   }

   @Override
   public void visitMemory(MemoryEntity memory) {
      m_path.down(ENTITY_MEMORY);

      assertRequired(ATTR_LOOPS, memory.getLoops());
      assertRequired(ATTR_WARMUPS, memory.getWarmups());

      m_path.up(ENTITY_MEMORY);
   }

   @Override
   public void visitSuite(SuiteEntity suite) {
      m_path.down(ENTITY_SUITE);

      assertRequired(ATTR_TYPE, suite.getType());

      visitSuiteChildren(suite);

      m_path.up(ENTITY_SUITE);
   }

   protected void visitSuiteChildren(SuiteEntity suite) {
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

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
