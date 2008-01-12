package dk.teachus.frontend.components;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;

public class WeekDayComparator implements Comparator<List<WeekDay>>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(List<WeekDay> o1, List<WeekDay> o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			Integer day1 = getWeekDayNumber(o1);
			Integer day2 = getWeekDayNumber(o2);
			
			compare = day1.compareTo(day2);
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	}

	private int getWeekDayNumber(List<WeekDay> weekDays) {
		int day = 10;
		for (WeekDay weekDay : weekDays) {
			int d = 10;
			if (weekDay == WeekDay.MONDAY) {
				d = 1;
			} else if (weekDay == WeekDay.TUESDAY) {
				d = 2;
			} else if (weekDay == WeekDay.WEDNESDAY) {
				d = 3;
			} else if (weekDay == WeekDay.THURSDAY) {
				d = 4;
			} else if (weekDay == WeekDay.FRIDAY) {
				d = 5;
			} else if (weekDay == WeekDay.SATURDAY) {
				d = 6;
			} else if (weekDay == WeekDay.SUNDAY) {
				d = 7;
			}
			
			if (d < day) {
				day = d;
			}
		}
		return day;
	}
	
}
