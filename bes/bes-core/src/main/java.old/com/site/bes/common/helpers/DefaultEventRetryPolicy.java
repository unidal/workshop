package com.site.bes.common.helpers;

import java.util.Calendar;
import java.util.Date;

import com.site.bes.Event;
import com.site.bes.EventRetryPolicy;

public class DefaultEventRetryPolicy implements EventRetryPolicy {
	private int m_retryIntervalInSecond;

	public DefaultEventRetryPolicy() {
		this(5 * 60); // 5 minutes later
	}

	public DefaultEventRetryPolicy(int retryIntervalInSecond) {
		m_retryIntervalInSecond = retryIntervalInSecond;
	}

	public Date getNextScheduleDate(Event event) {
		Calendar cal = Calendar.getInstance();
		Date date = event.getNextScheduleDate();

		if (date == null || date.before(cal.getTime())) {
			cal.add(Calendar.SECOND, m_retryIntervalInSecond);
			date = cal.getTime();
		}

		return date;
	}
}
