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

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.joda.time.DateMidnight;

import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PeriodsImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;

public class TestPeriods extends TestCase {
	
	public void testGenerateDate_repeatEveryWeek() {
		TeachUsDate date = new TeachUsDate(new DateMidnight(2007, 3, 12).toDateTime(), TimeZone.getDefault());

		Period period = createBasicPeriod();
		period.setBeginDate(date);
		period.setStartTime(date.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(10));
		period.setEndTime(date.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(15));
		period.addWeekDay(WeekDay.TUESDAY);
		period.setIntervalBetweenLessonStart(60);
		period.setLessonDuration(60);
		period.setRepeatEveryWeek(2);
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periodList.add(period);
		periods.setPeriods(periodList);

		List<DatePeriod> dates = periods.generateDatesForWeek(date);
		assertEquals(1, dates.size());
		for (DatePeriod datePeriod : dates) {
			TeachUsDate d = datePeriod.getDate();
			assertEquals(2007, d.getYear());
			assertEquals(3, d.getMonthOfYear());
			assertEquals(13, d.getDayOfMonth());
		}
		
		
		date = date.plusWeeks(1);
		dates = periods.generateDatesForWeek(date);
		assertEquals(0, dates.size());

		date = date.plusWeeks(1);
		dates = periods.generateDatesForWeek(date);
		assertEquals(1, dates.size());
		for (DatePeriod datePeriod : dates) {
			TeachUsDate d = datePeriod.getDate();
			assertEquals(2007, d.getYear());
			assertEquals(3, d.getMonthOfYear());
			assertEquals(27, d.getDayOfMonth());
		}
	}
	
	public void testGenerateDate_repeatEveryWeek2() {
		TeachUsDate date = new TeachUsDate(new DateMidnight(2007, 2, 26).toDateTime(), TimeZone.getDefault());

		Period period = createBasicPeriod();
		period.setBeginDate(date);
		period.setStartTime(date.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(10));
		period.setEndTime(date.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(16));
		period.addWeekDay(WeekDay.MONDAY);
		period.setIntervalBetweenLessonStart(60);
		period.setLessonDuration(60);
		period.setRepeatEveryWeek(4);
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periodList.add(period);
		periods.setPeriods(periodList);

		TeachUsDate weekDate = new TeachUsDate(2007, 3, 13, TimeZone.getDefault());
		List<DatePeriod> dates = periods.generateDates(weekDate, 7);
		
		assertEquals(7, dates.size());
		
		TeachUsDate checkDate = dates.get(0).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(3, checkDate.getMonthOfYear());
		assertEquals(26, checkDate.getDayOfMonth());
		
		checkDate = dates.get(1).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(4, checkDate.getMonthOfYear());
		assertEquals(23, checkDate.getDayOfMonth());
		
		checkDate = dates.get(2).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(5, checkDate.getMonthOfYear());
		assertEquals(21, checkDate.getDayOfMonth());
		
		checkDate = dates.get(3).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(6, checkDate.getMonthOfYear());
		assertEquals(18, checkDate.getDayOfMonth());
		
		checkDate = dates.get(4).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(7, checkDate.getMonthOfYear());
		assertEquals(16, checkDate.getDayOfMonth());
		
		checkDate = dates.get(5).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(8, checkDate.getMonthOfYear());
		assertEquals(13, checkDate.getDayOfMonth());
		
		checkDate = dates.get(6).getDate();
		assertEquals(2007, checkDate.getYear());
		assertEquals(9, checkDate.getMonthOfYear());
		assertEquals(10, checkDate.getDayOfMonth());
	}
	
	public void testGenerateDatesForWeek_oneDayPeriod() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new TeachUsDate(2007, 3, 24, timeZone));
		period.setEndDate(new TeachUsDate(2007, 3, 24, timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.SATURDAY);
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDatesForWeek(new TeachUsDate(2007, 3, 20, timeZone));
		assertEquals(1, dates.size());
	}
	
	public void testGenerateDates_oneDayPeriod() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new TeachUsDate(2007, 3, 24, timeZone));
		period.setEndDate(new TeachUsDate(2007, 3, 24, timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.SATURDAY);
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDates(new TeachUsDate(2007, 3, 20, timeZone), 7);
		assertEquals(1, dates.size());
	}
	
	public void testGenerateDates_noNumberOfDays() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);
		
		Period period = new PeriodImpl();
		period.setBeginDate(new TeachUsDate(2007, 3, 24, timeZone));
		period.setEndDate(new TeachUsDate(2007, 3, 24, timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.SATURDAY);
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDates(new TeachUsDate(2007, 3, 20, timeZone), 0);
		assertEquals(0, dates.size());
	}
	
	public void testGenerateDates_notCompletePeriod() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);
		
		Period period = new PeriodImpl();
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDates(new TeachUsDate(2007, 3, 20, timeZone), 7);
		assertEquals(0, dates.size());
	}
	
	public void testNumberOfWeeksBack() {
		TimeZone timeZone = TimeZone.getDefault();
		
		TeachUsDate date = new TeachUsDate(2007, 2, 26, timeZone);

		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(date);
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.MONDAY);
		period.setRepeatEveryWeek(4);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new TeachUsDate(2007, 9, 10, timeZone), 7);
		assertEquals(28, numberOfWeeks);
	}
	
	public void testNumberOfWeeksBack_twoNotOverlappingPeriods() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new TeachUsDate(2007, 3, 1, timeZone));
		period.setEndDate(new TeachUsDate(2007, 5, 31, timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);

		period = createBasicPeriod();
		period.setBeginDate(new TeachUsDate(2007, 8, 1,timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new TeachUsDate(2007, 8, 20, timeZone), 7);
		assertEquals(16, numberOfWeeks);
	}
	
	public void testNumberOfWeeksBack_twoNotOverlappingPeriods2() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new TeachUsDate(2007, 3, 1, timeZone));
		period.setEndDate(new TeachUsDate(2007, 3, 31, timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);

		period = createBasicPeriod();
		period.setBeginDate(new TeachUsDate(2007, 5, 1, timeZone));
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new TeachUsDate(2007, 5, 14, timeZone), 7);
		assertEquals(11, numberOfWeeks);
	}
	
	private Period createBasicPeriod() {
		Period period = new PeriodImpl();
		period.setName("Test Period");
		
		TeacherImpl teacher = new TeacherImpl();
		teacher.setId(2L);
		period.setTeacher(teacher);
		
		return period;
	}
	
}
