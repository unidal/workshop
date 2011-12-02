package com.site.lookup.logger;

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;

public class TimedConsoleLoggerManager extends ConsoleLoggerManager {
   private String m_dateFormat = "yyyy-MM-dd HH:mm:ss";

   @Override
   public Logger createLogger(int threshold, String name) {
      return new TimedConsoleLogger(threshold, name, m_dateFormat);
   }

   public void setDateFormat(String dateFormat) {
      m_dateFormat = dateFormat;
   }
}
