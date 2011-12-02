package com.site.lab.performance;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

public class BenchmarkExecutionTest {
   @Test
   public void testExecute() {
      BenchmarkExecution execution = new BenchmarkExecution(Mock.class, null);

      execution.execute();
      execution.generateReport(new File("target/mock.xml"));
   }

   @RunWith(PerformanceRunner.class)
   public static final class Mock {
      @CpuMeta
      public void newMethodCall() {
         privateMethod(0);
      }

      @CpuMeta
      @MemoryMeta(gcinfo = true)
      public void newMethodCalls() {
         for (int i = 0; i < 1000; i++) {
            privateMethod(0);
         }
      }

      int privateMethod(final int i) {
         // do nothing here
         return i;
      }
   }
}
