package dk.teachus.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

import dk.teachus.domain.Period;
import dk.teachus.domain.Teacher;

public class PeriodImpl extends AbstractHibernateObject implements Serializable, Period {
	public static enum WeekDay {
		MONDAY(DateTimeConstants.MONDAY),
		TUESDAY(DateTimeConstants.TUESDAY),
		WEDNESDAY(DateTimeConstants.WEDNESDAY),
		THURSDAY(DateTimeConstants.THURSDAY),
		FRIDAY(DateTimeConstants.FRIDAY),
		SATURDAY(DateTimeConstants.SATURDAY),
		SUNDAY(DateTimeConstants.SUNDAY);
		
		private int yodaWeekDay;

		public int getYodaWeekDay() {
			return yodaWeekDay;
		}

		private WeekDay(int yodaWeekDay) {
			this.yodaWeekDay = yodaWeekDay;
		}
		
		public static WeekDay parse(String yodaWeekDayString) {
			WeekDay weekDay = null;
			int yodaWeekDay = Integer.parseInt(yodaWeekDayString);
			
			for (WeekDay wd : values()) {
				if (wd.getYodaWeekDay() == yodaWeekDay) {
					weekDay = wd;
					break;
				}
			}
			
			return weekDay;
		}
	}

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private Date beginDate;

	private Date endDate;
	
	private List<WeekDay> weekDays;
	
	private Date startTime;
	
	private Date endTime;
	
	private Teacher teacher;
	
	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#getEndDate()
	 */
	public Date getEndDate() {
		return endDate;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#getEndTime()
	 */
	public Date getEndTime() {
		return endTime;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#getStartDate()
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#getStartTime()
	 */
	public Date getStartTime() {
		return startTime;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#getWeekDays()
	 */
	public List<WeekDay> getWeekDays() {
		return weekDays;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#setEndDate(java.util.Date)
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#setEndTime(java.util.Date)
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#setStartDate(java.util.Date)
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#setStartTime(java.util.Date)
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setWeekDays(List<WeekDay> weekDays) {
		this.weekDays = weekDays;
	}
	
	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#addWeekDay(dk.teachus.domain.impl.PeriodImpl.WeekDay)
	 */
	public void addWeekDay(WeekDay weekDay) {
		if (weekDays == null) {
			weekDays = new ArrayList<WeekDay>();
		}
		
		weekDays.add(weekDay);
	}
	
	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#hasWeekDay(java.util.Date)
	 */
	public boolean hasWeekDay(Date date) {
		DateMidnight weekDayDate = new DateMidnight(date);
		weekDayDate.getDayOfWeek();
		
		boolean hasWeekDay = false;
		for (WeekDay weekDay : weekDays) {
			if (weekDay.getYodaWeekDay() == weekDayDate.getDayOfWeek()) {
				hasWeekDay = true;
				break;
			}
		}
		
		return hasWeekDay;
	}
	
	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#dateIntervalContains(java.util.Date)
	 */
	public boolean dateIntervalContains(Date date) {
		DateMidnight d = new DateMidnight(date);
		DateMidnight start = null;
		if (beginDate != null) {
			start = new DateMidnight(beginDate);
		}
		DateMidnight end = null;
		if (endDate != null) {
			end = new DateMidnight(endDate);
		}
		boolean contains = false;
		
		if (start != null && end != null) {
			Interval interval = new Interval(start, end);
			contains = interval.contains(d);
		} else if (start != null) {
			contains = d.isAfter(start) || d.equals(start);
		} else if (end != null) {
			contains = d.isBefore(end) || d.equals(end);
		} else {
			contains = true;
		}
				
		return contains;
	}
	
	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#hasDate(java.util.Date)
	 */
	public boolean hasDate(Date date) {
		boolean hasDate = false;
		
		// Check weekday
		if (hasWeekDay(date)) {
			// Check start/end date
			if (dateIntervalContains(date)) {
				hasDate = true;
			}
		}
		
		return hasDate;
	}
	
	/* (non-Javadoc)
	 * @see dk.teachus.domain.impl.Period#generateDate(java.util.Date)
	 */
	public Date generateDate(Date startDate) {
		DateMidnight sd = new DateMidnight(startDate);

		if (hasDate(startDate) == false) {
			return null;
		}
		
		if (beginDate != null) {
			DateMidnight bd = new DateMidnight(beginDate);
			if (sd.isBefore(bd)) {
				sd = bd;
			}
		}
		
		while(hasWeekDay(sd.toDate()) == false) {
			sd = sd.plusDays(1);
		}
		
		return sd.toDate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
