package dk.teachus.frontend.components.calendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public class PupilPeriodDateComponent extends BookingPeriodDateComponent {
	private static final long serialVersionUID = 1L;
	
	private Pupil pupil;

	public PupilPeriodDateComponent(String id, Pupil pupil, Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date, bookings);
		
		this.pupil = pupil;
	}

	@Override
	protected Booking createNewBookingObject(BookingDAO bookingDAO) {
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPupil(pupil);
		return pupilBooking;
	}

	@Override
	protected boolean isChangeable(Booking booking) {
		boolean changeable = false;
		if (booking != null) {
			if (booking instanceof PupilBooking) {
				PupilBooking pupilBooking = (PupilBooking) booking;
				changeable = pupilBooking.getPupil().getId().equals(pupil.getId());
			}
		}
		return changeable;
	}

	@Override
	protected boolean shouldDisplayStringInsteadOfOccupiedIcon() {
		return false;
	}

	@Override
	protected boolean mayChangeBooking(Period period, DateTime bookingStartTime) {
		boolean mayChangeBooking = false;

		if (TeachUsSession.get().getUserLevel() == UserLevel.TEACHER) {
			mayChangeBooking = true;
		} else {
			DateTime today = new DateTime().withTime(23, 59, 59, 999);
			mayChangeBooking = bookingStartTime.isAfter(today);
		}

		return mayChangeBooking;
	}
	
	@Override
	protected BookingPeriodDateComponent createNewInstance(String id, Period period, DateMidnight date, Bookings bookings) {
		return new PupilPeriodDateComponent(id, pupil, period, date, bookings);
	}

}
