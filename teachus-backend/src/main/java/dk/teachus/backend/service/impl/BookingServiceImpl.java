package dk.teachus.backend.service.impl;

import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.backend.domain.impl.PupilBookingImpl;
import dk.teachus.backend.domain.impl.TeacherBookingImpl;
import dk.teachus.backend.service.BookingService;

@Transactional(propagation = Propagation.REQUIRED)
public class BookingServiceImpl implements BookingService {
	
	private final BookingDAO bookingDAO;
	
	public BookingServiceImpl(final BookingDAO bookingDAO) {
		this.bookingDAO = bookingDAO;
	}
	
	@Override
	public void book(final Booking booking) {
		// Validate the booking
		final Period period = booking.getPeriod();
		final DateTime date = booking.getDate();
		
		if (period.getStatus() != Status.FINAL) {
			throw new IllegalArgumentException("Can only book in active periods");
		}
		
		if (period.hasDate(date) == false) {
			throw new IllegalArgumentException("The period can not be booked on this date");
		}
		
		if (booking instanceof PupilBooking) {
			final PupilBooking pupilBooking = (PupilBooking) booking;
			if (pupilBooking.getPupil().isActive() == false) {
				throw new IllegalArgumentException("Can only book for active pupils");
			}
			
			if (pupilBooking.getCreateDate() == null) {
				throw new IllegalArgumentException("Pupil bookings must have a create date");
			}
			
			// Ensure that the teacher property is set
			if (pupilBooking.getTeacher() == null) {
				pupilBooking.setTeacher(pupilBooking.getPupil().getTeacher());
			}
		}
		
		bookingDAO.saveBooking(booking);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PupilBooking createPupilBookingObject() {
		return new PupilBookingImpl();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public TeacherBooking createTeacherBookingObject() {
		return new TeacherBookingImpl();
	}
	
	@Override
	public void deleteBooking(final Booking booking) {
		bookingDAO.deleteBooking(booking);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<PupilBooking> getFutureBookingsForTeacher(final Teacher teacher) {
		return bookingDAO.getFutureBookingsForTeacher(teacher);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<PupilBooking> getUnpaidBookings(final Pupil pupil) {
		return bookingDAO.getUnpaidBookings(pupil);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<PupilBooking> getUnpaidBookings(final Teacher teacher) {
		return bookingDAO.getUnpaidBookings(teacher);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<PupilBooking> getTeacherNotificationBookings(final Teacher teacher) {
		return bookingDAO.getTeacherNotificationBookings(teacher);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Map<Pupil, List<PupilBooking>> getPupilNotificationBookings() {
		return bookingDAO.getPupilNotificationBookings();
	}
	
	@Override
	public void teacherNotificationMailSent(final List<PupilBooking> pupilBookings) {
		bookingDAO.teacherNotificationMailSent(pupilBookings);
	}
	
	@Override
	public void pupilNotificationMailSent(final Map<Pupil, List<PupilBooking>> pupilBookings) {
		bookingDAO.pupilNotificationMailSent(pupilBookings);
	}
	
	@Override
	public void changePaidStatus(final PupilBooking pupilBooking) {
		if (pupilBooking.isActive() == false) {
			throw new IllegalArgumentException("Can only change paid status on active bookings");
		}
		
		bookingDAO.changePaidStatus(pupilBooking);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<PupilBooking> getPaidBookings(final Teacher teacher, final DateMidnight startDate, final DateMidnight endDate) {
		return bookingDAO.getPaidBookings(teacher, startDate, endDate);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<PupilBooking> getUnPaidBookings(final Teacher teacher, final DateMidnight fromDate, final DateMidnight toDate) {
		return bookingDAO.getUnPaidBookings(teacher, fromDate, toDate);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Integer> getYearsWithPaidBookings(final Teacher teacher) {
		return bookingDAO.getYearsWithPaidBookings(teacher);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Integer> getYearsWithBookings(final Teacher teacher) {
		return bookingDAO.getYearsWithBookings(teacher);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Bookings getAllBookings(final Teacher teacher) {
		return bookingDAO.getAllBookings(teacher);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Bookings getBookings(final Teacher teacher, final DateMidnight fromDate, final DateMidnight toDate) {
		return bookingDAO.getBookings(teacher, fromDate, toDate);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Bookings getBookings(final Pupil pupil, final DateMidnight fromDate, final DateMidnight toDate) {
		return bookingDAO.getBookings(pupil, fromDate, toDate);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Booking getBooking(final Long id) {
		return bookingDAO.getBooking(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public int getBookingCount(final Period period) {
		return bookingDAO.getBookingCount(period);
	}
	
	@Transactional(readOnly = true)
	@Override
	public DateTime getLastBookingDate(final Period period) {
		return bookingDAO.getLastBookingDate(period);
	}
	
}
