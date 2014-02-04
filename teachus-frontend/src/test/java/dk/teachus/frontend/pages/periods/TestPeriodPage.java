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
package dk.teachus.frontend.pages.periods;

import java.util.ArrayList;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.jmock.Expectations;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.BookingsImpl;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.frontend.test.WicketTestCase;

public class TestPeriodPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;
	
	public void testRenderNewPeriod() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(2L);
				will(Expectations.returnValue(createTeacher(2L)));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		final Period period = new PeriodImpl();
		period.setTeacher(createTeacher(2L));
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PeriodPage(period);
			}
		});
		
		tester.assertRenderedPage(PeriodPage.class);
	}
	
	public void testRenderExistingPeriod() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(2L);
				final Teacher teacher = createTeacher(2L);
				will(Expectations.returnValue(teacher));
				
				one(personDAO).getAttribute(teacher);
				will(Expectations.returnValue(new TeacherAttributeImpl()));
				
				tester.setPersonDAO(personDAO);
				
				final BookingDAO bookingDAO = createBookingDAO();
				
				exactly(1).of(bookingDAO).getBookings(teacher, new DateMidnight(2007, 1, 1), new DateMidnight(2007, 1, 7));
				will(Expectations.returnValue(new BookingsImpl(new ArrayList<Booking>())));
				
				tester.setBookingDAO(bookingDAO);
			}
		});
		
		final Period period = createPeriod();
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PeriodPage(period);
			}
		});
		
		tester.assertRenderedPage(PeriodPage.class);
		
		tester.assertContains(period.getName());
	}
	
	public void testFormSubmit() {
		final TeachUsWicketTester tester = createTester();
		final Period period = createPeriod();
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(2L);
				final Teacher teacher = createTeacher(2L);
				will(Expectations.returnValue(teacher));
				
				one(personDAO).getAttribute(teacher);
				will(Expectations.returnValue(new TeacherAttributeImpl()));
				
				tester.setPersonDAO(personDAO);
				
				final BookingDAO bookingDAO = createBookingDAO();
				
				one(bookingDAO).getLastBookingDate(period);
				will(Expectations.returnValue(new DateTime()));
				
				exactly(2).of(bookingDAO).getBookings(teacher, new DateMidnight(2007, 1, 1), new DateMidnight(2007, 1, 7));
				will(Expectations.returnValue(new BookingsImpl(new ArrayList<Booking>())));
				
				tester.setBookingDAO(bookingDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PeriodPage(period);
			}
		});
		
		final FormTester formTester = tester.newFormTester("form:form", false);
		formTester.setValue("elements:11:element:input:inputField", "");
		
		// Test that submitting an empty form works
		formTester.submit("elements:13:element:saveButton");
		
		// We should still be on the period page because of validation errors
		tester.assertRenderedPage(PeriodPage.class);
	}
	
}
