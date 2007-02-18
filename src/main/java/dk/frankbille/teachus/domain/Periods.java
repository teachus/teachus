package dk.frankbille.teachus.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface Periods extends Serializable {

	List<Period> getPeriods();
	void setPeriods(List<Period> result);

	void addPeriod(Period period);

	boolean hasDate(Date date);

	List<DatePeriod> generateDatesForWeek(Date startDate);

	int calculateNumberOfWeeks(Date lastDate, int numberOfDays);

}