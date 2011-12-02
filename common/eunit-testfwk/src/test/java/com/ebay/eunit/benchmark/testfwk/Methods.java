package com.ebay.eunit.benchmark.testfwk;

import java.lang.reflect.Method;

import org.junit.runner.RunWith;

import com.ebay.eunit.annotation.Groups;
import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;

@RunWith(BenchmarkClassRunner.class)
@Groups("benchmark")
public class Methods {
   @CpuMeta(loops = 100000)
   public void callByReflection() throws Exception {
      final Method m = getClass().getDeclaredMethod("m");
      final Object[] args = new Object[0];

      m.setAccessible(true);

      for (int i = 0; i < 1000; i++) {
         m.invoke(this, args);
      }
   }

   @CpuMeta(loops = 20000000)
   public void callDirectly() {
      for (int i = 0; i < 1000; i++) {
         m();
      }
   }

   protected void m() {
   }
}
