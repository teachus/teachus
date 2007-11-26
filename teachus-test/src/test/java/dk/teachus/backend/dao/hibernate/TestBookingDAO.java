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
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.test.SpringTestCase;

public class TestBookingDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testBook() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		Long periodId = 1L;

		DateTime date = new DateTime(2007, 6, 11, 11, 0, 0, 0);
		
		// First create a list of new bookings
		createPupilBooking(periodId, 6, date, new DateTime().minusHours(3).toDate());
		
		Bookings bookings = bookingDAO.getBookings(teacher, date.toDateMidnight(), date.toDateMidnight());
		endTransaction();
				
		assertEquals(1, bookings.getBookingList().size());
		
		// Add a teacher booking on the date
		createTeacherBooking(periodId, 2, date.plusHours(1));
		
		bookings = bookingDAO.getBookings(teacher, date.toDateMidnight(), date.toDateMidnight());
		endTransaction();
				
		assertEquals(2, bookings.getBookingList().size());
	}
	
	public void testBook_createDate() {
		Long bookingId = createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), null);
		
		Booking booking = getBookingDAO().getBooking(bookingId);
		endTransaction();
		
		assertNotNull(booking.getCreateDate());
	}
	
	public void testGetUnsentBookings() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		// First create a list of new bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		List<PupilBooking> unsentBookings = bookingDAO.getTeacherNotificationBookings(teacher);
		endTransaction();
		
		assertEquals(1, unsentBookings.size());
	}
	
	public void testGetPupilNotificationBookings() {
		BookingDAO bookingDAO = getBookingDAO();
		endTransaction();
		
		// First create a list of new bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 12, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 7L, new DateTime(2007, 3, 12, 13, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 8L, new DateTime(2007, 3, 12, 14, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		Map<Pupil, List<PupilBooking>> unsentBookings = bookingDAO.getPupilNotificationBookings();
		endTransaction();
		
		// Number of different pupils
		assertEquals(3, unsentBookings.size());
		
		// Number of bookings in for each pupil
		assertEquals(2, getPupilBookings(unsentBookings, 6).size());
		assertEquals(1, getPupilBookings(unsentBookings, 7).size());
		assertEquals(1, getPupilBookings(unsentBookings, 8).size());
	}
	
	private Pupil getPupil(Map<Pupil, List<PupilBooking>> unsentBookings, long pupilId) {
		Pupil pupil = null;
		
		for (Pupil p : unsentBookings.keySet()) {
			if (new Long(pupilId).equals(p.getId())) {
				pupil = p;
				break;
			}
		}
		
		return pupil;
	}
	
	private List<PupilBooking> getPupilBookings(Map<Pupil, List<PupilBooking>> unsentBookings, long pupilId) {
		List<PupilBooking> pupilBookings = null;
		
		Pupil pupil = getPupil(unsentBookings, pupilId);
		
		if (pupil != null) {
			pupilBookings = unsentBookings.get(pupil);
		}
		
		return pupilBookings;
	}
	
	public void testNewBookingsMailSent() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		// First create a list of new bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		List<PupilBooking> unsentBookings = bookingDAO.getTeacherNotificationBookings(teacher);
		endTransaction();
		
		assertEquals(1, unsentBookings.size());
		
		bookingDAO.teacherNotificationMailSent(unsentBookings);
		endTransaction();
		
		List<PupilBooking> unsentBookings2 = bookingDAO.getTeacherNotificationBookings(teacher);
		endTransaction();
		
		assertEquals(0, unsentBookings2.size());
		
		// Get the bookings to test that the update date is set
		for (PupilBooking booking : unsentBookings) {
			Booking checkBooking = bookingDAO.getBooking(booking.getId());
			assertNotNull(checkBooking.getUpdateDate());
		}
	}
	
	public void testPupilNotificationMailSent() {
		BookingDAO bookingDAO = getBookingDAO();
		endTransaction();
		
		// First create a list of new bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 12, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 7L, new DateTime(2007, 3, 12, 13, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 8L, new DateTime(2007, 3, 12, 14, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		Map<Pupil, List<PupilBooking>> pupilNotificationBookings = bookingDAO.getPupilNotificationBookings();
		endTransaction();
		
		assertEquals(3, pupilNotificationBookings.size());
		
		// Lets remove one of them
		Pupil pupil = getPupil(pupilNotificationBookings, 7);
		pupilNotificationBookings.remove(pupil);
		
		// Now lets update
		bookingDAO.pupilNotificationMailSent(pupilNotificationBookings);
		endTransaction();
		
		pupilNotificationBookings = bookingDAO.getPupilNotificationBookings();
		endTransaction();
		
		assertEquals(1, pupilNotificationBookings.size());
		
		assertNotNull(getPupil(pupilNotificationBookings, 7));
	}
	
	public void testGetBookings() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		// Create some bookings
		createPupilBooking(1, 6, new DateTime(2007, 6, 11, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1, 6, new DateTime(2007, 6, 13, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1, 6, new DateTime(2007, 6, 15, 11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		createTeacherBooking(1, 2, new DateTime(2007, 6, 11, 12, 0, 0, 0));
		createTeacherBooking(1, 2, new DateTime(2007, 6, 13, 12, 0, 0, 0));
		createTeacherBooking(1, 2, new DateTime(2007, 6, 15, 12, 0, 0, 0));
		
		DateMidnight fromDate = new DateMidnight(2007, 6, 11);
		DateMidnight toDate = new DateMidnight(2007, 6, 17);
		Bookings bookings = bookingDAO.getBookings(teacher, fromDate, toDate);
		endTransaction();
		
		assertNotNull(bookings);
		
		assertEquals(6, bookings.getBookingList().size());
		
		for (Booking booking : bookings.getBookingList()) {
			DateTime date = new DateTime(booking.getDate());
			assertTrue(fromDate.isBefore(date));
			assertTrue(toDate.isAfter(date));
			
			assertEquals(new Long(1), booking.getPeriod().getId());
		}
	}
	
	public void testDeleteBooking() {
		Pupil pupil = (Pupil) getPersonDAO().getPerson(13L);
		endTransaction();
		
		List<PupilBooking> unpaidBookings = getBookingDAO().getUnpaidBookings(pupil);
		endTransaction();
		
		int bookingsBefore = unpaidBookings.size();
		
		assertTrue(bookingsBefore > 0);
		
		getBookingDAO().deleteBooking(unpaidBookings.get(0));
		endTransaction();
		
		unpaidBookings = getBookingDAO().getUnpaidBookings(pupil);
		endTransaction();
		
		int bookingsAfter = unpaidBookings.size();
		
		assertEquals(bookingsBefore-1, bookingsAfter);
	}
	
	public void testGetFutureBookingsForTeacher_inactiveTeacher() {
		// Add some future bookings
		createPupilBooking(1L, 6L, new DateTime().plusMonths(1).withDayOfWeek(DateTimeConstants.MONDAY).withTime(11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 6L, new DateTime().plusMonths(1).withDayOfWeek(DateTimeConstants.WEDNESDAY).withTime(11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 6L, new DateTime().plusMonths(1).withDayOfWeek(DateTimeConstants.FRIDAY).withTime(11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		Teacher teacher = inactivateTeacher();
		
		List<PupilBooking> futureBookingsForTeacher = getBookingDAO().getFutureBookingsForTeacher(teacher);
		endTransaction();
		
		assertEquals(0, futureBookingsForTeacher.size());
	}
	
	public void testGetUnpaidBookings_pupil_inactiveTeacher() {
		Pupil pupil = (Pupil) getPersonDAO().getPerson(13L);
		endTransaction();
		
		List<PupilBooking> unpaidBookings = getBookingDAO().getUnpaidBookings(pupil);
		endTransaction();
		
		assertEquals(3, unpaidBookings.size());
		
		// Inactivate teacher
		inactivateTeacher();
		
		unpaidBookings = getBookingDAO().getUnpaidBookings(pupil);
		endTransaction();
		
		assertEquals(0, unpaidBookings.size());
	}
	
	public void testGetUnpaidBookings_teacher_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		List<PupilBooking> unpaidBookings = getBookingDAO().getUnpaidBookings(teacher);
		endTransaction();
		
		assertEquals(6, unpaidBookings.size());
		
		// Inactivate teacher
		teacher = inactivateTeacher();
		
		unpaidBookings = getBookingDAO().getUnpaidBookings(teacher);
		endTransaction();
		
		assertEquals(0, unpaidBookings.size());
	}
	
	public void testGetUnsentBookings_inactiveTeacher() {
		// add some unsent bookings
		createPupilBooking(1L, 6L, new DateTime().plusMonths(1).withDayOfWeek(DateTimeConstants.MONDAY).withTime(11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 6L, new DateTime().plusMonths(1).withDayOfWeek(DateTimeConstants.WEDNESDAY).withTime(11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		createPupilBooking(1L, 6L, new DateTime().plusMonths(1).withDayOfWeek(DateTimeConstants.FRIDAY).withTime(11, 0, 0, 0), new DateTime().minusHours(3).toDate());
		
		Teacher teacher = getTeacher();
		
		List<PupilBooking> unsentBookings = getBookingDAO().getTeacherNotificationBookings(teacher);
		endTransaction();
		
		assertEquals(3, unsentBookings.size());
		
		// Inactive teacher
		teacher = inactivateTeacher();
		
		unsentBookings = getBookingDAO().getTeacherNotificationBookings(teacher);
		endTransaction();
		
		assertEquals(0, unsentBookings.size());
	}
	
	public void testGetPaidBookings_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		Date startDate = new DateMidnight(2007, 1, 1).toDate();
		Date endDate = new DateMidnight(2007, 12, 31).toDate();
		
		List<PupilBooking> paidBookings = getBookingDAO().getPaidBookings(teacher, startDate, endDate);
		endTransaction();
		
		assertEquals(12, paidBookings.size());
		
		// Inactive teacher
		teacher = inactivateTeacher();
		
		paidBookings = getBookingDAO().getPaidBookings(teacher, startDate, endDate);
		endTransaction();
		
		assertEquals(0, paidBookings.size());
	}
	
	public void testGetUnPaidBookings_dateRange_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		Date startDate = new DateMidnight(2007, 3, 1).toDate();
		Date endDate = new DateMidnight(2007, 12, 31).toDate();
		
		List<PupilBooking> unpaidBookings = getBookingDAO().getUnPaidBookings(teacher, startDate, endDate);
		endTransaction();
		
		assertEquals(4, unpaidBookings.size());
		
		// Inactivate teacher
		teacher = inactivateTeacher();
		
		unpaidBookings = getBookingDAO().getUnPaidBookings(teacher, startDate, endDate);
		endTransaction();
		
		assertEquals(0, unpaidBookings.size());
	}
	
	public void testGetYearsWithPaidBookings_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		List<Integer> yearsWithPaidBookings = getBookingDAO().getYearsWithPaidBookings(teacher);
		endTransaction();
		
		assertEquals(2, yearsWithPaidBookings.size());

		// Inactivate teacher
		teacher = inactivateTeacher();
		
		yearsWithPaidBookings = getBookingDAO().getYearsWithPaidBookings(teacher);
		endTransaction();
		
		assertEquals(0, yearsWithPaidBookings.size());
	}
	
	public void testGetYearsWithBookings_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		List<Integer> yearsWithBookings = getBookingDAO().getYearsWithBookings(teacher);
		endTransaction();
		
		assertEquals(2, yearsWithBookings.size());

		// Inactivate teacher
		teacher = inactivateTeacher();
		
		yearsWithBookings = getBookingDAO().getYearsWithBookings(teacher);
		endTransaction();
		
		assertEquals(0, yearsWithBookings.size());
	}
	
	public void testGetBookings_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		DateMidnight fromDate = new DateMidnight(2007, 1, 1);
		DateMidnight toDate = new DateMidnight(2007, 12, 31);
		
		// Add some teacher bookings
		createTeacherBooking(1L, teacher.getId(), new DateTime(2007, 5, 9, 11, 0, 0, 0));
		
		Bookings bookings = getBookingDAO().getBookings(teacher, fromDate, toDate);
		endTransaction();
		
		assertEquals(19, bookings.getBookingList().size());
		
		// Inactivate teacher
		teacher = inactivateTeacher();
		
		bookings = getBookingDAO().getBookings(teacher, fromDate, toDate);
		endTransaction();
		
		assertEquals(0, bookings.getBookingList().size());
	}
	
	/**
	 * Please note that we *do* allow to get the booking
	 * even if the teacher is inactive.
	 */
	public void testGetBooking_inactiveTeacher() {
		Booking booking = getBookingDAO().getBooking(100L);
		endTransaction();
		
		assertNotNull(booking);

		// Inactivate teacher
		inactivateTeacher();
		
		booking = getBookingDAO().getBooking(100L);
		endTransaction();
		
		assertNotNull(booking);
	}
	
}
