package com.site.lab.performance;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/arrayTypes.xml")
public class ArrayTypes {
   @CpuMeta
   @MemoryMeta
   public boolean[] booleanArray0() {
      return new boolean[0];
   }

   @CpuMeta
   @MemoryMeta
   public boolean[] booleanArray1() {
      return new boolean[1];
   }

   @CpuMeta
   @MemoryMeta
   public boolean[] booleanArray4() {
      return new boolean[4];
   }

   @CpuMeta
   @MemoryMeta
   public boolean[] booleanArray5() {
      return new boolean[5];
   }

   @CpuMeta
   @MemoryMeta
   public boolean[] booleanArray9() {
      return new boolean[9];
   }

   @CpuMeta
   @MemoryMeta
   public int[] integerArray0() {
      return new int[0];
   }

   @CpuMeta
   @MemoryMeta
   public int[] integerArray1() {
      return new int[1];
   }

   @CpuMeta
   @MemoryMeta
   public int[] integerArray4() {
      return new int[4];
   }

   @CpuMeta
   @MemoryMeta
   public int[] integerArray5() {
      return new int[5];
   }

   @CpuMeta
   @MemoryMeta
   public int[] integerArray9() {
      return new int[9];
   }

   @CpuMeta
   @MemoryMeta
   public int[][] integerArrays01() {
      return new int[0][1];
   }

   @CpuMeta
   @MemoryMeta
   public int[][] integerArrays11() {
      return new int[1][1];
   }

   @CpuMeta
   @MemoryMeta
   public int[][] integerArrays15() {
      return new int[1][5];
   }

   @CpuMeta
   @MemoryMeta
   public int[][] integerArrays19() {
      return new int[1][9];
   }

   @CpuMeta
   @MemoryMeta
   public int[][] integerArrays51() {
      return new int[5][1];
   }

   @CpuMeta
   @MemoryMeta
   public int[][] integerArrays91() {
      return new int[9][1];
   }

}
