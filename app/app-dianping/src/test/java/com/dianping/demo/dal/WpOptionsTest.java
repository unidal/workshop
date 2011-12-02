package com.dianping.demo.dal;

import org.junit.Test;

import com.site.lookup.ComponentTestCase;

public class WpOptionsTest extends ComponentTestCase {
   @Test
   public void test() throws Exception {
      WpOptionsDao dao = lookup(WpOptionsDao.class);
      
      System.out.println(dao);
   }
}
