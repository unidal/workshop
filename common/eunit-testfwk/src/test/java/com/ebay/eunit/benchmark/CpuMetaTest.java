package com.ebay.eunit.benchmark;

import java.util.Stack;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.EunitJUnit4Runner;

@RunWith(EunitJUnit4Runner.class)
public class CpuMetaTest {
   private static Stack<String> s_stack = new Stack<String>();

   @Test
   public void cpu1() throws Throwable {
      BenchmarkClassRunner runner = new BenchmarkClassRunner(CpuTest1.class);

      runner.runClass();
   }

   @RunWith(BenchmarkClassRunner.class)
   @CpuMeta(loops = 2, warmups = 0)
   public static class CpuTest1 {
      @AfterClass
      public static void afterClass() {
         Assert.assertEquals("[c1, c2, c2]", s_stack.toString());
      }

      @BeforeClass
      public static void beforeClass() {
         s_stack.clear();
      }

      @CpuMeta(loops = 1, warmups = 0)
      public void c1() {
         s_stack.push("c1");
      }

      @Test
      // inherit from class
      public void c2() {
         s_stack.push("c2");
      }
   }
}
