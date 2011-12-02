package com.site.kernel.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date daysAfter(Date date, int days) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}
}
