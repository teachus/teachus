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
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;

import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.TeachUsDate;
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

	private TeachUsDate beginDate;

	private TeachUsDate endDate;

	private List<WeekDay> weekDays = new ArrayList<WeekDay>();

	private TeachUsDate startTime;

	private TeachUsDate endTime;

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

	public boolean conflicts(TeachUsDate bookedTime, TeachUsDate time) {
		boolean conflicts = false;
		
		// On the same date
		if (bookedTime.getDateMidnight().equals(time.getDateMidnight())) {
			bookedTime = DateUtils.resetDateTime(bookedTime, time);
			time = DateUtils.resetDateTime(time, time);
			
			DateTime st = bookedTime.getDateTime().minusMinutes(lessonDuration);
			DateTime et = bookedTime.getDateTime().plusMinutes(lessonDuration);
			
			Interval bookedInterval = new Interval(st, et);
			
			if (bookedInterval.contains(time.getDateTime()) && st.equals(time.getDateTime()) == false) {
				conflicts = true;
			}
		}
		
		return conflicts;
	}

	public boolean dateIntervalContains(TeachUsDate date) {
		DateMidnight start = null;
		if (beginDate != null) {
			start = beginDate.getDateMidnight();
		}
		DateMidnight end = null;
		if (endDate != null) {
			end = endDate.getDateMidnight();
		}
		boolean contains = false;

		if (start != null && end != null) {
			Interval interval = new Interval(start, end);
			contains = interval.contains(date.getDateMidnight()) || date.getDateMidnight().equals(end);
		} else if (start != null) {
			contains = date.getDateMidnight().isAfter(start) || date.getDateMidnight().equals(start);
		} else if (end != null) {
			contains = date.getDateMidnight().isBefore(end) || date.getDateMidnight().equals(end);
		} else {
			contains = true;
		}

		return contains;
	}

	public TeachUsDate generateDate(TeachUsDate startDate) {
		if (hasDate(startDate) == false) {
			return null;
		}

		if (beginDate != null) {
			if (startDate.isBefore(beginDate)) {
				startDate = beginDate;
			}
		}

		while (hasWeekDay(startDate) == false) {
			startDate = startDate.plusDays(1);
		}

		return startDate;
	}

	public TeachUsDate getBeginDate() {
		return beginDate;
	}

	public TeachUsDate getEndDate() {
		return endDate;
	}

	public TeachUsDate getEndTime() {
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

	public TeachUsDate getStartTime() {
		return startTime;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public List<WeekDay> getWeekDays() {
		return weekDays;
	}

	public boolean hasDate(TeachUsDate date) {
		boolean hasDate = false;

		// Check weekday
		if (hasWeekDay(date)) {
			// Check start/end date
			if (dateIntervalContains(date)) {
				// If the period has a start date also consider the 
				// repeatEveryWeek factor
				if (beginDate != null && repeatEveryWeek > 1) {
					int weeks = Weeks.weeksBetween(beginDate.getDateMidnight(), date.getDateMidnight()).getWeeks();
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

	public boolean hasWeekDay(TeachUsDate weekDayDate) {
		DateMidnight dateMidnight = weekDayDate.getDateMidnight();
		dateMidnight.getDayOfWeek();

		boolean hasWeekDay = false;
		for (WeekDay weekDay : weekDays) {
			if (weekDay.getYodaWeekDay() == dateMidnight.getDayOfWeek()) {
				hasWeekDay = true;
				break;
			}
		}

		return hasWeekDay;
	}

	public boolean inLesson(TeachUsDate bookedTime, TeachUsDate time) {
		boolean inLesson = false;
		
		// On the same date
		if (bookedTime.getDateMidnight().equals(time.getDateMidnight())) {
			bookedTime = DateUtils.resetDateTime(bookedTime, time);
			time = DateUtils.resetDateTime(time, time);
			
			DateTime et = bookedTime.getDateTime().plusMinutes(lessonDuration);
			
			Interval bookedInterval = new Interval(bookedTime.getDateTime(), et);
			
			if (bookedInterval.contains(time.getDateTime()) && bookedTime.equals(time) == false) {
				inLesson = true;
			}
		}
		
		return inLesson;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean isTimeValid(TeachUsDate time) {
		boolean timeValid = false;

		time = DateUtils.resetDateTime(time, time);
		TeachUsDate st = DateUtils.resetDateTime(startTime, time);
		TeachUsDate et = DateUtils.resetDateTime(endTime, time);
		Interval periodTimeInterval = new Interval(st.getDateTime(), et.getDateTime());
		
		if (periodTimeInterval.contains(time.getDateTime())) {
			int timeMinutes = new Duration(st.getDateTime(), time.getDateTime()).toPeriod(PeriodType.minutes()).getMinutes();
			
			if (timeMinutes%intervalBetweenLessonStart == 0) {
				timeValid = true;
			}
		}
		
		return timeValid;
	}
	
	public boolean mayBook(TeachUsDate time) {
		boolean mayBook = false;

		time = DateUtils.resetDateTime(time, time);
		TeachUsDate et = DateUtils.resetDateTime(endTime, time);

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

	public void setBeginDate(TeachUsDate beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(TeachUsDate endDate) {
		this.endDate = endDate;
	}

	public void setEndTime(TeachUsDate endTime) {
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

	public void setStartTime(TeachUsDate startTime) {
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
