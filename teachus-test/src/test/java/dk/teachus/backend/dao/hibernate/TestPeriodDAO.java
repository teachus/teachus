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
package dk.teachus.backend.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.backend.test.SpringTestCase;

public class TestPeriodDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testSave() {
		TimeZone timeZone = TimeZone.getDefault();
		
		// Get teacher
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		Period period = new PeriodImpl();
		period.setStatus(Status.FINAL);
		period.setBeginDate(new TeachUsDate(new DateMidnight(), timeZone).minusMonths(4));
		period.setEndDate(new TeachUsDate(new DateMidnight(), timeZone).plusMonths(6));
		period.setStartTime(new TeachUsDate(new DateTime(), timeZone).withTime(10, 0, 0, 0));
		period.setEndTime(new TeachUsDate(new DateTime(), timeZone).withTime(17, 0, 0, 0));
		period.setLocation("Home");
		period.setName("Tue / Thu @ home");
		period.addWeekDay(WeekDay.TUESDAY);
		period.addWeekDay(WeekDay.THURSDAY);
		period.setPrice(300);
		period.setTeacher(teacher);
		
		getPeriodDAO().save(period);
		endTransaction();
		
		Period period2 = getPeriodDAO().get(period.getId());
		endTransaction();
		
		assertNotNull(period2);
	}
	
	public void testGetPeriods() {
		TimeZone timeZone = TimeZone.getDefault();
		
		// Add a new teacher
		Teacher teacher = new TeacherImpl();
		teacher.setActive(true);
		teacher.setCurrency("kr");
		teacher.setEmail("t2@teachus.dk");
		teacher.setName("T2");
		teacher.setUsername("t2t2");
		getPersonDAO().save(teacher);
		endTransaction();
		
		// Add some periods
		Period period = new PeriodImpl();
		period.setStatus(Status.FINAL);
		period.setBeginDate(new TeachUsDate(new DateMidnight(), timeZone).minusMonths(4));
		period.setEndDate(new TeachUsDate(new DateMidnight(), timeZone).plusMonths(6));
		period.setStartTime(new TeachUsDate(new DateTime(), timeZone).withTime(10, 0, 0, 0));
		period.setEndTime(new TeachUsDate(new DateTime(), timeZone).withTime(17, 0, 0, 0));
		period.setLocation("Home");
		period.setName("Tue / Thu @ home");
		period.addWeekDay(WeekDay.TUESDAY);
		period.addWeekDay(WeekDay.THURSDAY);
		period.setPrice(300);
		period.setTeacher(teacher);
		
		getPeriodDAO().save(period);
		endTransaction();
		
		// Now load the periods from teacher 2L
		Teacher teacher2 = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		Periods periods = getPeriodDAO().getPeriods(teacher2);
		endTransaction();
		
		List<Period> periods2 = periods.getPeriods();
		assertEquals(3, periods2.size());
		for (Period period2 : periods2) {
			// We shouldn't have loaded the period which is associated with teacher 1
			assertFalse(period2.getId().equals(period.getId()));
		}
	}
	
	public void testGetPeriods_onlyActive() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		Periods periods = getPeriodDAO().getPeriods(teacher);
		endTransaction();
		
		int periodsBefore = periods.getPeriods().size();
		
		// Add a period and delete it
		Period period = new PeriodImpl();
		period.setName("Empty period");
		period.setStartTime(new TeachUsDate(new DateTime(), timeZone).withTime(10, 0, 0, 0));
		period.setEndTime(new TeachUsDate(new DateTime(), timeZone).withTime(18, 0, 0, 0));
		period.setEndDate(new TeachUsDate(2005, 12, 15, timeZone));
		period.setLocation("Private");
		period.setPrice(300);
		period.setStatus(Status.FINAL);
		period.setTeacher(getTeacher());
		period.addWeekDay(WeekDay.MONDAY);
		getPeriodDAO().save(period);
		endTransaction();
		
		getPeriodDAO().delete(period);
		endTransaction();
		
		// Add a period which is draft		
		period = getPeriodDAO().createPeriodObject();
		period.setStatus(Status.DRAFT);
		period.setTeacher(teacher);
		getPeriodDAO().save(period);
		endTransaction();
		
		// Get the periods again
		periods = getPeriodDAO().getPeriods(teacher);
		endTransaction();
		
		int periodsAfter = periods.getPeriods().size();
		
		assertEquals(periodsBefore, periodsAfter);
	}
	
	public void testGetPeriods_bothDraftAndActive() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		Periods periods = getPeriodDAO().getPeriods(teacher, false);
		endTransaction();
		
		int periodsBefore = periods.getPeriods().size();
		
		// Add a period which is draft		
		Period period = getPeriodDAO().createPeriodObject();
		period.setStatus(Status.DRAFT);
		period.setTeacher(teacher);
		getPeriodDAO().save(period);
		endTransaction();

		periods = getPeriodDAO().getPeriods(teacher, false);
		endTransaction();
		
		int periodsAfter = periods.getPeriods().size();
	
		assertEquals(periodsBefore+1, periodsAfter);
	}

	public void testGetPeriods_onlyActiveTeacher() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		teacher.setActive(false);
		
		getPersonDAO().save(teacher);
		endTransaction();
		
		Periods periods = getPeriodDAO().getPeriods(teacher);
		endTransaction();
		
		assertEquals(0, periods.getPeriods().size());
	}
	
	public void testGetPeriod_onlyActiveTeacher() {
		// Test that the period can be loaded
		Period period = getPeriodDAO().get(3L);
		endTransaction();
		
		assertNotNull(period);
		
		// Inactivate teacher
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		teacher.setActive(false);
		
		getPersonDAO().save(teacher);
		endTransaction();
		
		// Load period again
		period = getPeriodDAO().get(3L);
		endTransaction();
		
		assertNull(period);
	}

	/**
	 *  It's not possible to delete a period with active bookings
	 */
	public void testDeletePeriod_withActiveBookings() {
		// Get period with active bookings
		Period period = getPeriodDAO().get(1L);
		endTransaction();
		
		try {
			getPeriodDAO().delete(period);
			fail("Didn't throw an exception");
		} catch (IllegalArgumentException e) {
			// OK, expected
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}
	}
	
	public void testDeletePeriod_noActiveBookings() {
		TimeZone timeZone = TimeZone.getDefault();
		
		// Create period which has a deleted booking
		Period period = new PeriodImpl();
		period.setName("Empty period with deleted booking");
		period.setStartTime(new TeachUsDate(new DateTime(), timeZone).withTime(10, 0, 0, 0));
		period.setEndTime(new TeachUsDate(new DateTime(), timeZone).withTime(18, 0, 0, 0));
		period.setEndDate(new TeachUsDate(2005, 12, 15, timeZone));
		period.setLocation("Private");
		period.setPrice(300);
		period.setStatus(Status.FINAL);
		period.setTeacher(getTeacher());
		period.addWeekDay(WeekDay.TUESDAY);
		getPeriodDAO().save(period);
		endTransaction();
		
		Long booking = createPupilBooking(period.getId(), 3, new DateTime(2005, 10, 10, 11, 0, 0, 0).withDayOfWeek(DateTimeConstants.TUESDAY), new Date());
		getBookingDAO().deleteBooking(getBookingDAO().getBooking(booking));
		endTransaction();
		
		// Try to delete the period. It should be possible
		getPeriodDAO().delete(period);
		endTransaction();
		
		period = getPeriodDAO().get(period.getId());
		endTransaction();
		
		assertEquals(Status.DELETED, period.getStatus());
	}
	
}
