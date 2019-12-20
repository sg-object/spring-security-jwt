package com.sg.jwt.common.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public abstract class JodaTimeUtil {

	public static DateTime nowToDate() {
		return LocalDateTime.now().toDateTime();
	}

	public static DateTime nowAfterDaysToDate(int days) {
		return LocalDateTime.now().plusDays(days).toDateTime();
	}

	public static DateTime nowAfterHoursToDate(int hours) {
		return LocalDateTime.now().plusHours(hours).toDateTime();
	}

	public static DateTime nowAfterMinutesToDate(int minutes) {
		return LocalDateTime.now().plusMinutes(minutes).toDateTime();
	}
}
