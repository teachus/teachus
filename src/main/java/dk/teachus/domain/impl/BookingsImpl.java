package dk.teachus.domain.impl;

import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;

public class BookingsImpl implements Bookings {
	private static final long serialVersionUID = 1L;

	private List<Booking> bookings;
	
	public BookingsImpl(List<Booking> pupilBookings) {
		this.bookings = pupilBookings;
	}

	public Booking getBooking(DateTime time) {
		Booking booking = null;
		
		for (Booking foundBooking : bookings) {
			DateTime dt1 = new DateTime(foundBooking.getDate());
			DateTime dt2 = time;
			
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
	
	public boolean mayBook(Period period, DateTime time) {
		boolean mayBook = false;
		
		{
			boolean conflicts = false;
			
			for (Booking booking : bookings) {
				if (booking.getPeriod().getId().equals(period.getId())) {
					if (period.conflicts(new DateTime(booking.getDate()), time)) {
						conflicts = true;
						break;
					}
				}
			}
			
			mayBook = conflicts == false;
		}
		
		return mayBook;
	}
	
	public boolean isBeforeBooking(Period period, DateTime time) {
		boolean beforeBooking = false;
		
		{
			boolean inLesson = false;
			
			for (Booking booking : bookings) {
				if (booking.getPeriod().getId().equals(period.getId())) {
					if (period.inLesson(new DateTime(booking.getDate()), time)) {
						inLesson = true;
						break;
					}
				}
			}
			
			if (inLesson == false) {
				beforeBooking = true;
			}
		}
		
		return beforeBooking;
	}

}
