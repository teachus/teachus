package dk.teachus.frontend.components;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.Component;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Teacher;

public class TeacherPeriodDateComponent extends PeriodDateComponent {
	private static final long serialVersionUID = 1L;

	private Bookings bookings;
	private Teacher teacher;

	public TeacherPeriodDateComponent(String id, Teacher teacher, Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date);
		
		this.bookings = bookings;
		this.teacher = teacher;
	}

	@Override
	protected Component getTimeContent(String wicketId, Period period, DateTime time) {
		Booking booking = bookings.getBooking(time.toDate());
		
		return new TeacherPeriodDateComponentPanel(wicketId, teacher, booking, period, time);
	}

}
