package dk.teachus.domain;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateMidnight;

public interface Periods extends Serializable {

	List<Period> getPeriods();
	void setPeriods(List<Period> result);

	void addPeriod(Period period);

	boolean hasDate(DateMidnight date);
	
	boolean containsDate(DateMidnight date);
	
	boolean hasPeriodBefore(DateMidnight date);
	
	boolean hasPeriodAfter(DateMidnight date);

	List<DatePeriod> generateDatesForWeek(DateMidnight startDate);
	
	List<DatePeriod> generateDates(DateMidnight startDate, int numberOfDays);

	int numberOfWeeksBack(DateMidnight lastDate, int numberOfDays);

}