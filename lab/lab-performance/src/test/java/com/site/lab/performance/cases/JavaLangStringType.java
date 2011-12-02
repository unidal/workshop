package com.site.lab.performance.cases;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/stringType.xml")
public class JavaLangStringType {
   @MemoryMeta
   public String newString() {
      return new String();
   }

   @CpuMeta
   @MemoryMeta
   public boolean isEmpty() {
      String str = " \t\r\n";

      return str.trim().length() == 0;
   }

   @CpuMeta
   @MemoryMeta
   public boolean isEmpty2() {
      String str = " \t\r\n";
      int len = str.length();

      for (int i = 0; i < len; i++) {
         if (str.charAt(i) < ' ') {
            return false;
         }
      }

      return true;
   }
}
