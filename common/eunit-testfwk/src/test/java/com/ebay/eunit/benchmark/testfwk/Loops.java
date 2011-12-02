package com.ebay.eunit.benchmark.testfwk;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.annotation.Groups;
import com.ebay.eunit.benchmark.BenchmarkClassRunner;
import com.ebay.eunit.benchmark.CpuMeta;

@RunWith(BenchmarkClassRunner.class)
@Groups("benchmark")
public class Loops {
   private static List<Object> m_list = new ArrayList<Object>();

   @BeforeClass
   public static void init() {
      for (int i = 0; i < 100; i++) {
         m_list.add(i);
      }
   }

   @CpuMeta(loops = 10000000)
   @Test
   public void indexLoop() {
      List<Object> list = m_list;
      int len = list.size();

      for (int i = 0; i < len; i++) {
         Object obj = list.get(i);

         assert (obj != null);
      }
   }

   @CpuMeta(loops = 1000000)
   @Test
   public void forEachLoop() {
      List<Object> list = m_list;

      for (Object obj : list) {
         assert (obj != null);
      }
   }
}
