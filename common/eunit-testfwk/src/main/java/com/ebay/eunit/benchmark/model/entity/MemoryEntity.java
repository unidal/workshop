package com.ebay.eunit.benchmark.model.entity;

import com.ebay.eunit.benchmark.model.BaseEntity;
import com.ebay.eunit.benchmark.model.IVisitor;

public class MemoryEntity extends BaseEntity<MemoryEntity> {
   private Integer m_loops;

   private Integer m_warmups;

   private Long m_footprint;

   private Long m_permanentFootprint;

   private Long m_totalFootprint;

   private Long m_gcCount;

   private Long m_gcAmount;

   private Long m_gcTime;

   public MemoryEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitMemory(this);
   }

   public Long getFootprint() {
      return m_footprint;
   }

   public Long getGcAmount() {
      return m_gcAmount;
   }

   public Long getGcCount() {
      return m_gcCount;
   }

   public Long getGcTime() {
      return m_gcTime;
   }

   public Integer getLoops() {
      return m_loops;
   }

   public Long getPermanentFootprint() {
      return m_permanentFootprint;
   }

   public Long getTotalFootprint() {
      return m_totalFootprint;
   }

   public Integer getWarmups() {
      return m_warmups;
   }

   @Override
   public void mergeAttributes(MemoryEntity other) {
      if (other.getLoops() != null) {
         m_loops = other.getLoops();
      }

      if (other.getWarmups() != null) {
         m_warmups = other.getWarmups();
      }

      if (other.getFootprint() != null) {
         m_footprint = other.getFootprint();
      }

      if (other.getPermanentFootprint() != null) {
         m_permanentFootprint = other.getPermanentFootprint();
      }

      if (other.getTotalFootprint() != null) {
         m_totalFootprint = other.getTotalFootprint();
      }

      if (other.getGcCount() != null) {
         m_gcCount = other.getGcCount();
      }

      if (other.getGcAmount() != null) {
         m_gcAmount = other.getGcAmount();
      }

      if (other.getGcTime() != null) {
         m_gcTime = other.getGcTime();
      }
   }

   public MemoryEntity setFootprint(Long footprint) {
      m_footprint=footprint;
      return this;
   }

   public MemoryEntity setGcAmount(Long gcAmount) {
      m_gcAmount=gcAmount;
      return this;
   }

   public MemoryEntity setGcCount(Long gcCount) {
      m_gcCount=gcCount;
      return this;
   }

   public MemoryEntity setGcTime(Long gcTime) {
      m_gcTime=gcTime;
      return this;
   }

   public MemoryEntity setLoops(Integer loops) {
      m_loops=loops;
      return this;
   }

   public MemoryEntity setPermanentFootprint(Long permanentFootprint) {
      m_permanentFootprint=permanentFootprint;
      return this;
   }

   public MemoryEntity setTotalFootprint(Long totalFootprint) {
      m_totalFootprint=totalFootprint;
      return this;
   }

   public MemoryEntity setWarmups(Integer warmups) {
      m_warmups=warmups;
      return this;
   }

}
