package dk.teachus.dao.hibernate;

import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.WicketSpringTestCase;

public class TestBookingDAO extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testBook() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		Long periodId = 1L;

		DateTime date = new DateTime(2007, 6, 11, 11, 0, 0, 0);
		
		// First create a list of new bookings
		createPupilBooking(periodId, 6, date);
		
		Bookings bookings = bookingDAO.getBookings(teacher, date.toDateMidnight(), date.toDateMidnight());
		endTransaction();
				
		assertEquals(1, bookings.getBookingList().size());
		
		// Add a teacher booking on the date
		createTeacherBooking(periodId, 2, date.plusHours(1));
		
		bookings = bookingDAO.getBookings(teacher, date.toDateMidnight(), date.toDateMidnight());
		endTransaction();
				
		assertEquals(2, bookings.getBookingList().size());
	}
	
	public void testGetUnsentBookings() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		// First create a list of new bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0));
		
		List<PupilBooking> unsentBookings = bookingDAO.getUnsentBookings(teacher);
		endTransaction();
		
		assertEquals(1, unsentBookings.size());
	}
	
	public void testNewBookingsMailSent() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		// First create a list of new bookings
		createPupilBooking(1L, 6L, new DateTime(2007, 3, 12, 11, 0, 0, 0));
		
		List<PupilBooking> unsentBookings = bookingDAO.getUnsentBookings(teacher);
		endTransaction();
		
		assertEquals(1, unsentBookings.size());
		
		bookingDAO.newBookingsMailSent(unsentBookings);
		endTransaction();
		
		unsentBookings = bookingDAO.getUnsentBookings(teacher);
		endTransaction();
		
		assertEquals(0, unsentBookings.size());
	}
	
	public void testGetBookings() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		
		Teacher teacher = (Teacher) personDAO.getPerson(2L);
		endTransaction();
		
		// Create some bookings
		createPupilBooking(1, 6, new DateTime(2007, 6, 11, 11, 0, 0, 0));
		createPupilBooking(1, 6, new DateTime(2007, 6, 13, 11, 0, 0, 0));
		createPupilBooking(1, 6, new DateTime(2007, 6, 15, 11, 0, 0, 0));
		
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
	
}
