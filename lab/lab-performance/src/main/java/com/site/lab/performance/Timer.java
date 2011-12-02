package com.site.lab.performance;

public class Timer {
   private static final long startup = System.currentTimeMillis();

   public static void initialize() {
      // nothing here
   }

   public static long getCurrentTime() {
      return System.currentTimeMillis() - startup;
   }
}
