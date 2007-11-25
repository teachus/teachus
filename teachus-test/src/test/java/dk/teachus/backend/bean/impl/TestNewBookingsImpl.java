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
package dk.teachus.backend.bean.impl;

import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.backend.bean.NewBookings;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.test.SpringTestCase;

public class TestNewBookingsImpl extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testCreateMails() {
		NewBookings newBookings = (NewBookings) applicationContext.getBean("newBookings");
		
		// Count the number of mails before
		int firstMessageCount = countRowsInTable(TABLE_MESSAGE);
		int firstMessageRecCount = countRowsInTable(TABLE_MESSAGE_RECIPIENT);
		
		// Execute the new bookings bean
		newBookings.sendNewBookingsMail();
		
		// The number of messages shouldn't have changed
		int secondMessageCount = countRowsInTable(TABLE_MESSAGE);
		int secondMessageRecCount = countRowsInTable(TABLE_MESSAGE_RECIPIENT);
		assertEquals(firstMessageCount, secondMessageCount);
		assertEquals(firstMessageRecCount, secondMessageRecCount);
		
		// Create a new booking
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		// Execute the new bookings bean
		newBookings.sendNewBookingsMail();

		// Now the message count should be one larger
		int thirdMessageCount = countRowsInTable(TABLE_MESSAGE);
		int thirdMessageRecCount = countRowsInTable(TABLE_MESSAGE_RECIPIENT);
		assertEquals(secondMessageCount+1, thirdMessageCount);
		assertEquals(secondMessageRecCount+1, thirdMessageRecCount);
		
	}
	
	public void testSetSentFlag() {
		NewBookings newBookings = (NewBookings) applicationContext.getBean("newBookings");

		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		List<PupilBooking> unsentBookingsBefore = getBookingDAO().getUnsentBookings(getTeacher());
		
		newBookings.sendNewBookingsMail();
		
		List<PupilBooking> unsentBookingsAfter = getBookingDAO().getUnsentBookings(getTeacher());
		
		assertEquals(unsentBookingsBefore.size()-1, unsentBookingsAfter.size());
	}
	
}
