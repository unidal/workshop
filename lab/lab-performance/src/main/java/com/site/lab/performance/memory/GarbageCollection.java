package com.site.lab.performance.memory;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

public class GarbageCollection {
   public long getGcCount() {
      long count = 0;

      for (GarbageCollectorMXBean mxbean : ManagementFactory.getGarbageCollectorMXBeans()) {
         if (mxbean.isValid()) {
            count += mxbean.getCollectionCount();
         }
      }

      return count;
   }

   public long getGcTime() {
      long time = 0;

      for (GarbageCollectorMXBean mxbean : ManagementFactory.getGarbageCollectorMXBeans()) {
         if (mxbean.isValid()) {
            time += mxbean.getCollectionTime();
         }
      }

      return time;
   }

   public long getGcAmount() {
      long amount = 0;

      return amount;
   }

   public void runGC() {
      // It helps to call Runtime.gc()
      // using several method calls:
      for (int r = 0; r < 10; r++) {
         runGC0();
      }
   }

   private void runGC0() {
      long usedMem1 = usedMemory();
      long usedMem2 = Long.MAX_VALUE;

      for (int i = 0; (usedMem1 < usedMem2) && (i < 500); i++) {
         Runtime.getRuntime().runFinalization();
         Runtime.getRuntime().gc();

         // allow other threads to execute
         Thread.yield();

         usedMem2 = usedMem1;
         usedMem1 = usedMemory();
      }
   }

   public long usedMemory() {
      return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   }
}
