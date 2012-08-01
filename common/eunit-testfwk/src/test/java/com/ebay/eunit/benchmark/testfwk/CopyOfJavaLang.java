package com.ebay.eunit.benchmark.testfwk;

import org.junit.runner.RunWith;

import com.ebay.eunit.annotation.Groups;
import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;
import com.ebay.eunit.benchmark.MemoryMeta;

@RunWith(BenchmarkClassRunner.class)
@Groups("benchmark")
public class CopyOfJavaLang {
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public Object newObject0() {
      return new Object();
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public Object newObject() {
      return new Object();
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public Boolean newBoolean() {
   	return new Boolean(true);
   }

   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public String newString() {
      return new String();
   }
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public String newString2() {
      return new String("");
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public byte[] newByteArray() {
      return new byte[9];
   }

   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray9() {
      return new char[9];
   }
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray0() {
      return new char[0];
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray1() {
      return new char[1];
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray3() {
      return new char[3];
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray4() {
      return new char[4];
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray5() {
      return new char[5];
   }
   
   @CpuMeta(loops = 10000000)
   @MemoryMeta(loops = 1000000)
   public char[] newCharArray8() {
      return new char[8];
   }
}
