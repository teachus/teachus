package dk.frankbille.teachus.frontend.components;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.Component;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Booking;
import dk.frankbille.teachus.domain.Bookings;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsApplication;

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
