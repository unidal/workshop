package com.site.kernel.logging.cal;

import com.site.kernel.common.BaseTestCase;

public class CalFactoryTest extends BaseTestCase {
	private static final long SLEEP_MILLIS = 50;

	public void testAbnormalCase() {
		CalTransaction ct = CalFactory.createCalTransaction("URL", "abc1");

		ct.addData("name1", "value1");
		ct.addData("name2", "value2");
		ct.addData("name3=value3&name4=value4&");

		CalTransaction ct2 = CalFactory.createCalTransaction("FILE", "test2");

		try {
			Thread.sleep(SLEEP_MILLIS);

			CalTransaction ct3 = CalFactory.createCalTransaction("FILE", "test3");
			Thread.sleep(SLEEP_MILLIS);
			ct3.complete();

			CalTransaction ct0 = CalFactory.getCalTransaction("URL");
			ct0.addData("test", "URL");

			CalTransaction ct4 = CalFactory.createCalTransaction("FILE", "test4");
			Thread.sleep(SLEEP_MILLIS);
			ct4.complete();

			CalTransaction ct1 = CalFactory.getCalTransaction("FILE");

			CalEvent ce1 = CalFactory.createCalEvent("Info", "cal-event info", "-1", "abd");
			ce1.complete();

			CalHeartbeat ce2 = CalFactory.createCalHeartbeat("NewEntry", "cal-heartbeat", "abd");
			ce2.complete();

			ct1.addData("test", "FILE");
			ct1.setStatus("-1");

			ct2.complete();

			Thread.sleep(SLEEP_MILLIS);
		} catch (InterruptedException ex) {
		}

		ct.complete();
		ct2.complete();

		CalTransaction ctt = CalFactory.createCalTransaction("URL", "ViewItem");

		ctt.addData("pid", "12345678");
		ctt.addData("param", new String[] { "123", null, "456" });

		try {
			Thread.sleep(SLEEP_MILLIS);
		} catch (InterruptedException ex) {
		}

		CalFactory.createCalEvent("Info", "TestEvent").complete("OK");
		CalFactory.createCalHeartbeat("HB", "TestHeartbeat").complete();

		CalFactory.createCalTransaction("SQL", "test-sql").setStatus("FAIL");

		CalFactory.getCalTransaction("URL").addData("中文行不行啊");
		ctt.complete();
	}

	public void testNormalCase() {
		CalTransaction ct = CalFactory.createCalTransaction("URL", "abc1");

		ct.addData("name1", "value1");
		ct.addData("name2", "value2");
		ct.addData("name3=value3&name4=value4&");

		CalTransaction ct2 = CalFactory.createCalTransaction("FILE", "test2");

		CalTransaction ct3 = CalFactory.createCalTransaction("FILE", "test3");
		ct3.complete();

		CalTransaction ct0 = CalFactory.getCalTransaction("URL");
		ct0.addData("test", "URL");

		CalTransaction ct4 = CalFactory.createCalTransaction("FILE", "test4");
		ct4.complete();

		CalEvent ce1 = CalFactory.createCalEvent("Info", "cal-event info", "-1", "abd");
		ce1.complete();

		CalHeartbeat ce2 = CalFactory.createCalHeartbeat("NewEntry", "cal-heartbeat", "abd");
		ce2.complete();

		ct2.complete();

		ct.complete();

		CalTransaction ctt = CalFactory.createCalTransaction("URL", "ViewItem");

		ctt.addData("pid", "12345678");
		ctt.addData("param", new String[] { "123", null, "456" });

		CalFactory.createCalEvent("Info", "TestEvent").complete("OK");
		CalFactory.createCalHeartbeat("HB", "TestHeartbeat").complete();

		CalFactory.createCalTransaction("SQL", "test-sql").complete("FAIL");

		CalFactory.getCalTransaction("URL").addData("中文行不行啊");
		ctt.complete();
	}

}
