/**
 * 
 */
package dk.teachus.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dk.teachus.domain.DatePeriod;
import dk.teachus.domain.Period;
import dk.teachus.frontend.utils.PeriodComparator;

public class DatePeriodImpl implements Serializable, DatePeriod {
	private static final long serialVersionUID = 1L;

	private Date date;

	private List<Period> periods;

	public DatePeriodImpl(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public List<Period> getPeriods() {
		Collections.sort(periods, new PeriodComparator());
		
		return periods;
	}
	
	public void addPeriod(Period period) {
		if (periods == null) {
			periods = new ArrayList<Period>();
		}
		
		periods.add(period);
	}

}