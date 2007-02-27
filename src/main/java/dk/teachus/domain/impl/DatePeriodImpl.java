/**
 * 
 */
package dk.teachus.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.teachus.domain.DatePeriod;
import dk.teachus.domain.Period;

public class DatePeriodImpl implements Serializable, DatePeriod {
	private static final long serialVersionUID = 1L;

	private Date date;

	private List<Period> periods;

	public DatePeriodImpl(Date date) {
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.DatePeriod#getDate()
	 */
	public Date getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.DatePeriod#getPeriods()
	 */
	public List<Period> getPeriods() {
		return periods;
	}
	
	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.DatePeriod#addPeriod(dk.teachus.domain.Period)
	 */
	public void addPeriod(Period period) {
		if (periods == null) {
			periods = new ArrayList<Period>();
		}
		
		periods.add(period);
	}

}