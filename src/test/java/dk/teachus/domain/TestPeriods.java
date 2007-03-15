package dk.teachus.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.domain.impl.PeriodImpl;
import dk.teachus.domain.impl.PeriodsImpl;
import dk.teachus.domain.impl.PeriodImpl.WeekDay;
import junit.framework.TestCase;

public class TestPeriods extends TestCase {
	
	public void testGenerateDate_repeatEveryWeek() {
		DateMidnight date = new DateMidnight(2007, 3, 12);

		Period period = new PeriodImpl();
		period.setBeginDate(date.toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 15, 0, 0, 0).toDate());
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
			DateMidnight d = new DateMidnight(datePeriod.getDate());
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
			DateMidnight d = new DateMidnight(datePeriod.getDate());
			assertEquals(2007, d.getYear());
			assertEquals(3, d.getMonthOfYear());
			assertEquals(27, d.getDayOfMonth());
		}
	}
	
	public void testGenerateDate_repeatEveryWeek2() {
		DateMidnight date = new DateMidnight(2007, 2, 26);

		Period period = new PeriodImpl();
		period.setBeginDate(date.toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 16, 0, 0, 0).toDate());
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
		
		DateMidnight checkDate = new DateMidnight(dates.get(0).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(3, checkDate.getMonthOfYear());
		assertEquals(26, checkDate.getDayOfMonth());
		
		checkDate = new DateMidnight(dates.get(1).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(4, checkDate.getMonthOfYear());
		assertEquals(23, checkDate.getDayOfMonth());
		
		checkDate = new DateMidnight(dates.get(2).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(5, checkDate.getMonthOfYear());
		assertEquals(21, checkDate.getDayOfMonth());
		
		checkDate = new DateMidnight(dates.get(3).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(6, checkDate.getMonthOfYear());
		assertEquals(18, checkDate.getDayOfMonth());
		
		checkDate = new DateMidnight(dates.get(4).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(7, checkDate.getMonthOfYear());
		assertEquals(16, checkDate.getDayOfMonth());
		
		checkDate = new DateMidnight(dates.get(5).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(8, checkDate.getMonthOfYear());
		assertEquals(13, checkDate.getDayOfMonth());
		
		checkDate = new DateMidnight(dates.get(6).getDate());
		assertEquals(2007, checkDate.getYear());
		assertEquals(9, checkDate.getMonthOfYear());
		assertEquals(10, checkDate.getDayOfMonth());
	}
	
	public void testNumberOfWeeksBack() {
		DateMidnight date = new DateMidnight(2007, 2, 26);

		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = new PeriodImpl();
		period.setBeginDate(date.toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 16, 0, 0, 0).toDate());
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

		Period period = new PeriodImpl();
		period.setBeginDate(new DateMidnight(2007, 3, 1).toDate());
		period.setEndDate(new DateMidnight(2007, 5, 31).toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 16, 0, 0, 0).toDate());
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		period = new PeriodImpl();
		period.setBeginDate(new DateMidnight(2007, 8, 1).toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 16, 0, 0, 0).toDate());
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new DateMidnight(2007, 8, 20), 7);
		assertEquals(16, numberOfWeeks);
	}
	
	public void testNumberOfWeeksBack_twoNotOverlappingPeriods2() {
		Periods periods = new PeriodsImpl();
		List<Period> periodList = new ArrayList<Period>();
		periods.setPeriods(periodList);

		Period period = new PeriodImpl();
		period.setBeginDate(new DateMidnight(2007, 3, 1).toDate());
		period.setEndDate(new DateMidnight(2007, 3, 31).toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 16, 0, 0, 0).toDate());
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		period = new PeriodImpl();
		period.setBeginDate(new DateMidnight(2007, 5, 1).toDate());
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 16, 0, 0, 0).toDate());
		period.addWeekDay(WeekDay.MONDAY);
		periodList.add(period);
		
		int numberOfWeeks = periods.numberOfWeeksBack(new DateMidnight(2007, 5, 14), 7);
		assertEquals(11, numberOfWeeks);
	}
	
}