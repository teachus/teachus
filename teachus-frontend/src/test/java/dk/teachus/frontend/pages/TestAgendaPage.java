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
package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.jmock.Expectations;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.pages.persons.PupilPage;
import dk.teachus.frontend.test.WicketTestCase;

public class TestAgendaPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			TimeZone timeZone = TimeZone.getDefault();
			
			PersonDAO personDAO = createPersonDAO();
			BookingDAO bookingDAO = createBookingDAO();
			
			allowing(personDAO).getPerson(2L);
			will(returnValue(createTeacher(2L)));
			
			List<PupilBooking> bookings = new ArrayList<PupilBooking>();
			bookings.add(createPupilBooking(1L, new TeachUsDate(2007, 4, 20, 13, 0, 0, timeZone)));
			bookings.add(createPupilBooking(2L, new TeachUsDate(2007, 4, 20, 14, 0, 0, timeZone)));
			
			one(bookingDAO).getFutureBookingsForTeacher(with(aNonNull(Teacher.class)));
			will(returnValue(bookings));
			
			one(personDAO).getPerson(3L);
			will(returnValue(createPupil(3L)));
			
			tester.setPersonDAO(personDAO);
			tester.setBookingDAO(bookingDAO);
		}});
		
		tester.startPage(AgendaPage.class);
		
		tester.assertRenderedPage(AgendaPage.class);
		
		// Test clicking on one of the items
		tester.clickLink("list:filterForm:table:body:rows:1:cells:1:cell:link");
		
		tester.assertRenderedPage(PupilPage.class);
	}
	
}
