package dk.frankbille.teachus.domain.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.PupilBookings;

public class PupilBookingsImpl implements PupilBookings {
	private static final long serialVersionUID = 1L;

	private List<PupilBooking> pupilBookings;
	
	public PupilBookingsImpl(List<PupilBooking> pupilBookings) {
		this.pupilBookings = pupilBookings;
	}

	public PupilBooking getBooking(Date time) {
		PupilBooking booking = null;
		
		for (PupilBooking pupilBooking : pupilBookings) {
			DateTime dt1 = new DateTime(pupilBooking.getDate());
			DateTime dt2 = new DateTime(time);
			
			if (dt1.toDateMidnight().equals(dt2.toDateMidnight())) {
				if (dt1.getHourOfDay() == dt2.getHourOfDay()
						&& dt1.getMinuteOfHour() == dt2.getMinuteOfHour()) {
					booking = pupilBooking;
					break;
				}
			}
		}
		
		return booking;
	}

	public List<PupilBooking> getBookingList() {
		return pupilBookings;
	}

}
