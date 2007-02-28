package dk.teachus.frontend.components;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.Component;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;

public class PupilPeriodDateComponent extends PeriodDateComponent {
	private static final long serialVersionUID = 1L;
	
	private Pupil pupil;
	private Bookings bookings;

	public PupilPeriodDateComponent(String id, Pupil pupil, Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date);
		
		this.pupil = pupil;
		this.bookings = bookings;
	}

	@Override
	protected Component getTimeContent(String wicketId, Period period, DateTime time) {
		Booking booking = bookings.getBooking(time.toDate());
		
		return new PupilPeriodDateComponentPanel(wicketId, booking, pupil, period, time);
	}

}
