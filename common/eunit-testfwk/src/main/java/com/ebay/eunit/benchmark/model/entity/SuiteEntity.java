package com.ebay.eunit.benchmark.model.entity;

import static com.ebay.eunit.benchmark.model.Constants.ATTR_TYPE;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_SUITE;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.benchmark.model.BaseEntity;
import com.ebay.eunit.benchmark.model.IVisitor;

public class SuiteEntity extends BaseEntity<SuiteEntity> {
   private Class<?> m_type;

   private List<CaseEntity> m_cases = new ArrayList<CaseEntity>();

   private CpuEntity m_cpu;

   private MemoryEntity m_memory;

   public SuiteEntity(Class<?> type) {
      m_type = type;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitSuite(this);
   }

   public SuiteEntity addCase(CaseEntity _case) {
      m_cases.add(_case);
      return this;
   }

   public CaseEntity findCase(String name) {
      for (CaseEntity _case : m_cases) {
         if (!_case.getName().equals(name)) {
            continue;
         }

         return _case;
      }

      return null;
   }

   public List<CaseEntity> getCases() {
      return m_cases;
   }

   public CpuEntity getCpu() {
      return m_cpu;
   }

   public MemoryEntity getMemory() {
      return m_memory;
   }

   public Class<?> getType() {
      return m_type;
   }

   @Override
   public void mergeAttributes(SuiteEntity other) {
      assertAttributeEquals(other, ENTITY_SUITE, ATTR_TYPE, m_type, other.getType());

   }

   public boolean removeCase(String name) {
      int len = m_cases.size();

      for (int i = 0; i < len; i++) {
         CaseEntity _case = m_cases.get(i);

         if (!_case.getName().equals(name)) {
            continue;
         }

         m_cases.remove(i);
         return true;
      }

      return false;
   }

   public SuiteEntity setCpu(CpuEntity cpu) {
      m_cpu=cpu;
      return this;
   }

   public SuiteEntity setMemory(MemoryEntity memory) {
      m_memory=memory;
      return this;
   }

}
