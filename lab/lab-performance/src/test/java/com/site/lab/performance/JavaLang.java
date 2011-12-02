package com.site.lab.performance;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/java.lang.xml")
public class JavaLang {
   @CpuMeta
   @MemoryMeta
   public Object newObject() {
      return new Object();
   }

   @CpuMeta
   @MemoryMeta
   public Exception newException() {
      return new Exception();
   }

   @CpuMeta
   public void throwException() {
      try {
         throw new Exception();
      } catch (Exception e) {
         // ignore it
      }
   }

   @CpuMeta
   @MemoryMeta
   public Error newError() {
      return new Error();
   }

   @CpuMeta
   @MemoryMeta
   public Thread newThread() {
      return new Thread();
   }

   @CpuMeta
   @MemoryMeta
   public ThreadLocal<Object> newThreadLocal() {
      return new ThreadLocal<Object>();
   }

   @CpuMeta
   @MemoryMeta
   public StringBuilder newStringBuilder() {
      return new StringBuilder();
   }

}
