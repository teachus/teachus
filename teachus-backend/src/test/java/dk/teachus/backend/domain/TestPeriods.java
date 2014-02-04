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

import junit.framework.TestCase;

import org.joda.time.DateMidnight;
import org.joda.time.LocalTime;

import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PeriodsImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

public class TestPeriods extends TestCase {
	
	public void testGenerateDate_repeatEveryWeek() {
		DateMidnight date = new DateMidnight(2007, 3, 12);

		Period period = createBasicPeriod();
		period.setBeginDate(date);
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(15, 0, 0, 0));
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
			DateMidnight d = datePeriod.getDate();
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
			DateMidnight d = datePeriod.getDate();
			assertEquals(2007, d.getYear());
			assertEquals(3, d.getMonthOfYear());
			assertEquals(27, d.getDayOfMonth());
		}
	}
	
	public void testGenerateDate_repeatEveryWeek2() {
		DateMidnight date = new DateMidnight(2007, 2, 26);

		Period period = createBasicPeriod();
		period.setBeginDate(date);
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.MONDAY);
		period.setIntervalBetweenLessonStart(60);
		period.setLessonDuration(60);
		period.setRepeatEveryWeek(4);
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periodList.add(period);
		periods.setPeriods(periodList);

		DateMidnight weekDate = new DateMidnight(2007, 3, 13);
		List<DatePeriod> dates = periods.generateDates(weekDate, 7);
		
		assertEquals(7, dates.size());
		
		DateMidnight checkDate = dates.get(0).getDate();
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
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new DateMidnight(2007, 3, 24));
		period.setEndDate(new DateMidnight(2007, 3, 24));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.SATURDAY);
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDatesForWeek(new DateMidnight(2007, 3, 20));
		assertEquals(1, dates.size());
	}
	
	public void testGenerateDates_oneDayPeriod() {
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new DateMidnight(2007, 3, 24));
		period.setEndDate(new DateMidnight(2007, 3, 24));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.SATURDAY);
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDates(new DateMidnight(2007, 3, 20), 7);
		assertEquals(1, dates.size());
	}
	
	public void testGenerateDates_noNumberOfDays() {
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);
		
		Period period = new PeriodImpl();
		period.setBeginDate(new DateMidnight(2007, 3, 24));
		period.setEndDate(new DateMidnight(2007, 3, 24));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.SATURDAY);
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDates(new DateMidnight(2007, 3, 20), 0);
		assertEquals(0, dates.size());
	}
	
	public void testGenerateDates_notCompletePeriod() {
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);
		
		Period period = new PeriodImpl();
		periodList.add(period);
		
		List<DatePeriod> dates = periods.generateDates(new DateMidnight(2007, 3, 20), 7);
		assertEquals(0, dates.size());
	}
	
	public void testNumberOfWeeksBack() {
		DateMidnight date = new DateMidnight(2007, 2, 26);

		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(date);
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.MONDAY);
		period.setRepeatEveryWeek(4);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new DateMidnight(2007, 9, 10), 7);
		assertEquals(28, numberOfWeeks);
	}
	
	public void testNumberOfWeeksBack_twoNotOverlappingPeriods() {
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new DateMidnight(2007, 3, 1));
		period.setEndDate(new DateMidnight(2007, 5, 31));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);

		period = createBasicPeriod();
		period.setBeginDate(new DateMidnight(2007, 8, 1));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new DateMidnight(2007, 8, 20), 7);
		assertEquals(16, numberOfWeeks);
	}
	
	public void testNumberOfWeeksBack_twoNotOverlappingPeriods2() {
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = createBasicPeriod();
		period.setBeginDate(new DateMidnight(2007, 3, 1));
		period.setEndDate(new DateMidnight(2007, 3, 31));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);

		period = createBasicPeriod();
		period.setBeginDate(new DateMidnight(2007, 5, 1));
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(16, 0, 0, 0));
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new DateMidnight(2007, 5, 14), 7);
		assertEquals(11, numberOfWeeks);
	}
	
	public void testHasPeriodBefore() {
		Periods periods = new PeriodsImpl();
		Period period = new PeriodImpl();
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(18, 0, 0, 0));
		period.setName("Test with no begin date");
		period.addWeekDay(WeekDay.MONDAY);
		period.setStatus(PeriodStatus.FINAL);
		period.setTeacher(new TeacherImpl());
		periods.addPeriod(period);
		
		assertTrue(periods.hasPeriodBefore(new DateMidnight(2009, 5, 7)));
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
