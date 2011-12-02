package com.ebay.eunit.benchmark.model.entity;

import com.ebay.eunit.benchmark.model.BaseEntity;
import com.ebay.eunit.benchmark.model.IVisitor;

public class CpuEntity extends BaseEntity<CpuEntity> {
   private Integer m_loops;

   private Integer m_warmups;

   private Long m_cpuTime;

   private Long m_cpuFirstTime;

   private Long m_cpuTotalTime;

   private Long m_elapsedTime;

   private Long m_elapsedFirstTime;

   private Long m_elapsedTotalTime;

   public CpuEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCpu(this);
   }

   public Long getCpuFirstTime() {
      return m_cpuFirstTime;
   }

   public Long getCpuTime() {
      return m_cpuTime;
   }

   public Long getCpuTotalTime() {
      return m_cpuTotalTime;
   }

   public Long getElapsedFirstTime() {
      return m_elapsedFirstTime;
   }

   public Long getElapsedTime() {
      return m_elapsedTime;
   }

   public Long getElapsedTotalTime() {
      return m_elapsedTotalTime;
   }

   public Integer getLoops() {
      return m_loops;
   }

   public Integer getWarmups() {
      return m_warmups;
   }

   @Override
   public void mergeAttributes(CpuEntity other) {
      if (other.getLoops() != null) {
         m_loops = other.getLoops();
      }

      if (other.getWarmups() != null) {
         m_warmups = other.getWarmups();
      }

      if (other.getCpuTime() != null) {
         m_cpuTime = other.getCpuTime();
      }

      if (other.getCpuFirstTime() != null) {
         m_cpuFirstTime = other.getCpuFirstTime();
      }

      if (other.getCpuTotalTime() != null) {
         m_cpuTotalTime = other.getCpuTotalTime();
      }

      if (other.getElapsedTime() != null) {
         m_elapsedTime = other.getElapsedTime();
      }

      if (other.getElapsedFirstTime() != null) {
         m_elapsedFirstTime = other.getElapsedFirstTime();
      }

      if (other.getElapsedTotalTime() != null) {
         m_elapsedTotalTime = other.getElapsedTotalTime();
      }
   }

   public CpuEntity setCpuFirstTime(Long cpuFirstTime) {
      m_cpuFirstTime=cpuFirstTime;
      return this;
   }

   public CpuEntity setCpuTime(Long cpuTime) {
      m_cpuTime=cpuTime;
      return this;
   }

   public CpuEntity setCpuTotalTime(Long cpuTotalTime) {
      m_cpuTotalTime=cpuTotalTime;
      return this;
   }

   public CpuEntity setElapsedFirstTime(Long elapsedFirstTime) {
      m_elapsedFirstTime=elapsedFirstTime;
      return this;
   }

   public CpuEntity setElapsedTime(Long elapsedTime) {
      m_elapsedTime=elapsedTime;
      return this;
   }

   public CpuEntity setElapsedTotalTime(Long elapsedTotalTime) {
      m_elapsedTotalTime=elapsedTotalTime;
      return this;
   }

   public CpuEntity setLoops(Integer loops) {
      m_loops=loops;
      return this;
   }

   public CpuEntity setWarmups(Integer warmups) {
      m_warmups=warmups;
      return this;
   }

}
