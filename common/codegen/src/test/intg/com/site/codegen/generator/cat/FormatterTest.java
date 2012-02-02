package com.site.codegen.generator.cat;

import java.util.Formattable;
import java.util.Formatter;

import junit.framework.Assert;

import org.junit.Test;

public class FormatterTest {
   @Test
   public void test() {
      Mock m = new Mock();

      Assert.assertEquals("0:-1:-1", String.format("%s", m));
      Assert.assertEquals("4:-1:-1", String.format("%#s", m));
      Assert.assertEquals("1:1:-1", String.format("%-1s", m));
      Assert.assertEquals("1:3:-1", String.format("%-3s", m));
      Assert.assertEquals("1:3:0", String.format("%-3.0s", m));
      Assert.assertEquals("1:3:2", String.format("%-3.2s", m));
      Assert.assertEquals("0:3:2", String.format("%3.2s", m));
      Assert.assertEquals("0:-1:0", String.format("%.0s", m));
      Assert.assertEquals("0:-1:2", String.format("%.2s", m));
   }

   static class Mock implements Formattable {
      @Override
      public void formatTo(Formatter formatter, int flags, int width, int precision) {
         StringBuilder sb = new StringBuilder();

         sb.append(flags).append(':').append(width).append(':').append(precision);
         formatter.format(sb.toString());
      }
   }
}
