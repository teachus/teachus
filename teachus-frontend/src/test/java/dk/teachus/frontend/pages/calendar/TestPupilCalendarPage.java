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
package dk.teachus.frontend.pages.calendar;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.ITestPageSource;
import org.jmock.Expectations;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.BookingsImpl;
import dk.teachus.backend.domain.impl.PeriodsImpl;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.frontend.components.calendar.PupilPeriodsCalendarPanel;
import dk.teachus.frontend.test.WicketTestCase;

public class TestPupilCalendarPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;
	
	public void testRender_teacher() {
		final TeachUsWicketTester tester = createTester();
		
		final Pupil pupil = createPupil(11L);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				final PeriodDAO periodDAO = createPeriodDAO();
				
				one(personDAO).getPerson(2L);
				will(Expectations.returnValue(createTeacher(2L)));
				
				one(personDAO).getAttribute(with(Expectations.aNonNull(Teacher.class)));
				will(Expectations.returnValue(new TeacherAttributeImpl()));
				
				one(periodDAO).getPeriods(with(Expectations.aNonNull(Teacher.class)), with(Expectations.same(true)));
				will(Expectations.returnValue(new PeriodsImpl()));
				
				tester.setPersonDAO(personDAO);
				tester.setPeriodDAO(periodDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PupilCalendarPage(pupil);
			}
		});
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.assertComponent("calendar", PupilPeriodsCalendarPanel.class);
	}
	
	public void testRender_pupil() {
		// Log in as pupil
		final TeachUsWicketTester tester = createTester(11);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				final PeriodDAO periodDAO = createPeriodDAO();
				
				one(personDAO).getPerson(11L);
				will(Expectations.returnValue(createPupil(11L)));
				
				exactly(1).of(personDAO).getAttribute(with(Expectations.aNonNull(Teacher.class)));
				will(Expectations.returnValue(new TeacherAttributeImpl()));
				
				one(periodDAO).getPeriods(with(Expectations.aNonNull(Teacher.class)), with(Expectations.same(true)));
				will(Expectations.returnValue(new PeriodsImpl()));
				
				tester.setPersonDAO(personDAO);
				tester.setPeriodDAO(periodDAO);
			}
		});
		
		tester.startPage(PupilCalendarPage.class);
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.assertComponent("calendar", PupilPeriodsCalendarPanel.class);
	}
	
	public void testPupilBooked() {
		final TeachUsWicketTester tester = createTester();
		
		final DateTime dateTime = new DateTime(2007, 6, 11, 11, 0, 0, 0);
		
		final Pupil pupil = createPupil(6L);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				final PeriodDAO periodDAO = createPeriodDAO();
				final BookingDAO bookingDAO = createBookingDAO();
				
				one(personDAO).getPerson(2L);
				will(Expectations.returnValue(createTeacher(2L)));
				
				final List<Period> periods = new ArrayList<Period>();
				final Period period = createPeriod(1L);
				periods.add(period);
				final PeriodsImpl periodsImpl = new PeriodsImpl();
				periodsImpl.setPeriods(periods);
				
				one(periodDAO).getPeriods(with(Expectations.aNonNull(Teacher.class)), with(Expectations.same(true)));
				will(Expectations.returnValue(periodsImpl));
				
				one(personDAO).getAttribute(with(Expectations.aNonNull(Teacher.class)));
				will(Expectations.returnValue(new TeacherAttributeImpl()));
				
				final List<Booking> bookings = new ArrayList<Booking>();
				bookings.add(createPupilBooking(1L, pupil, period, dateTime));
				bookings.add(createPupilBooking(1L, createPupil(7L), period, dateTime.plusHours(2)));
				final Bookings bookingsImpl = new BookingsImpl(bookings);
				
				one(bookingDAO).getBookings(with(Expectations.aNonNull(Teacher.class)), with(Expectations.aNonNull(DateMidnight.class)),
						with(Expectations.aNonNull(DateMidnight.class)));
				will(Expectations.returnValue(bookingsImpl));
				
				tester.setPersonDAO(personDAO);
				tester.setPeriodDAO(periodDAO);
				tester.setBookingDAO(bookingDAO);
			}
		});
		
		// Start the calendar
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PupilCalendarPage(dateTime.toDateMidnight(), pupil);
			}
		});
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		assertTimeOccupied(tester, "calendar:days:0:timeSlots:1");
	}
}
