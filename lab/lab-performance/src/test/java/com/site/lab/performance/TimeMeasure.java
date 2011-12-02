package com.site.lab.performance;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/time.xml")
public class TimeMeasure {
   private boolean m_firstTime = true;

   @CpuMeta(loops = 10)
   public void time1() throws InterruptedException {
      Thread.sleep(100);
   }

   @CpuMeta(loops = 10)
   public void time2() throws InterruptedException {
      if (m_firstTime) {
         m_firstTime = false;
         Thread.sleep(200);
      } else {
         Thread.sleep(100);
      }
   }
}
