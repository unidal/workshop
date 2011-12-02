package com.site.kernel.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

final class ConsoleLogger extends Log {
   private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");

   public static Log getLogger(String name) {
      return new ConsoleLogger();
   }

   @Override
   public boolean isLogEnabled(int logLevel) {
      // Log everything
      return true;
   }

   @Override
   public void log(int logLevel, Object message, Throwable t) {
      StringBuffer sb = new StringBuffer(1024);

      synchronized (sb) {
         sb.append('[').append(DATE_FORMAT.format(new Date())).append(']');
         sb.append(' ').append(getLevel(logLevel)).append(" - ");
         sb.append(String.valueOf(message));
      }

      System.out.println(sb.toString());

      if (t != null) {
         t.printStackTrace(System.out);
      }
   }

   private String getLevel(int logLevel) {
      switch (logLevel) {
      case DEBUG:
         return "DEBUG";
      case INFO:
         return "INFO";
      case WARN:
         return "WARN";
      case ERROR:
         return "ERROR";
      case FATAL:
         return "FATAL";
      default:
         return "UNKNOWN";
      }
   }
}
