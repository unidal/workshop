package com.ebay.eunit.benchmark.testfwk;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.EunitJUnit4Runner;
import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;

@RunWith(BenchmarkClassRunner.class)
public class InterOperatibility {
   private EunitJUnit4Runner m_runner;

   private static int s_index;

   @After
   public void after() {
      Assert.assertEquals(100010, s_index);
      s_index = 0;
   }

   @Before
   public void before() throws Throwable {
      m_runner = new EunitJUnit4Runner(Test1.class);
   }

   @CpuMeta(loops = 100000)
   public void test() throws Throwable {
      m_runner.runMethod("test");
   }

   public static class Test1 {
      @Test
      public void test() {
         s_index++;
      }
   }
}
