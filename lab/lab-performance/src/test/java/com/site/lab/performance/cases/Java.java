package com.site.lab.performance.cases;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/java.xml")
public final class Java {
   @CpuMeta(loops = 10000000)
   @Ignore
   public void newMethodCall() {
      privateMethod(0);
   }

   @CpuMeta
   @Ignore
   public void newMethodCalls() {
      for (int i = 0; i < 1000; i++) {
         privateMethod(0);
      }
   }

   private volatile int m_counter;

   @CpuMeta
   public void volatileCall() {
      for (int i = 0; i < 1000; i++) {
         m_counter += 1;
      }
   }

   int privateMethod(final int i) {
      // do nothing here
      return i;
   }
}
