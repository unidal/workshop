package com.site.lookup.logger;

import org.junit.Test;

public class TimedConsoleLoggerTest {
	@Test
	public void testWithPattern() {
		TimedConsoleLogger logger = new TimedConsoleLogger(TimedConsoleLogger.LEVEL_INFO, "test", "MM-dd HH:mm:ss.SSS",
		      "target/logger/test.log", true);

		logger.debug("zero");
		logger.info("first");
		logger.warn("second");
	}

	@Test
	public void testWithoutPattern() throws Exception {
		TimedConsoleLogger logger = new TimedConsoleLogger(TimedConsoleLogger.LEVEL_INFO, "test", "MM-dd HH:mm:ss.SSS",
		      "target/logger/test_{0,date,ss}.log", true);

		logger.debug("zero");
		logger.info("first");
		logger.warn("second");

		Thread.sleep(1000L);

		logger.debug("zero");
		logger.info("first");
		logger.warn("second");
	}
}
