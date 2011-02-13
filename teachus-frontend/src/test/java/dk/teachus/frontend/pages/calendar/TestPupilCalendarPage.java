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
import java.util.TimeZone;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.ITestPageSource;
import org.jmock.Expectations;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.BookingsImpl;
import dk.teachus.backend.domain.impl.PeriodsImpl;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.calendar.PupilPeriodsCalendarPanel;
import dk.teachus.frontend.test.WicketTestCase;

public class TestPupilCalendarPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender_teacher() {
		final TeachUsWicketTester tester = createTester();
		
		final Pupil pupil = createPupil(11L);
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			
			one(personDAO).getPerson(2L);
			will(returnValue(createTeacher(2L)));
			
			one(personDAO).getAttributes(with(a(Teacher.class)));
			will(returnValue(new ArrayList<TeacherAttribute>()));
			
			one(periodDAO).getPeriods(with(a(Teacher.class)), with(same(true)));
			will(returnValue(new PeriodsImpl()));
			
			tester.setPersonDAO(personDAO);
			tester.setPeriodDAO(periodDAO);
		}});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilCalendarPage(pupil);
			}			
		});
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.assertComponent("calendar", PupilPeriodsCalendarPanel.class);
		
		tester.assertContains(pupil.getName());
	}
	
	public void testRender_pupil() {
		// Log in as pupil
		final TeachUsWicketTester tester = createTester(11);
				
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			
			one(personDAO).getPerson(11L);
			will(returnValue(createPupil(11L)));
			
			exactly(1).of(personDAO).getAttributes(with(a(Teacher.class)));
			will(returnValue(new ArrayList<TeacherAttribute>()));
			
			one(periodDAO).getPeriods(with(a(Teacher.class)), with(same(true)));
			will(returnValue(new PeriodsImpl()));
			
			tester.setPersonDAO(personDAO);
			tester.setPeriodDAO(periodDAO);
		}});
		
		tester.startPage(PupilCalendarPage.class);
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.assertComponent("calendar", PupilPeriodsCalendarPanel.class);
		
		tester.assertContains(TeachUsSession.get().getPerson().getName());
	}
	
	public void testPupilBooked() {
		TimeZone timeZone = TimeZone.getDefault();
		
		final TeachUsWicketTester tester = createTester();
					
		final TeachUsDate dateTime = new TeachUsDate(2007, 6, 11, 11, 0, 0, timeZone);
		
		final Pupil pupil = createPupil(6L);
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			BookingDAO bookingDAO = createBookingDAO();
			
			one(personDAO).getPerson(2L);
			will(returnValue(createTeacher(2L)));
			
			List<Period> periods = new ArrayList<Period>();
			Period period = createPeriod(1L);
			periods.add(period);
			PeriodsImpl periodsImpl = new PeriodsImpl();
			periodsImpl.setPeriods(periods);
			
			one(periodDAO).getPeriods(with(a(Teacher.class)), with(same(true)));
			will(returnValue(periodsImpl));
			
			one(personDAO).getAttributes(with(a(Teacher.class)));
			will(returnValue(new ArrayList<TeacherAttribute>()));
			
			List<Booking> bookings = new ArrayList<Booking>();
			bookings.add(createPupilBooking(1L, pupil, period, dateTime));
			bookings.add(createPupilBooking(1L, createPupil(7L), period, dateTime.plusHours(2)));
			Bookings bookingsImpl = new BookingsImpl(bookings);
			
			one(bookingDAO).getBookings(with(a(Teacher.class)), with(a(TeachUsDate.class)), with(a(TeachUsDate.class)));
			will(returnValue(bookingsImpl));
			
			tester.setPersonDAO(personDAO);
			tester.setPeriodDAO(periodDAO);
			tester.setBookingDAO(bookingDAO);
		}});
		
		// Start the calendar
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilCalendarPage(dateTime, pupil);
			}			
		});
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		assertTimeOccupied(tester, "calendar:days:0:timeSlots:1");
	}
}
