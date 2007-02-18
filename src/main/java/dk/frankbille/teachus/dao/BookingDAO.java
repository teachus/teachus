package dk.frankbille.teachus.dao;

import java.io.Serializable;
import java.util.Date;

import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.PupilBookings;
import dk.frankbille.teachus.domain.Teacher;

public interface BookingDAO extends Serializable {

	void bookPupil(PupilBooking pupilBooking);
	
	PupilBooking createBookingObject();

	void deleteBooking(PupilBooking booking);

	/**
	 * 
	 * @param period
	 * @param date Only the date part of the date will be used in the search
	 * @return
	 */
	PupilBookings getBookingsForDate(Period period, Date date);

	PupilBookings getFutureBookingsForTeacher(Teacher teacher);

	PupilBookings getUnpaidBookings(Pupil pupil);

	PupilBookings getUnpaidBookings(Teacher teacher);
	
}
