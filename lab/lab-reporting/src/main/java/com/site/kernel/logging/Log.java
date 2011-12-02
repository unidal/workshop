package com.site.kernel.logging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.site.kernel.SystemPathFinder;

public abstract class Log {
   public static final int DEBUG = 1;

   public static final int INFO = 2;

   public static final int WARN = 3;

   public static final int ERROR = 4;

   public static final int FATAL = 5;

   // used for concurrent logging
   private static final ThreadLocal EVENTS = new ThreadLocal() {
      protected Object initialValue() {
         return new ThreadLocalData();
      }
   };

   /**
    * 0 - Not initialized 
    * 1 - initialized 
    * 2 - log4j.xml not found 
    * 3 - fail to initialized
    */
   private static int log4jInitialized = 0;

   public final static Log getLog(Class clazz) {
      return getLog(clazz.getName());
   }

   public final static Log getLog(String name) {
      if (log4jInitialized == 0) {
         File configDir = SystemPathFinder.getAppConfig();

         if (new File(configDir, "log4j.xml").canRead()) {
            try {
               Log4jLogger.initialize();
               log4jInitialized = 1;
            } catch (Error e) {
               Log log = ConsoleLogger.getLogger(name);

               log.warn("Loading Log4j configuration failure, log to Console instead.", e);
               log4jInitialized = 3;
            }
         } else {
            log4jInitialized = 2;
         }
      }

      if (log4jInitialized == 1) {
         return Log4jLogger.getLogger(name);
      }

      return ConsoleLogger.getLogger(name);
   }

   public void assertLog(boolean assertion, String message) {
      if (assertion) {
         logInternal(ERROR, message, null);
      }
   }

   /**
    * Begin a concurrent logging, all of events are buffered until committed
    */
   public void begin() {
      ThreadLocalData data = (ThreadLocalData) EVENTS.get();

      if (data.isConcurrent()) {
         flush(data.getEvents());
      }

      data.setConcurrent(true);
   }

   /**
    * Commit all buffered logging
    */
   public void commit() {
      ThreadLocalData data = (ThreadLocalData) EVENTS.get();

      if (data.isConcurrent()) {
         flush(data.getEvents());
      }

      data.setConcurrent(false);
   }

   public void debug(Object message) {
      logInternal(DEBUG, message, null);
   }

   public void debug(Object message, Throwable t) {
      logInternal(DEBUG, message, t);
   }

   public void error(Object message) {
      logInternal(ERROR, message, null);
   }

   public void error(Object message, Throwable t) {
      logInternal(ERROR, message, t);
   }

   private void flush(List<LogEvent> events) {
      for (LogEvent event : events) {
         log(event.getLogLevel(), event.getMessage(), event.getThrowable());
      }

      events.clear();
   }

   public void info(Object message) {
      logInternal(INFO, message, null);
   }

   public void info(Object message, Throwable t) {
      logInternal(INFO, message, t);
   }

   public boolean isDebugEnabled() {
      return isLogEnabled(DEBUG);
   }

   public boolean isErrorEnabled() {
      return isLogEnabled(ERROR);
   }

   public boolean isInfoEnabled() {
      return isLogEnabled(INFO);
   }

   public abstract boolean isLogEnabled(int logLevel);

   public boolean isWarnEnabled() {
      return isLogEnabled(WARN);
   }

   public void log(int logLevel, Object message) {
      logInternal(logLevel, message, null);
   }

   public abstract void log(int logLevel, Object message, Throwable t);

   public void log(Throwable t) {
      logInternal(ERROR, null, t);
   }

   private void logInternal(int logLevel, Object message, Throwable t) {
      ThreadLocalData data = (ThreadLocalData) EVENTS.get();

      if (data.isConcurrent()) {
         List<LogEvent> events = data.getEvents();

         events.add(new LogEvent(logLevel, message, t));
      } else {
         log(logLevel, message, t);
      }
   }

   public void warn(Object message) {
      logInternal(WARN, message, null);
   }

   public void warn(Object message, Throwable t) {
      logInternal(WARN, message, t);
   }

   static final class LogEvent {
      private int m_logLevel;

      private Object m_message;

      private Throwable m_throwable;

      public LogEvent(int logLevel, Object message, Throwable throwable) {
         m_logLevel = logLevel;
         m_message = message;
         m_throwable = throwable;
      }

      public int getLogLevel() {
         return m_logLevel;
      }

      public Object getMessage() {
         return m_message;
      }

      public Throwable getThrowable() {
         return m_throwable;
      }
   }

   static final class ThreadLocalData extends ThreadLocal {
      private boolean m_concurrent;

      private List<LogEvent> m_events;

      public ThreadLocalData() {
         m_events = new ArrayList<LogEvent>(100);
         m_concurrent = false;
      }

      public List<LogEvent> getEvents() {
         return m_events;
      }

      public boolean isConcurrent() {
         return m_concurrent;
      }

      public void setConcurrent(boolean concurrent) {
         m_concurrent = concurrent;
      }

      public void setEvents(List<LogEvent> events) {
         m_events = events;
      }

   }

}