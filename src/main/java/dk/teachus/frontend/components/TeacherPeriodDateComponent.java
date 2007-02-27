package dk.teachus.frontend.components;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.Component;
import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;

public class TeacherPeriodDateComponent extends PeriodDateComponent {
	private static final long serialVersionUID = 1L;

	private Bookings bookings;
	private Teacher teacher;

	public TeacherPeriodDateComponent(String id, Teacher teacher, Period period, DateMidnight date) {
		super(id, period, date);		
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		
		this.bookings = bookingDAO.getBookingsForDate(period, date.toDate());
		this.teacher = teacher;
	}

	@Override
	protected Component getTimeContent(String wicketId, Period period, DateTime time) {
		Booking booking = bookings.getBooking(time.toDate());
		
		return new TeacherPeriodDateComponentPanel(wicketId, teacher, booking, period, time);
	}

}
