package dk.teachus.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherBooking;

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
	
	List<Integer> getYearsWithPaidBookings(Teacher teacher);
	
}
