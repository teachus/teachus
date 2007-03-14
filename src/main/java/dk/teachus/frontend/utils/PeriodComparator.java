package dk.teachus.frontend.utils;

import java.util.Comparator;
import java.util.Date;

import org.joda.time.DateTime;

import dk.teachus.domain.Period;

public class PeriodComparator implements Comparator<Period> {

	public int compare(Period o1, Period o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			if (o1.getStartTime() != null && o2.getStartTime() != null) {
				DateTime startTime1 = convertAndReset(o1.getStartTime());
				DateTime startTime2 = convertAndReset(o2.getStartTime());
				compare = startTime1.compareTo(startTime2);
			} else if (o1.getStartTime() != null) {
				compare = -1;
			} else if (o2.getStartTime() != null) {
				compare = 1;
			}
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	}
	
	private DateTime convertAndReset(Date date) {
		DateTime now = new DateTime();
		DateTime dateTime = new DateTime(date);
		
		dateTime = dateTime.withYear(now.getYear());
		dateTime = dateTime.withMonthOfYear(now.getMonthOfYear());
		dateTime = dateTime.withDayOfMonth(now.getDayOfMonth());
		dateTime = dateTime.withSecondOfMinute(0);
		dateTime = dateTime.withMillisOfSecond(0);
		
		return dateTime;
	}

}
