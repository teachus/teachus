package dk.teachus.domain;

import org.joda.time.DateTime;

import dk.teachus.domain.impl.PeriodImpl;
import junit.framework.TestCase;

public class TestPeriod extends TestCase {

	public void testIsTimeValid_simple() {
		Period period = new PeriodImpl();
		period.setStartTime(new DateTime(2006, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2008, 1, 1, 15, 0, 0, 0).toDate());
		period.setIntervalBetweenLessonStart(60);
		
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 1, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 1)));
		
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 15, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 9, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 12, 30, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 16, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 11, 1, 0, 0)));
	}
	
	public void testIsTimeValid_halfHour() {
		Period period = new PeriodImpl();
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 15, 0, 0, 0).toDate());
		period.setIntervalBetweenLessonStart(30);
		
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 1, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 1)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 30, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 30, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 30, 0, 0)));
		
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 15, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 9, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 16, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 11, 1, 0, 0)));
	}
	
	public void testIsTimeValid_15Min() {
		Period period = new PeriodImpl();
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 15, 0, 0, 0).toDate());
		period.setIntervalBetweenLessonStart(15);
		
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 0, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 1, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 1)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 30, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 30, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 30, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 11, 15, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 30, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 45, 0, 0)));
		
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 15, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 9, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 16, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 11, 1, 0, 0)));
	}
		
	public void testIsTimeValid_15Min_shiftedStartTime() {
		Period period = new PeriodImpl();
		period.setStartTime(new DateTime(2007, 1, 1, 10, 10, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 15, 0, 0, 0).toDate());
		period.setIntervalBetweenLessonStart(15);
		
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 10, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 10, 25, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 14, 55, 0, 0)));
		assertTrue(period.isTimeValid(new DateTime(2007, 1, 1, 12, 40, 0, 0)));
		
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 15, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 9, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 16, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 11, 1, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 10, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 11, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 14, 0, 0, 0)));
		assertFalse(period.isTimeValid(new DateTime(2007, 1, 1, 14, 15, 0, 0)));
	}

	public void testMayBook_hour() {
		Period period = new PeriodImpl();
		period.setStartTime(new DateTime(2007, 1, 1, 10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime(2007, 1, 1, 15, 0, 0, 0).toDate());
		period.setIntervalBetweenLessonStart(60);
		period.setLessonDuration(60);

		assertTrue(period.mayBook(new DateTime(2007, 1, 1, 10, 0, 0, 0)));
		assertTrue(period.mayBook(new DateTime(2007, 1, 1, 11, 0, 0, 0)));
		assertTrue(period.mayBook(new DateTime(2007, 1, 1, 14, 0, 0, 0)));
		assertTrue(period.mayBook(new DateTime(2007, 1, 1, 14, 0, 0, 1)));
		assertTrue(period.mayBook(new DateTime(2007, 1, 1, 11, 0, 1, 0)));
		assertTrue(period.mayBook(new DateTime(2007, 1, 1, 11, 0, 0, 1)));
		
		assertFalse(period.mayBook(new DateTime(2007, 1, 1, 15, 0, 0, 0)));
		assertFalse(period.mayBook(new DateTime(2007, 1, 1, 9, 0, 0, 0)));
		assertFalse(period.mayBook(new DateTime(2007, 1, 1, 12, 30, 0, 0)));
		assertFalse(period.mayBook(new DateTime(2007, 1, 1, 16, 0, 0, 0)));
		assertFalse(period.mayBook(new DateTime(2007, 1, 1, 11, 1, 0, 0)));		
	}
	
}
