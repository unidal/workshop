package com.site.kernel.logging;

import com.site.kernel.common.BaseTestCase;

public class LogTest extends BaseTestCase {
	public void testLog() {
		Log log = Log.getLog(LogTest.class);

		log.debug("DEBUG");
		log.info("INFO");
		log.warn("WARN");
		log.error("ERROR");

		log.begin();
		log.info("Concurrent logging start");

		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("Concurrent logging end - 20 ms later");
		log.commit();
	}
}
