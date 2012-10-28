package dk.teachus.backend.domain;

import org.joda.time.DateTimeConstants;

public enum WeekDay {
	MONDAY(DateTimeConstants.MONDAY), //
	TUESDAY(DateTimeConstants.TUESDAY), //
	WEDNESDAY(DateTimeConstants.WEDNESDAY), //
	THURSDAY(DateTimeConstants.THURSDAY), //
	FRIDAY(DateTimeConstants.FRIDAY), //
	SATURDAY(DateTimeConstants.SATURDAY), //
	SUNDAY(DateTimeConstants.SUNDAY);
	
	public static WeekDay parse(final String yodaWeekDayString) {
		WeekDay weekDay = null;
		final int yodaWeekDay = Integer.parseInt(yodaWeekDayString);
		
		for (final WeekDay wd : WeekDay.values()) {
			if (wd.getYodaWeekDay() == yodaWeekDay) {
				weekDay = wd;
				break;
			}
		}
		
		return weekDay;
	}
	
	private final int yodaWeekDay;
	
	private WeekDay(final int yodaWeekDay) {
		this.yodaWeekDay = yodaWeekDay;
	}
	
	public int getYodaWeekDay() {
		return yodaWeekDay;
	}
}