package dk.teachus.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
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

		private int yodaWeekDay;

		private WeekDay(int yodaWeekDay) {
			this.yodaWeekDay = yodaWeekDay;
		}

		public int getYodaWeekDay() {
			return yodaWeekDay;
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

	private int lessonDuration = 60;
	
	private int intervalBetweenLessonStart = 60;

	public void addWeekDay(WeekDay weekDay) {
		if (weekDays == null) {
			weekDays = new ArrayList<WeekDay>();
		}

		weekDays.add(weekDay);
	}

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

		while (hasWeekDay(sd.toDate()) == false) {
			sd = sd.plusDays(1);
		}

		return sd.toDate();
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getEndTime() {
		return endTime;
	}

	public int getIntervalBetweenLessonStart() {
		return intervalBetweenLessonStart;
	}

	public int getLessonDuration() {
		return lessonDuration;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public List<WeekDay> getWeekDays() {
		return weekDays;
	}

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
	
	public boolean isTimeValid(DateTime time) {
		boolean timeValid = false;

		time = resetDateTime(time, time);
		DateTime st = resetDateTime(new DateTime(startTime), time);
		DateTime et = resetDateTime(new DateTime(endTime), time);
		Interval periodTimeInterval = new Interval(st, et);
		
		if (periodTimeInterval.contains(time)) {
			int timeMinutes = time.getMinuteOfHour();
			timeMinutes -= st.getMinuteOfHour();
			
			if (timeMinutes%intervalBetweenLessonStart == 0) {
				timeValid = true;
			}
		}
		
		return timeValid;
	}
	
	public boolean mayBook(DateTime time) {
		boolean mayBook = false;

		time = resetDateTime(time, time);
		DateTime et = resetDateTime(new DateTime(endTime), time);

		if (isTimeValid(time)) {
			time = time.plusMinutes(lessonDuration);
			if (time.equals(et) || time.isBefore(et)) {
				mayBook = true;
			}
		}

		return mayBook;
	}
	
	public boolean conflicts(DateTime bookedTime, DateTime time) {
		boolean conflicts = false;
		
		// On the same date
		if (bookedTime.toDateMidnight().equals(time.toDateMidnight())) {
			bookedTime = resetDateTime(bookedTime, time);
			time = resetDateTime(time, time);
			
			DateTime st = bookedTime.minusMinutes(lessonDuration);
			DateTime et = bookedTime.plusMinutes(lessonDuration);
			
			Interval bookedInterval = new Interval(st, et);
			
			if (bookedInterval.contains(time) && st.equals(time) == false) {
				conflicts = true;
			}
		}
		
		return conflicts;
	}
	
	public boolean inLesson(DateTime bookedTime, DateTime time) {
		boolean inLesson = false;
		
		// On the same date
		if (bookedTime.toDateMidnight().equals(time.toDateMidnight())) {
			bookedTime = resetDateTime(bookedTime, time);
			time = resetDateTime(time, time);
			
				DateTime et = bookedTime.plusMinutes(lessonDuration);
			
			Interval bookedInterval = new Interval(bookedTime, et);
			
			if (bookedInterval.contains(time) && bookedTime.equals(time) == false) {
				inLesson = true;
			}
		}
		
		return inLesson;
	}
	
	private DateTime resetDateTime(DateTime time, DateTime resetTo) {
		return time
			.withDate(resetTo.getYear(), resetTo.getMonthOfYear(), resetTo.getDayOfMonth())
			.withSecondOfMinute(0)
			.withMillisOfSecond(0);
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setIntervalBetweenLessonStart(int intervalBetweenLessonStart) {
		this.intervalBetweenLessonStart = intervalBetweenLessonStart;
	}

	public void setLessonDuration(int lessonDuration) {
		this.lessonDuration = lessonDuration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setWeekDays(List<WeekDay> weekDays) {
		this.weekDays = weekDays;
	}
}
