package dk.teachus.dao.hibernate;

import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.TeacherBooking;
import dk.teachus.frontend.WicketSpringTestCase;

public class TestBookingDAO extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testBook() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(1L);
		endTransaction();
		
		Pupil pupil = (Pupil) personDAO.getPerson(6L);
		endTransaction();		

		DateTime date = new DateTime(2007, 6, 11, 11, 0, 0, 0);
		
		// First create a list of new bookings
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPeriod(period);
		pupilBooking.setPupil(pupil);
		pupilBooking.setPaid(false);
		pupilBooking.setNotificationSent(false);
		pupilBooking.setCreateDate(new DateTime().minusHours(3).toDate());
		pupilBooking.setDate(date.toDate());
		
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		Bookings bookings = bookingDAO.getBookingsForDate(period, date.toDate());
		endTransaction();
				
		assertEquals(1, bookings.getBookingList().size());
		
		// Add a teacher booking on the date
		TeacherBooking teacherBooking = bookingDAO.createTeacherBookingObject();
		teacherBooking.setCreateDate(new DateTime().minusHours(3).toDate());
		teacherBooking.setDate(date.plusHours(1).toDate());
		teacherBooking.setPeriod(period);
		teacherBooking.setTeacher(pupil.getTeacher());
		
		bookingDAO.book(teacherBooking);
		endTransaction();
		
		bookings = bookingDAO.getBookingsForDate(period, date.toDate());
		endTransaction();
				
		assertEquals(2, bookings.getBookingList().size());
	}
	
	public void testGetUnsentBookings() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(1L);
		endTransaction();
		
		Pupil pupil = (Pupil) personDAO.getPerson(6L);
		endTransaction();
		
		// First create a list of new bookings
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPeriod(period);
		pupilBooking.setPupil(pupil);
		pupilBooking.setPaid(false);
		pupilBooking.setNotificationSent(false);
		pupilBooking.setCreateDate(new DateTime().minusHours(3).toDate());
		pupilBooking.setDate(new DateTime(2007, 3, 12, 11, 0, 0, 0).toDate());
		
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		List<PupilBooking> unsentBookings = bookingDAO.getUnsentBookings(pupil.getTeacher());
		endTransaction();
		
		assertEquals(1, unsentBookings.size());
	}
	
	public void testNewBookingsMailSent() {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(1L);
		endTransaction();
		
		Pupil pupil = (Pupil) personDAO.getPerson(6L);
		endTransaction();
		
		// First create a list of new bookings
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPeriod(period);
		pupilBooking.setPupil(pupil);
		pupilBooking.setPaid(false);
		pupilBooking.setNotificationSent(false);
		pupilBooking.setCreateDate(new DateTime().minusHours(3).toDate());
		pupilBooking.setDate(new DateTime(2007, 3, 12, 11, 0, 0, 0).toDate());
		
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		List<PupilBooking> unsentBookings = bookingDAO.getUnsentBookings(pupil.getTeacher());
		endTransaction();
		
		assertEquals(1, unsentBookings.size());
		
		bookingDAO.newBookingsMailSent(unsentBookings);
		endTransaction();
		
		unsentBookings = bookingDAO.getUnsentBookings(pupil.getTeacher());
		endTransaction();
		
		assertEquals(0, unsentBookings.size());
	}
	
}
