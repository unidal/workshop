package com.site.lookup.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.codehaus.plexus.logging.AbstractLogger;
import org.codehaus.plexus.logging.Logger;

public class TimedConsoleLogger extends AbstractLogger implements Logger {
	private MessageFormat m_format;

	private ReentrantLock m_lock = new ReentrantLock();

	private String m_logFilePattern;

	private BufferedWriter m_writer;

	private MessageFormat m_logFileFormat;

	private String m_lastPath;

	private boolean m_showClass;

	private boolean m_devMode;

	public TimedConsoleLogger(int threshold, String name, String dateFormat, String logFilePattern, boolean showClass,
	      boolean devMode) {
		super(threshold, name);

		String pattern;

		if (showClass) {
			pattern = "[{0,date," + dateFormat + "}] [{1}] [{3}] {2}";
		} else {
			pattern = "[{0,date," + dateFormat + "}] [{1}] {2}";
		}

		m_showClass = showClass;
		m_format = new MessageFormat(pattern);
		m_logFilePattern = logFilePattern;
		m_devMode = devMode;

		if (logFilePattern != null && logFilePattern.indexOf("{0,") >= 0) {
			m_logFileFormat = new MessageFormat(logFilePattern);

			// IllegalArgumentException will be thrown for invalid pattern
			m_logFileFormat.format(new Object[] { new Date() });
		}

		// override by command line
		String mode = System.getProperty("devMode", "false");

		if ("true".equals(mode)) {
			m_devMode = true;
		}
	}

	private String getTimedMessage(String level, String message) {
		if (m_showClass) {
			return m_format.format(new Object[] { new Date(), level, message, getCallerClassName() });
		} else {
			return m_format.format(new Object[] { new Date(), level, message });
		}
	}

	private String getCallerClassName() {
		StackTraceElement[] elements = new Exception().getStackTrace();

		if (elements.length > 5) {
			String className = elements[5].getClassName();
			int pos = className.lastIndexOf('.');

			if (pos > 0) {
				return className.substring(pos + 1);
			} else {
				return className;
			}
		}

		return "N/A";
	}

	@Override
	public void debug(String message, Throwable throwable) {
		if (isDebugEnabled()) {
			out("DEBUG", message, throwable);
		}
	}

	private void out(String severity, String message, Throwable throwable) {
		m_lock.lock();

		try {
			String timedMessage = getTimedMessage(severity, message);

			if (m_devMode || m_logFilePattern == null || m_logFilePattern.length() == 0) {
				System.out.println(timedMessage);

				if (throwable != null) {
					throwable.printStackTrace(System.out);
				}
			} else {
				try {
					BufferedWriter writer = getWriter();

					writer.write(timedMessage);
					writer.newLine();

					if (throwable != null) {
						throwable.printStackTrace(new PrintWriter(writer));
					}

					writer.flush();
				} catch (Exception e) {
					System.out.println(getTimedMessage("ERROR", e.toString()));
				}
			}
		} finally {
			m_lock.unlock();
		}
	}

	private BufferedWriter getWriter() throws IOException {
		if (m_logFileFormat != null) {
			String path = m_logFileFormat.format(new Object[] { new Date() });

			if (!path.equals(m_lastPath)) {
				// close last one
				if (m_writer != null) {
					try {
						m_writer.close();
					} catch (IOException e) {
						// ignore it
					}
				}

				File file = new File(path);

				file.getParentFile().mkdirs();

				FileOutputStream fos = new FileOutputStream(file, true);

				m_writer = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
				m_lastPath = path;
			}
		} else if (m_writer == null) {
			File file = new File(m_logFilePattern);

			file.getParentFile().mkdirs();

			FileOutputStream fos = new FileOutputStream(file, true);
			m_writer = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
			m_lastPath = m_logFilePattern;
		}

		return m_writer;
	}

	@Override
	public void info(String message, Throwable throwable) {
		if (isInfoEnabled()) {
			out("INFO", message, throwable);
		}
	}

	@Override
	public void warn(String message, Throwable throwable) {
		if (isWarnEnabled()) {
			out("WARN", message, throwable);
		}
	}

	@Override
	public void error(String message, Throwable throwable) {
		if (isErrorEnabled()) {
			out("ERROR", message, throwable);
		}
	}

	@Override
	public void fatalError(String message, Throwable throwable) {
		if (isFatalErrorEnabled()) {
			out("FATAL", message, throwable);
		}
	}

	@Override
	public Logger getChildLogger(String name) {
		return this;
	}
}
