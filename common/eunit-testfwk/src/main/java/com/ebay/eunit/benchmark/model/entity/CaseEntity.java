package com.ebay.eunit.benchmark.model.entity;

import static com.ebay.eunit.benchmark.model.Constants.ATTR_NAME;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_CASE;

import com.ebay.eunit.benchmark.model.BaseEntity;
import com.ebay.eunit.benchmark.model.IVisitor;

public class CaseEntity extends BaseEntity<CaseEntity> {
   private String m_name;

   private java.lang.reflect.Method m_method;

   private String m_startAt;

   private String m_endAt;

   private CpuEntity m_cpu;

   private MemoryEntity m_memory;

   public CaseEntity(String name) {
      m_name = name;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCase(this);
   }

   public CpuEntity getCpu() {
      return m_cpu;
   }

   public String getEndAt() {
      return m_endAt;
   }

   public MemoryEntity getMemory() {
      return m_memory;
   }

   public java.lang.reflect.Method getMethod() {
      return m_method;
   }

   public String getName() {
      return m_name;
   }

   public String getStartAt() {
      return m_startAt;
   }

   @Override
   public void mergeAttributes(CaseEntity other) {
      assertAttributeEquals(other, ENTITY_CASE, ATTR_NAME, m_name, other.getName());

      if (other.getMethod() != null) {
         m_method = other.getMethod();
      }
   }

   public CaseEntity setCpu(CpuEntity cpu) {
      m_cpu=cpu;
      return this;
   }

   public CaseEntity setEndAt(String endAt) {
      m_endAt=endAt;
      return this;
   }

   public CaseEntity setMemory(MemoryEntity memory) {
      m_memory=memory;
      return this;
   }

   public CaseEntity setMethod(java.lang.reflect.Method method) {
      m_method=method;
      return this;
   }

   public CaseEntity setStartAt(String startAt) {
      m_startAt=startAt;
      return this;
   }

}
