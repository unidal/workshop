package com.site.kernel.logging;

final class NullLogger extends Log {
   private static final NullLogger s_instance = new NullLogger();

   private NullLogger() {
   }

   public static Log getLogger(String name) {
      return s_instance;
   }

   @Override
   public boolean isLogEnabled(int logLevel) {
      // Log nothing
      return false;
   }

   @Override
   public void log(int logLevel, Object message, Throwable t) {
      // Do nothing
   }
}
