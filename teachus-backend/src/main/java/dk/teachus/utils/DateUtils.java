package dk.teachus.utils;

import org.joda.time.DateTime;

public class DateUtils {

	public static DateTime resetDateTime(DateTime time, DateTime resetTo) {
		return time.withDate(resetTo.getYear(), resetTo.getMonthOfYear(),
				resetTo.getDayOfMonth()).withSecondOfMinute(0)
				.withMillisOfSecond(0);
	}

}
