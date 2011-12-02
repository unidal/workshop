package com.site.lab.performance;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/footprint.xml")
public class FootprintMeasure {
   @MemoryMeta
   public String footprint1(final ExecutionContext ctx) {
      return getClass().getName() + "footprint1" + ctx.getCurrentIndex();
   }

   @MemoryMeta
   public CacheEntry footprint2(final ExecutionContext ctx) {
      return new CacheEntry(getClass(), "footprint2", ctx.getCurrentIndex());
   }

   @CpuMeta(loops = 100000)
   public void hashcode1(final ExecutionContext ctx) {
      (getClass().getName() + "hashcode1" + ctx.getCurrentIndex()).hashCode();
   }

   @CpuMeta(loops = 100000)
   public void hashcode2(final ExecutionContext ctx) {
      new CacheEntry(getClass(), "hashcode2", ctx.getCurrentIndex()).hashCode();
   }

   private static final class CacheEntry {
      private Class<?> m_clazz;

      private String m_methodName;

      private int m_index;

      private int m_hash;

      public CacheEntry(Class<?> clazz, String methodName, int index) {
         m_clazz = clazz;
         m_methodName = methodName;
         m_index = index;
      }

      @Override
      public boolean equals(Object obj) {
         if (obj instanceof CacheEntry) {
            final CacheEntry e = (CacheEntry) obj;

            return e.m_clazz == m_clazz && e.m_index == m_index && e.m_methodName.equals(m_methodName);
         }

         return false;
      }

      @Override
      public int hashCode() {
         if (m_hash == 0) {
            int hash = m_clazz.hashCode();

            hash = hash * 31 + m_methodName.hashCode();
            hash = hash * 31 + m_index;

            m_hash = hash;
         }

         return m_hash;
      }
   }
}
