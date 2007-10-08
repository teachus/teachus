/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;

import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.utils.DateUtils;

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
	
	private String location;

	private double price;

	private int lessonDuration = 60;
	
	private int intervalBetweenLessonStart = 60;
	
	private int repeatEveryWeek = 1;
	
	private Status status = Status.DRAFT;

	public void addWeekDay(WeekDay weekDay) {
		weekDays.add(weekDay);
	}

	public boolean conflicts(DateTime bookedTime, DateTime time) {
		boolean conflicts = false;
		
		// On the same date
		if (bookedTime.toDateMidnight().equals(time.toDateMidnight())) {
			bookedTime = DateUtils.resetDateTime(bookedTime, time);
			time = DateUtils.resetDateTime(time, time);
			
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
			contains = interval.contains(date) || date.equals(end);
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

	public String getLocation() {
		return location;
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
			bookedTime = DateUtils.resetDateTime(bookedTime, time);
			time = DateUtils.resetDateTime(time, time);
			
				DateTime et = bookedTime.plusMinutes(lessonDuration);
			
			Interval bookedInterval = new Interval(bookedTime, et);
			
			if (bookedInterval.contains(time) && bookedTime.equals(time) == false) {
				inLesson = true;
			}
		}
		
		return inLesson;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean isTimeValid(DateTime time) {
		boolean timeValid = false;

		time = DateUtils.resetDateTime(time, time);
		DateTime st = DateUtils.resetDateTime(new DateTime(startTime), time);
		DateTime et = DateUtils.resetDateTime(new DateTime(endTime), time);
		Interval periodTimeInterval = new Interval(st, et);
		
		if (periodTimeInterval.contains(time)) {
			int timeMinutes = new Duration(st, time).toPeriod(PeriodType.minutes()).getMinutes();
			
			if (timeMinutes%intervalBetweenLessonStart == 0) {
				timeValid = true;
			}
		}
		
		return timeValid;
	}
	
	public boolean mayBook(DateTime time) {
		boolean mayBook = false;

		time = DateUtils.resetDateTime(time, time);
		DateTime et = DateUtils.resetDateTime(new DateTime(endTime), time);

		if (isTimeValid(time)) {
			time = time.plusMinutes(lessonDuration);
			if (time.equals(et) || time.isBefore(et)) {
				mayBook = true;
			}
		}

		return mayBook;
	}
	
	public void setStatus(Status status) {
		this.status = status;
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

	public void setLocation(String location) {
		this.location = location;
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
	
	public boolean isValid() {
		boolean valid = false;
		
		if (name != null && name.length() > 0
				&& startTime != null
				&& endTime != null
				&& teacher != null
				&& weekDays != null && weekDays.isEmpty() == false
				&& intervalBetweenLessonStart > 0
				&& lessonDuration > 0
				&& repeatEveryWeek > 0) {
			valid = true;
		}
		
		return valid;
	}
}
