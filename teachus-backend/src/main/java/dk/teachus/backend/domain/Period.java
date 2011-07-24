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
package dk.teachus.backend.domain;

import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;

public interface Period {

	public static enum Status {
		DRAFT,
		FINAL,
		DELETED
	}
	
	Long getId();
	
	String getName();
	
	DateMidnight getEndDate();

	LocalTime getEndTime();

	DateMidnight getBeginDate();

	LocalTime getStartTime();

	List<WeekDay> getWeekDays();
	
	Teacher getTeacher();
	
	String getLocation();
	
	double getPrice();
	
	Status getStatus();
	
	/**
	 * @return The duration of a lesson in minutes
	 */
	int getLessonDuration();
	
	/**
	 * @return How often a lesson can start. F.ex. 15 on this property
	 * gives: 10.10, 10.15, 10.30 and 10.45
	 */
	int getIntervalBetweenLessonStart();
	
	int getRepeatEveryWeek();
	
	void setName(String name);

	void setEndDate(DateMidnight endDate);

	void setEndTime(LocalTime endTime);

	void setBeginDate(DateMidnight startDate);

	void setStartTime(LocalTime startTime);
	
	void setTeacher(Teacher teacher);
	
	void setLocation(String location);
	
	void setPrice(double price);
	
	void setLessonDuration(int minutes);
	
	void setIntervalBetweenLessonStart(int minutes);
	
	void setRepeatEveryWeek(int repeatEveryWeek);
	
	void setStatus(Status status);

	/*
	 * Utility methods
	 */
	void addWeekDay(WeekDay weekDay);

	boolean hasWeekDay(DateMidnight date);

	boolean dateIntervalContains(DateMidnight date);

	boolean hasDate(DateTime dateTime);
	
	boolean hasDate(DateMidnight date);

	DateMidnight generateDate(DateMidnight startDate);
	
	boolean isTimeValid(DateTime dateTime);
	
	boolean isTimeValid(LocalTime time);

	boolean mayBook(DateTime dateTime);
	
	boolean mayBook(LocalTime time);

	boolean conflicts(DateTime bookedTime, DateTime time);

	boolean inLesson(DateTime bookedTime, DateTime time);
	
	boolean isValid();
	
}