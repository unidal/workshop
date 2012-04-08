package com.site.lookup.logger;

import java.text.MessageFormat;
import java.util.Date;

import org.codehaus.plexus.logging.AbstractLogger;
import org.codehaus.plexus.logging.Logger;

public class TimedConsoleLogger extends AbstractLogger implements Logger {
   private MessageFormat m_format;

   public TimedConsoleLogger(int threshold, String name, String dateFormat) {
      super(threshold, name);

      m_format = new MessageFormat("[{0,date," + dateFormat + "}] [{1}] {2}");
   }

   private String getTimedMessage(String level, String message) {
      return m_format.format(new Object[] { new Date(), level, message });
   }

   @Override
   public void debug(String message, Throwable throwable) {
      if (isDebugEnabled()) {
         System.out.println(getTimedMessage("DEBUG", message));

         if (throwable != null) {
            throwable.printStackTrace(System.out);
         }
      }
   }

   @Override
   public void info(String message, Throwable throwable) {
      if (isInfoEnabled()) {
         System.out.println(getTimedMessage("INFO", message));

         if (throwable != null) {
            throwable.printStackTrace(System.out);
         }
      }
   }

   @Override
   public void warn(String message, Throwable throwable) {
      if (isWarnEnabled()) {
         System.out.println(getTimedMessage("WARN", message));

         if (throwable != null) {
            throwable.printStackTrace(System.out);
         }
      }
   }

   @Override
   public void error(String message, Throwable throwable) {
      if (isErrorEnabled()) {
         System.out.println(getTimedMessage("ERROR", message));

         if (throwable != null) {
            throwable.printStackTrace(System.out);
         }
      }
   }

   @Override
   public void fatalError(String message, Throwable throwable) {
      if (isFatalErrorEnabled()) {
         System.out.println(getTimedMessage("FATAL", message));

         if (throwable != null) {
            throwable.printStackTrace(System.out);
         }
      }
   }

   @Override
   public Logger getChildLogger(String name) {
      return this;
   }
}
