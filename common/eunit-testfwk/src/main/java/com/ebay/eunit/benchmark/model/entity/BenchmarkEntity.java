package com.ebay.eunit.benchmark.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.benchmark.model.BaseEntity;
import com.ebay.eunit.benchmark.model.IVisitor;

public class BenchmarkEntity extends BaseEntity<BenchmarkEntity> {
   private List<SuiteEntity> m_suites = new ArrayList<SuiteEntity>();

   public BenchmarkEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitBenchmark(this);
   }

   public BenchmarkEntity addSuite(SuiteEntity suite) {
      m_suites.add(suite);
      return this;
   }

   public SuiteEntity findSuite(Class<?> type) {
      for (SuiteEntity suite : m_suites) {
         if (!suite.getType().equals(type)) {
            continue;
         }

         return suite;
      }

      return null;
   }

   public List<SuiteEntity> getSuites() {
      return m_suites;
   }

   @Override
   public void mergeAttributes(BenchmarkEntity other) {
   }

   public boolean removeSuite(Class<?> type) {
      int len = m_suites.size();

      for (int i = 0; i < len; i++) {
         SuiteEntity suite = m_suites.get(i);

         if (!suite.getType().equals(type)) {
            continue;
         }

         m_suites.remove(i);
         return true;
      }

      return false;
   }

}
