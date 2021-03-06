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
import java.util.Map;

import org.joda.time.DateTime;

import dk.teachus.backend.bean.NotificationBean;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.test.SpringTestCase;

public class TestVelocityNotificationBean extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testCreateMails() {
		NotificationBean notificationBean = (NotificationBean) applicationContext.getBean("notificationBean");
		
		// Count the number of mails before
		int firstMessageCount = countRowsInTable(TABLE_MESSAGE);
		
		// Execute the new bookings bean
		notificationBean.sendTeacherNotificationMail();
		
		// The number of messages shouldn't have changed
		int secondMessageCount = countRowsInTable(TABLE_MESSAGE);
		assertEquals(firstMessageCount, secondMessageCount);
		
		// Create a new booking
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3));
		
		// Execute the new bookings bean
		notificationBean.sendTeacherNotificationMail();

		// Now the message count should be one larger
		int thirdMessageCount = countRowsInTable(TABLE_MESSAGE);
		assertEquals(secondMessageCount+1, thirdMessageCount);
		
	}
	
	public void testSetSentTeacherNotificationFlag() {
		NotificationBean notificationBean = (NotificationBean) applicationContext.getBean("notificationBean");

		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3));
		
		List<PupilBooking> unsentBookingsBefore = getBookingDAO().getTeacherNotificationBookings(getTeacher());
		
		notificationBean.sendTeacherNotificationMail();
		
		List<PupilBooking> unsentBookingsAfter = getBookingDAO().getTeacherNotificationBookings(getTeacher());
		
		assertEquals(unsentBookingsBefore.size()-1, unsentBookingsAfter.size());
	}
	
	public void testCreatePupilNotificationMail() {

		NotificationBean notificationBean = (NotificationBean) applicationContext.getBean("notificationBean");
		
		// Count the number of mails before
		int firstMessageCount = countRowsInTable(TABLE_MESSAGE);
		
		// Execute the new bookings bean
		notificationBean.sendPupilNotificationMail();
		
		// The number of messages shouldn't have changed
		int secondMessageCount = countRowsInTable(TABLE_MESSAGE);
		assertEquals(firstMessageCount, secondMessageCount);
		
		// Create some bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3));
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 12, 0, 0, 0), new DateTime().minusHours(3));
		createPupilBooking(1L, 7L, new DateTime(2007, 3, 12, 13, 0, 0, 0), new DateTime().minusHours(3));
		createPupilBooking(1L, 8L, new DateTime(2007, 3, 12, 14, 0, 0, 0), new DateTime().minusHours(3));
				
		// Execute the new bookings bean
		notificationBean.sendPupilNotificationMail();

		// Now the message count should be one larger
		int thirdMessageCount = countRowsInTable(TABLE_MESSAGE);
		assertEquals(secondMessageCount+3, thirdMessageCount);
	}
	
	public void testSetSentPupilNotificationFlag() {
		NotificationBean notificationBean = (NotificationBean) applicationContext.getBean("notificationBean");

		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3));
		
		Map<Pupil, List<PupilBooking>> unsentBookingsBefore = getBookingDAO().getPupilNotificationBookings();
		
		notificationBean.sendPupilNotificationMail();
		
		Map<Pupil, List<PupilBooking>> unsentBookingsAfter = getBookingDAO().getPupilNotificationBookings();
		
		assertEquals(unsentBookingsBefore.size()-1, unsentBookingsAfter.size());
	}
	
}
