package dk.teachus.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.Weeks;

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

	private List<WeekDay> weekDays = new ArrayList<WeekDay>();

	private Date startTime;

	private Date endTime;

	private Teacher teacher;

	private double price;

	private int lessonDuration = 60;
	
	private int intervalBetweenLessonStart = 60;
	
	private int repeatEveryWeek = 1;
	
	private boolean active;

	public void addWeekDay(WeekDay weekDay) {
		weekDays.add(weekDay);
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

	public boolean dateIntervalContains(DateMidnight date) {
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
			contains = interval.contains(date);
		} else if (start != null) {
			contains = date.isAfter(start) || date.equals(start);
		} else if (end != null) {
			contains = date.isBefore(end) || date.equals(end);
		} else {
			contains = true;
		}

		return contains;
	}

	public DateMidnight generateDate(DateMidnight startDate) {
		if (hasDate(startDate) == false) {
			return null;
		}

		if (beginDate != null) {
			DateMidnight bd = new DateMidnight(beginDate);
			if (startDate.isBefore(bd)) {
				startDate = bd;
			}
		}

		while (hasWeekDay(startDate) == false) {
			startDate = startDate.plusDays(1);
		}

		return startDate;
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

	public int getRepeatEveryWeek() {
		return repeatEveryWeek;
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

	public boolean hasDate(DateMidnight date) {
		boolean hasDate = false;

		// Check weekday
		if (hasWeekDay(date)) {
			// Check start/end date
			if (dateIntervalContains(date)) {
				// If the period has a start date also consider the 
				// repeatEveryWeek factor
				if (beginDate != null && repeatEveryWeek > 1) {
					int weeks = Weeks.weeksBetween(new DateMidnight(beginDate), new DateMidnight(date)).getWeeks();
					if (weeks%repeatEveryWeek == 0) {
						hasDate = true;
					}
				} else {
					hasDate = true;
				}
			}
		}

		return hasDate;
	}

	public boolean hasWeekDay(DateMidnight weekDayDate) {
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

	public boolean isActive() {
		return active;
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
	
	private DateTime resetDateTime(DateTime time, DateTime resetTo) {
		return time
			.withDate(resetTo.getYear(), resetTo.getMonthOfYear(), resetTo.getDayOfMonth())
			.withSecondOfMinute(0)
			.withMillisOfSecond(0);
	}
	
	public void setActive(boolean active) {
		this.active = active;
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

	public void setRepeatEveryWeek(int repeatEveryWeek) {
		this.repeatEveryWeek = repeatEveryWeek;
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
