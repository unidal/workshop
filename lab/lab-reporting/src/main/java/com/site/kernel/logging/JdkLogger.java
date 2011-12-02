package com.site.kernel.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

final class JdkLogger extends Log {

   private Logger m_logger;

   private JdkLogger(Logger logger) {
      m_logger = logger;
   }

   public static Log getLogger(String name) {
      Logger logger = Logger.getLogger(name);

      return new JdkLogger(logger);
   }

   @Override
   public boolean isLogEnabled(int logLevel) {
      return m_logger.isLoggable(LevelMapper.getLevel(logLevel));
   }

   @Override
   public void log(int logLevel, Object message, Throwable t) {
      // Do nothing
   }

   private static final class LevelMapper {
      public static Level getLevel(int logLevel) {
         switch (logLevel) {
         case Log.DEBUG:
            return Level.FINE;
         case Log.INFO:
            return Level.INFO;
         case Log.WARN:
            return Level.WARNING;
         case Log.ERROR:
            return Level.SEVERE;
         default:
            throw new IllegalArgumentException("No Level can be mapped for " + logLevel);
         }
      }

      public static int getLogLevel(Level level) {
         if (level == Level.FINE)
            return Log.DEBUG;
         else if (level == Level.INFO)
            return Log.INFO;
         else if (level == Level.WARNING)
            return Log.WARN;
         else if (level == Level.SEVERE)
            return Log.ERROR;
         else
            throw new IllegalArgumentException("Unknown level:" + level);
      }
   }
}
