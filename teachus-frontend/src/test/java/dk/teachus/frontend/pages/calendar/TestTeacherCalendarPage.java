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
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.BookingsImpl;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.backend.domain.impl.PeriodsImpl;
import dk.teachus.backend.domain.impl.TeacherBookingImpl;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.test.WicketTestCase;

public class TestTeacherCalendarPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			
			one(personDAO).getPerson(2L);
			will(returnValue(createTeacher(2L)));
			
			one(periodDAO).getPeriods(with(a(Teacher.class)));
			will(returnValue(new PeriodsImpl()));
			
			tester.setPersonDAO(personDAO);
			tester.setPeriodDAO(periodDAO);
		}});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		tester.assertComponent("calendar", CalendarPanel.class);
		
		tester.assertContains(TeachUsSession.get().getPerson().getName());
	}
	
	public void testBookTeacher() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			Teacher teacher = createTeacher(2L);
			
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			BookingDAO bookingDAO = createBookingDAO();
			
			one(personDAO).getPerson(2L);
			will(returnValue(teacher));
			
			List<Period> periods = new ArrayList<Period>();
			Period period = createPeriod(1L);
			periods.add(period);
			PeriodsImpl periodsImpl = new PeriodsImpl();
			periodsImpl.setPeriods(periods);
			
			exactly(2).of(periodDAO).getPeriods(with(a(Teacher.class)));
			will(returnValue(periodsImpl));
			
			exactly(2).of(bookingDAO).getBookings(with(a(Teacher.class)), with(a(TeachUsDate.class)), with(a(TeachUsDate.class)));
			will(returnValue(new BookingsImpl(new ArrayList<Booking>())));
			
			one(bookingDAO).createTeacherBookingObject();
			will(returnValue(new TeacherBookingImpl()));
			
			one(personDAO).getAttributes(teacher);
			will(returnValue(new ArrayList<TeacherAttribute>()));
			
			one(bookingDAO).book(with(a(Booking.class)));
			
			tester.setBookingDAO(bookingDAO);
			tester.setPeriodDAO(periodDAO);
			tester.setPersonDAO(personDAO);
		}});
			
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage(new TeachUsDate(2007, 5, 9, TimeZone.getDefault()));
			}			
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		String timePath = "calendar:calendar:weeks:1:days:2:periods:1:period:rows:3";
		
		assertTimeNotSelected(tester, timePath);

		// Book time
		tester.clickLink(timePath+":contentContainer:content:link");
		
		// Show the page again to check that it is displaying
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage(new TeachUsDate(2007, 5, 9, TimeZone.getDefault()));
			}			
		});
		
		assertTimeSelected(tester, timePath);		
	}
	
	public void testNavigation() {
		final TimeZone timeZone = TimeZone.getDefault();
		
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			BookingDAO bookingDAO = createBookingDAO();
			
			one(personDAO).getPerson(2L);
			Teacher teacher = createTeacher(2L);
			will(returnValue(teacher));
			
			Periods periods = new PeriodsImpl();
			Period period = createPeriod();
			periods.addPeriod(period);
			exactly(2).of(periodDAO).getPeriods(with(a(Teacher.class)));
			will(returnValue(periods));
			
			one(bookingDAO).getBookings(teacher, new TeachUsDate(2007, 11, 5, timeZone), new TeachUsDate(2007, 11, 16, timeZone));
			will(returnValue(new BookingsImpl(new ArrayList<Booking>())));
			
			one(bookingDAO).getBookings(teacher, new TeachUsDate(2007, 10, 22, timeZone), new TeachUsDate(2007, 11, 2, timeZone));
			will(returnValue(new BookingsImpl(new ArrayList<Booking>())));
			
			tester.setPersonDAO(personDAO);
			tester.setPeriodDAO(periodDAO);
			tester.setBookingDAO(bookingDAO);
		}});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage(new TeachUsDate(2007, 11, 7, timeZone));
			}
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		tester.clickLink("calendar:calendar:backLink");

		tester.assertRenderedPage(TeacherCalendarPage.class);
	}
	
	public void testNavigation_fullWeek() {
		final TimeZone timeZone = TimeZone.getDefault();
		
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			PeriodDAO periodDAO = createPeriodDAO();
			BookingDAO bookingDAO = createBookingDAO();
			
			one(personDAO).getPerson(2L);
			Teacher teacher = createTeacher(2L);
			will(returnValue(teacher));
			
			Periods periods = new PeriodsImpl();
			Period period = createPeriod();
			period.addWeekDay(WeekDay.TUESDAY);
			period.addWeekDay(WeekDay.THURSDAY);
			period.addWeekDay(WeekDay.SATURDAY);
			period.addWeekDay(WeekDay.SUNDAY);
			periods.addPeriod(period);
			exactly(2).of(periodDAO).getPeriods(with(a(Teacher.class)));
			will(returnValue(periods));
			
			one(bookingDAO).getBookings(teacher, new TeachUsDate(2007, 11, 5, timeZone), new TeachUsDate(2007, 11, 11, timeZone));
			will(returnValue(new BookingsImpl(new ArrayList<Booking>())));
			
			one(bookingDAO).getBookings(teacher, new TeachUsDate(2007, 10, 29, timeZone), new TeachUsDate(2007, 11, 4, timeZone));
			will(returnValue(new BookingsImpl(new ArrayList<Booking>())));
			
			tester.setPersonDAO(personDAO);
			tester.setPeriodDAO(periodDAO);
			tester.setBookingDAO(bookingDAO);
		}});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage(new TeachUsDate(2007, 11, 7, timeZone));
			}
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		tester.clickLink("calendar:calendar:backLink");

		tester.assertRenderedPage(TeacherCalendarPage.class);
	}
}
