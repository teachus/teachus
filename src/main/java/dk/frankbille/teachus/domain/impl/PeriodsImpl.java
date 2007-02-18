package dk.frankbille.teachus.domain.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;

import dk.frankbille.teachus.domain.DatePeriod;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;

public class PeriodsImpl implements Periods {
	private static final long serialVersionUID = 1L;

	private List<Period> periods = new ArrayList<Period>();

	/* (non-Javadoc)
	 * @see dk.frankbille.teachus.domain.impl.Periods#getPeriods()
	 */
	public List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}

	/* (non-Javadoc)
	 * @see dk.frankbille.teachus.domain.impl.Periods#addPeriod(dk.frankbille.teachus.domain.Period)
	 */
	public void addPeriod(Period period) {		
		periods.add(period);
	}
	
	/* (non-Javadoc)
	 * @see dk.frankbille.teachus.domain.impl.Periods#hasDate(java.util.Date)
	 */
	public boolean hasDate(Date date) {
		boolean hasDate = false;
		
		for (Period period : periods) {
			if (period.hasDate(date)) {
				hasDate = true;
				break;
			}
		}
		
		return hasDate;
	}
	
	/* (non-Javadoc)
	 * @see dk.frankbille.teachus.domain.impl.Periods#generateDatesForWeek(java.util.Date)
	 */
	public List<DatePeriod> generateDatesForWeek(Date startDate) {
		List<DatePeriod> dates = new ArrayList<DatePeriod>();
		DateMidnight sd = new DateMidnight(startDate).withDayOfWeek(DateTimeConstants.MONDAY);
		int week = sd.getWeekOfWeekyear();
		
		boolean dateInPeriods = true;
		while(week == sd.getWeekOfWeekyear() && dateInPeriods == true) {
			dateInPeriods = false;
			
			DatePeriod datePeriod = null;
			for (Period period : periods) {
				// Check if this period can handle the date at all
				if (period.dateIntervalContains(sd.toDate())) {
					dateInPeriods = true;
				
					Date date = period.generateDate(sd.toDate());
					if (date != null) {
						if (datePeriod == null) {
							datePeriod = new DatePeriodImpl(date);
							dates.add(datePeriod);
						}
						
						datePeriod.addPeriod(period);
					}
				}
			}
			
			sd = sd.plusDays(1);
		}
		
		Collections.sort(dates, new Comparator<DatePeriod>() {
			public int compare(DatePeriod o1, DatePeriod o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		return dates;
	}
	
	
	
	/* (non-Javadoc)
	 * @see dk.frankbille.teachus.domain.impl.Periods#calculateNumberOfWeeks(java.util.Date, int)
	 */
	public int calculateNumberOfWeeks(Date lastDate, int numberOfDays) {
		int numberOfWeeks = 0;
		DateMidnight ld = new DateMidnight(lastDate);
		
		int dates = 0;
		int weekDates = generateDatesForWeek(ld.toDate()).size();
		if (weekDates > 0) {
			do {
				numberOfWeeks++;
				dates += weekDates;
				ld = ld.minusWeeks(1);
				weekDates = generateDatesForWeek(ld.toDate()).size();
			} while(dates+weekDates <= numberOfDays && weekDates > 0);
		}
		
		return numberOfWeeks;
	}
}
