package com.site.kernel.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This is a wrapper class for log4j
 */
final class Log4jLogger extends Log {
   private Logger m_logger;

   private int m_logLevel;

   private static final String FQCN = Log.class.getName();

   private Log4jLogger(Logger logger) {
      m_logger = logger;
      m_logLevel = LevelMapper.getLogLevel(logger.getEffectiveLevel());
   }

   public static void initialize() {
      Log4jConfigurator.init();
   }

   public static Log getLogger(String name) {
      Logger logger = LogManager.getLogger(name);

      return new Log4jLogger(logger);
   }

   public Logger getLogger() {
      return m_logger;
   }

   public Appender getCalAppender() {
      return Log4jConfigurator.getCalAppender();
   }

   public boolean isLogEnabled(int logLevel) {
      if (m_logLevel <= logLevel) {
         Level level = LevelMapper.getLevel(logLevel);

         return m_logger.isEnabledFor(level);
      } else {
         return false;
      }
   }

   public void log(int logLevel, Object message, Throwable t) {
      if (m_logLevel <= logLevel) {
         Level level = LevelMapper.getLevel(logLevel);
         LoggingEvent event = new LoggingEvent(FQCN, m_logger, level, message, t);

         m_logger.callAppenders(event);
      }
   }

   private static final class LevelMapper {
      public static Level getLevel(int logLevel) {
         switch (logLevel) {
         case Log.DEBUG:
            return Level.DEBUG;
         case Log.INFO:
            return Level.INFO;
         case Log.WARN:
            return Level.WARN;
         case Log.ERROR:
            return Level.ERROR;
         default:
            throw new IllegalArgumentException("No Level can be mapped for " + logLevel);
         }
      }

      public static int getLogLevel(Level level) {
         if (level == Level.DEBUG)
            return Log.DEBUG;
         else if (level == Level.INFO)
            return Log.INFO;
         else if (level == Level.WARN)
            return Log.WARN;
         else if (level == Level.ERROR)
            return Log.ERROR;
         else
            throw new IllegalArgumentException("Unknown level:" + level);
      }
   }
}