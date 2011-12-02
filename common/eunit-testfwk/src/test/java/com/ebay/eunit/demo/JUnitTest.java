package com.ebay.eunit.demo;

import junit.framework.Assert;

import org.junit.Test;

public class JUnitTest {
   @Test
   public void testAdd() {
      int actual = 1 + 1;
      int expected = 2;

      Assert.assertEquals(expected, actual);
   }
}
