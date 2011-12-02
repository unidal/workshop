package com.ebay.eunit.benchmark.testfwk;

import org.junit.runner.RunWith;

import com.ebay.eunit.annotation.Groups;
import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;
import com.ebay.eunit.benchmark.MemoryMeta;

@RunWith(BenchmarkClassRunner.class)
@Groups("benchmark")
public class Strings {
   private String m_id = "Kitty";

   @CpuMeta
   @MemoryMeta
   public String stringFormat() {
      return String.format("Hello, %s!", m_id);
   }

   @CpuMeta(loops = 10000000)
   @MemoryMeta
   public String concatenation() {
      return "Hello, " + m_id + "!";
   }

   @CpuMeta(loops = 10000000)
   @MemoryMeta
   public String constant() {
      return "Hello, Kitty!";
   }
}
