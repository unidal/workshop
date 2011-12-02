package com.ebay.eunit.cmd.testfwk;

import org.junit.runner.RunWith;

import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;
import com.ebay.eunit.benchmark.MemoryMeta;
import com.ebay.eunit.cmd.CommandClassRunner;

@RunWith(BenchmarkClassRunner.class)
public class MyTest {
   @CpuMeta(loops = 1000)
   @MemoryMeta(loops = 1000)
   public void test() throws Throwable {
      CommandClassRunner runner = new CommandClassRunner(CommandTest.class);

      runner.runMethod("getEcho");
   }
}
