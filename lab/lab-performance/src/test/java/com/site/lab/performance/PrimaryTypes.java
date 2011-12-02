package com.site.lab.performance;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/primaryTypes.xml")
public class PrimaryTypes {
   @CpuMeta
   @MemoryMeta
   public boolean newBoolean() {
      return true;
   }

   @CpuMeta
   @MemoryMeta
   public byte newByte() {
      return 0;
   }

   @CpuMeta
   @MemoryMeta
   public char newChar() {
      return 0;
   }

   @CpuMeta
   @MemoryMeta
   public double newDouble() {
      return 0;
   }

   @CpuMeta
   @MemoryMeta
   public float newFloat() {
      return 0;
   }

   @CpuMeta
   @MemoryMeta
   public BigInteger newBigInteger() {
      return new BigInteger("0");
   }

   @CpuMeta
   @MemoryMeta
   public BigDecimal newBigDecimal() {
      return new BigDecimal("0");
   }

   @CpuMeta
   @MemoryMeta
   public int newInteger() {
      return 0;
   }

   @CpuMeta
   @MemoryMeta
   public long newLong() {
      return 0;
   }

   @CpuMeta
   @MemoryMeta
   public short newShort() {
      return 0;
   }
}
