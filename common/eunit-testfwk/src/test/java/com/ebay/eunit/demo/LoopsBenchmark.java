package com.ebay.eunit.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;

@RunWith(BenchmarkClassRunner.class)
public class LoopsBenchmark {
   private static List<Object> m_list = new ArrayList<Object>();

   @BeforeClass
   public static void init() {
      for (int i = 0; i < 10; i++) {
         m_list.add(i);
      }
   }

   @CpuMeta(loops = 20000000, warmups = 10)
   @Test
   public void indexLoop() {
      List<Object> list = m_list;
      int len = list.size();

      for (int i = 0; i < len; i++) {
         Object obj = list.get(i);

         assert (obj != null);
      }
   }

   @CpuMeta(loops = 20000000, warmups = 10)
   @Test
   public void forEachLoop() {
      List<Object> list = m_list;

      for (Object obj : list) {
         assert (obj != null);
      }
   }
}
