package dk.frankbille.teachus.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import dk.frankbille.teachus.domain.Booking;
import dk.frankbille.teachus.domain.Bookings;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.TeacherBooking;

public interface BookingDAO extends Serializable {

	void book(Booking booking);
	
	PupilBooking createPupilBookingObject();

	TeacherBooking createTeacherBookingObject();

	void deleteBooking(Booking booking);

	/**
	 * 
	 * @param period
	 * @param date Only the date part of the date will be used in the search
	 * @return
	 */
	Bookings getBookingsForDate(Period period, Date date);

	List<PupilBooking> getFutureBookingsForTeacher(Teacher teacher);

	List<PupilBooking> getUnpaidBookings(Pupil pupil);

	List<PupilBooking> getUnpaidBookings(Teacher teacher);

	List<PupilBooking> getUnsentBookings(Teacher teacher);

	void newBookingsMailSent(List<PupilBooking> pupilBookings);

	void changePaidStatus(PupilBooking pupilBooking);

	List<PupilBooking> getPaidBookings(Teacher teacher, Date startDate, Date endDate);
	
}
