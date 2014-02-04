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
import java.util.StringTokenizer;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;

import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.PeriodStatus;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.WeekDay;
import dk.teachus.utils.DateUtils;

@PersistenceCapable(table = "period")
public class PeriodImpl extends AbstractJdoObject implements Serializable, Period {
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private String name;
	
	@Persistent(column = "begin_date")
	private DateMidnight beginDate;
	
	@Persistent(column = "end_date")
	private DateMidnight endDate;
	
	@NotPersistent
	private List<WeekDay> weekDays = new ArrayList<WeekDay>();
	
	@Persistent(column = "start_time")
	private LocalTime startTime;
	
	@Persistent(column = "end_time")
	private LocalTime endTime;
	
	@Persistent(column = "teacher_id")
	@Extension(vendorName = "datanucleus", key = "implementation-classes", value = "dk.teachus.backend.domain.impl.TeacherImpl")
	private Teacher teacher;
	
	@Persistent
	private String location;
	
	@Persistent
	private double price;
	
	@Persistent(column = "lesson_duration")
	private int lessonDuration = 60;
	
	@Persistent(column = "interval_between_lesson_start")
	private int intervalBetweenLessonStart = 60;
	
	@Persistent(column = "repeat_every_week")
	private int repeatEveryWeek = 1;
	
	@Persistent
	private PeriodStatus status = PeriodStatus.DRAFT;
	
	@Override
	public void addWeekDay(final WeekDay weekDay) {
		weekDays.add(weekDay);
	}
	
	@Override
	public boolean conflicts(DateTime bookedTime, DateTime time) {
		boolean conflicts = false;
		
		// On the same date
		if (bookedTime.toDateMidnight().equals(time.toDateMidnight())) {
			bookedTime = DateUtils.resetDateTime(bookedTime, time);
			time = DateUtils.resetDateTime(time, time);
			
			final DateTime st = bookedTime.minusMinutes(lessonDuration);
			final DateTime et = bookedTime.plusMinutes(lessonDuration);
			
			final Interval bookedInterval = new Interval(st, et);
			
			if (bookedInterval.contains(time) && st.equals(time) == false) {
				conflicts = true;
			}
		}
		
		return conflicts;
	}
	
	@Override
	public boolean dateIntervalContains(final DateMidnight date) {
		final DateMidnight start = beginDate;
		final DateMidnight end = endDate;
		boolean contains = false;
		
		if (start != null && end != null) {
			final Interval interval = new Interval(start, end);
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
	
	@Override
	public DateMidnight generateDate(DateMidnight startDate) {
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
	
	@Override
	public DateMidnight getBeginDate() {
		return beginDate;
	}
	
	@Override
	public DateMidnight getEndDate() {
		return endDate;
	}
	
	@Override
	public LocalTime getEndTime() {
		return endTime;
	}
	
	@Override
	public int getIntervalBetweenLessonStart() {
		return intervalBetweenLessonStart;
	}
	
	@Override
	public int getLessonDuration() {
		return lessonDuration;
	}
	
	@Override
	public String getLocation() {
		return location;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public double getPrice() {
		return price;
	}
	
	@Override
	public int getRepeatEveryWeek() {
		return repeatEveryWeek;
	}
	
	@Override
	public LocalTime getStartTime() {
		return startTime;
	}
	
	@Override
	public Teacher getTeacher() {
		return teacher;
	}
	
	@Override
	public List<WeekDay> getWeekDays() {
		return weekDays;
	}
	
	/**
	 * This method is used to convert from the list of weekdays, to a string that can be serialized
	 */
	@Persistent(column = "week_days")
	public String getPersistentWeekDays() {
		final StringBuilder sb = new StringBuilder();
		
		for (final WeekDay day : weekDays) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(day.getYodaWeekDay());
		}
		
		return sb.toString();
	}
	
	public void setPersistentWeekDays(final String persistentWeekDays) {
		weekDays = new ArrayList<WeekDay>();
		
		if (persistentWeekDays != null && persistentWeekDays.length() > 0) {
			weekDays = new ArrayList<WeekDay>();
			
			final StringTokenizer stringTokenizer = new StringTokenizer(persistentWeekDays, ",");
			while (stringTokenizer.hasMoreTokens()) {
				final String token = stringTokenizer.nextToken();
				weekDays.add(WeekDay.parse(token));
			}
		}
	}
	
	@Override
	public boolean hasDate(final DateTime dateTime) {
		return hasDate(dateTime.toDateMidnight());
	}
	
	@Override
	public boolean hasDate(final DateMidnight date) {
		boolean hasDate = false;
		
		// Check weekday
		if (hasWeekDay(date)) {
			// Check start/end date
			if (dateIntervalContains(date)) {
				// If the period has a start date also consider the
				// repeatEveryWeek factor
				if (beginDate != null && repeatEveryWeek > 1) {
					final int weeks = Weeks.weeksBetween(beginDate, date).getWeeks();
					if (weeks % repeatEveryWeek == 0) {
						hasDate = true;
					}
				} else {
					hasDate = true;
				}
			}
		}
		
		return hasDate;
	}
	
	@Override
	public boolean hasWeekDay(final DateMidnight weekDayDate) {
		final DateMidnight dateMidnight = weekDayDate;
		dateMidnight.getDayOfWeek();
		
		boolean hasWeekDay = false;
		for (final WeekDay weekDay : weekDays) {
			if (weekDay.getYodaWeekDay() == dateMidnight.getDayOfWeek()) {
				hasWeekDay = true;
				break;
			}
		}
		
		return hasWeekDay;
	}
	
	@Override
	public boolean inLesson(DateTime bookedTime, DateTime time) {
		boolean inLesson = false;
		
		// On the same date
		if (bookedTime.toDateMidnight().equals(time.toDateMidnight())) {
			bookedTime = DateUtils.resetDateTime(bookedTime, time);
			time = DateUtils.resetDateTime(time, time);
			
			final DateTime et = bookedTime.plusMinutes(lessonDuration);
			
			final Interval bookedInterval = new Interval(bookedTime, et);
			
			if (bookedInterval.contains(time) && bookedTime.equals(time) == false) {
				inLesson = true;
			}
		}
		
		return inLesson;
	}
	
	@Override
	public PeriodStatus getStatus() {
		return status;
	}
	
	@Override
	public boolean isTimeValid(final DateTime dateTime) {
		return isTimeValid(dateTime.toLocalTime());
	}
	
	@Override
	public boolean isTimeValid(final LocalTime time) {
		boolean timeValid = false;
		
		final Interval periodTimeInterval = new Interval(startTime.toDateTimeToday(), endTime.toDateTimeToday());
		
		if (periodTimeInterval.contains(time.toDateTimeToday())) {
			final int timeMinutes = new Duration(startTime.toDateTimeToday(), time.toDateTimeToday()).toPeriod(PeriodType.minutes()).getMinutes();
			
			if (timeMinutes % intervalBetweenLessonStart == 0) {
				timeValid = true;
			}
		}
		
		return timeValid;
	}
	
	@Override
	public boolean mayBook(final DateTime dateTime) {
		return mayBook(dateTime.toLocalTime());
	}
	
	@Override
	public boolean mayBook(LocalTime time) {
		boolean mayBook = false;
		
		if (isTimeValid(time)) {
			time = time.plusMinutes(lessonDuration);
			if (time.equals(endTime) || time.isBefore(endTime)) {
				mayBook = true;
			}
		}
		
		return mayBook;
	}
	
	@Override
	public void setStatus(final PeriodStatus status) {
		this.status = status;
	}
	
	@Override
	public void setBeginDate(final DateMidnight beginDate) {
		this.beginDate = beginDate;
	}
	
	@Override
	public void setEndDate(final DateMidnight endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public void setEndTime(final LocalTime endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public void setIntervalBetweenLessonStart(final int intervalBetweenLessonStart) {
		this.intervalBetweenLessonStart = intervalBetweenLessonStart;
	}
	
	@Override
	public void setLessonDuration(final int lessonDuration) {
		this.lessonDuration = lessonDuration;
	}
	
	@Override
	public void setLocation(final String location) {
		this.location = location;
	}
	
	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public void setPrice(final double price) {
		this.price = price;
	}
	
	@Override
	public void setRepeatEveryWeek(final int repeatEveryWeek) {
		this.repeatEveryWeek = repeatEveryWeek;
	}
	
	@Override
	public void setStartTime(final LocalTime startTime) {
		this.startTime = startTime;
	}
	
	@Override
	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}
	
	public void setWeekDays(final List<WeekDay> weekDays) {
		this.weekDays = weekDays;
	}
	
	@Override
	public boolean isValid() {
		boolean valid = false;
		
		if (name != null && name.length() > 0 && startTime != null && endTime != null && teacher != null && weekDays != null && weekDays.isEmpty() == false
				&& intervalBetweenLessonStart > 0 && lessonDuration > 0 && repeatEveryWeek > 0) {
			valid = true;
		}
		
		return valid;
	}
}
