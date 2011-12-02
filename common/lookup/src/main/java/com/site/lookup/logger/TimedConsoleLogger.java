package com.site.lookup.logger;

import java.text.MessageFormat;
import java.util.Date;

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;

public class TimedConsoleLogger implements Logger {
   private ConsoleLogger m_logger;

   private MessageFormat m_format;

   public TimedConsoleLogger(int threshold, String name, String dateFormat) {
      m_logger = new ConsoleLogger(threshold, name);
      m_format = new MessageFormat("[{0,date," + dateFormat + "}] {1}");
   }

   private String getTimedMessage(String message) {
      return m_format.format(new Object[] { new Date(), message });
   }

   public void debug(String message, Throwable throwable) {
      m_logger.debug(getTimedMessage(message), throwable);
   }

   public void debug(String message) {
      m_logger.debug(getTimedMessage(message));
   }

   public void error(String message, Throwable throwable) {
      m_logger.error(getTimedMessage(message), throwable);
   }

   public void error(String message) {
      m_logger.error(getTimedMessage(message));
   }

   public void fatalError(String message, Throwable throwable) {
      m_logger.fatalError(getTimedMessage(message), throwable);
   }

   public void fatalError(String message) {
      m_logger.fatalError(getTimedMessage(message));
   }

   public Logger getChildLogger(String name) {
      return m_logger.getChildLogger(name);
   }

   public String getName() {
      return m_logger.getName();
   }

   public int getThreshold() {
      return m_logger.getThreshold();
   }

   public void info(String message, Throwable throwable) {
      m_logger.info(getTimedMessage(message), throwable);
   }

   public void info(String message) {
      m_logger.info(getTimedMessage(message));
   }

   public boolean isDebugEnabled() {
      return m_logger.isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return m_logger.isErrorEnabled();
   }

   public boolean isFatalErrorEnabled() {
      return m_logger.isFatalErrorEnabled();
   }

   public boolean isInfoEnabled() {
      return m_logger.isInfoEnabled();
   }

   public boolean isWarnEnabled() {
      return m_logger.isWarnEnabled();
   }

   public void setThreshold(int threshold) {
      m_logger.setThreshold(threshold);
   }

   public void warn(String message, Throwable throwable) {
      m_logger.warn(getTimedMessage(message), throwable);
   }

   public void warn(String message) {
      m_logger.warn(getTimedMessage(message));
   }
}
