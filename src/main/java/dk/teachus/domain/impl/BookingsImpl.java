package dk.teachus.domain.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;

public class BookingsImpl implements Bookings {
	private static final long serialVersionUID = 1L;

	private List<Booking> bookings;
	
	public BookingsImpl(List<Booking> pupilBookings) {
		this.bookings = pupilBookings;
	}

	public Booking getBooking(Date time) {
		Booking booking = null;
		
		for (Booking foundBooking : bookings) {
			DateTime dt1 = new DateTime(foundBooking.getDate());
			DateTime dt2 = new DateTime(time);
			
			if (dt1.toDateMidnight().equals(dt2.toDateMidnight())) {
				if (dt1.getHourOfDay() == dt2.getHourOfDay()
						&& dt1.getMinuteOfHour() == dt2.getMinuteOfHour()) {
					booking = foundBooking;
					break;
				}
			}
		}
		
		return booking;
	}

	public List<Booking> getBookingList() {
		return bookings;
	}

}
