package com.site.lab.performance.cases;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/java.lang.management.xml")
public class JavaLangManagement {
   @CpuMeta(loops = 1, warmups = 0)
   @MemoryMeta(loops = 0, warmups = 0)
   public void getOperatingSystemMXBean() {
      OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();

      System.out.println(os);
      System.out.println(os.getAvailableProcessors());
      System.out.println(os.getArch());
      System.out.println(os.getName());
      System.out.println(os.getVersion());
   }

   @CpuMeta(loops = 1, warmups = 0)
   @MemoryMeta(loops = 0, warmups = 0)
   public void getThreadMXBean() {
      ThreadMXBean thread = ManagementFactory.getThreadMXBean();

      System.out.println(thread);
      System.out.println(thread.isThreadContentionMonitoringSupported());
      System.out.println(thread.isThreadContentionMonitoringEnabled());
      System.out.println(thread.isCurrentThreadCpuTimeSupported());
      System.out.println(thread.isThreadCpuTimeSupported());
      System.out.println(thread.isThreadCpuTimeEnabled());
   }

   @CpuMeta(loops = 1, warmups = 0)
   @MemoryMeta(loops = 0, warmups = 0)
   public void getMemoryPoolMXBeans() {
      List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();

      System.out.println(pools);
      for (MemoryPoolMXBean pool : pools) {
         System.out.println(pool);
         System.out.println(pool.isValid());
         System.out.println(pool.isCollectionUsageThresholdSupported());
         System.out.println(pool.isUsageThresholdSupported());

         if (pool.isCollectionUsageThresholdSupported()) {
            System.out.println(pool.isCollectionUsageThresholdExceeded());
         }

         if (pool.isUsageThresholdSupported()) {
            System.out.println(pool.isUsageThresholdExceeded());
         }
      }
   }
}
