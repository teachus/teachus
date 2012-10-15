package dk.teachus.backend.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;

public interface BookingService {
	
	void book(Booking booking);
	
	PupilBooking createPupilBookingObject();
	
	TeacherBooking createTeacherBookingObject();
	
	void deleteBooking(Booking booking);
	
	List<PupilBooking> getFutureBookingsForTeacher(Teacher teacher);
	
	List<PupilBooking> getUnpaidBookings(Pupil pupil);
	
	List<PupilBooking> getUnpaidBookings(Teacher teacher);
	
	List<PupilBooking> getTeacherNotificationBookings(Teacher teacher);
	
	Map<Pupil, List<PupilBooking>> getPupilNotificationBookings();
	
	void teacherNotificationMailSent(List<PupilBooking> pupilBookings);
	
	void pupilNotificationMailSent(Map<Pupil, List<PupilBooking>> pupilBookings);
	
	void changePaidStatus(PupilBooking pupilBooking);
	
	List<PupilBooking> getPaidBookings(Teacher teacher, DateMidnight startDate, DateMidnight endDate);
	
	List<PupilBooking> getUnPaidBookings(Teacher teacher, DateMidnight fromDate, DateMidnight toDate);
	
	List<Integer> getYearsWithPaidBookings(Teacher teacher);
	
	List<Integer> getYearsWithBookings(Teacher teacher);
	
	Bookings getAllBookings(Teacher teacher);
	
	Bookings getBookings(Teacher teacher, DateMidnight fromDate, DateMidnight toDate);
	
	Bookings getBookings(Pupil pupil, DateMidnight fromDate, DateMidnight toDate);
	
	Booking getBooking(Long id);
	
	int getBookingCount(Period period);
	
	DateTime getLastBookingDate(Period period);
	
}
