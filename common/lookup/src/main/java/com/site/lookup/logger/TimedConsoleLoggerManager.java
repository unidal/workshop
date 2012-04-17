package com.site.lookup.logger;

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;

public class TimedConsoleLoggerManager extends ConsoleLoggerManager {
	private String m_dateFormat = "MM-dd HH:mm:ss.SSS";

	private String m_logDir;

	private boolean m_showClass;

	@Override
	public Logger createLogger(int threshold, String name) {
		return new TimedConsoleLogger(threshold, name, m_dateFormat, m_logDir, m_showClass);
	}

	public void setDateFormat(String dateFormat) {
		m_dateFormat = dateFormat;
	}

	public void setLogDir(String logFile) {
		m_logDir = logFile;
	}

	public void setShowClass(boolean showClass) {
		m_showClass = showClass;
	}
}
