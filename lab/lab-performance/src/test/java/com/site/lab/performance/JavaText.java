package com.site.lab.performance;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/java.text.xml")
public class JavaText {
   @CpuMeta(loops = 10000)
   @MemoryMeta(loops = 10000)
   public MessageFormat newMessageFormat0() {
      return new MessageFormat("{0,date,yyyy-MM-dd hh:mm:ss}");
   }

   @CpuMeta(loops = 10000)
   @MemoryMeta(loops = 10000)
   public MessageFormat newMessageFormat1() {
      return new MessageFormat("{0} {1} {2}");
   }

   @CpuMeta(loops = 10000)
   @MemoryMeta(loops = 10000)
   public MessageFormat newMessageFormat2() {
      return new MessageFormat("{0} {1} {3}", Locale.US);
   }

   @CpuMeta(loops = 10000)
   @MemoryMeta(loops = 10000)
   public MessageFormat newMessageFormat3() {
      return new MessageFormat("{0,date,yyyy-MM-dd}");
   }

   @CpuMeta(loops = 10000)
   @MemoryMeta(loops = 10000)
   public SimpleDateFormat newSimpleDateFormat() {
      return new SimpleDateFormat("yyyy-MM-dd");
   }

   @CpuMeta(loops = 10000)
   @MemoryMeta(loops = 10000)
   public DecimalFormat newDecimalFormat() {
      return new DecimalFormat("###,##0.00");
   }
}
