package dk.teachus.domain;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public interface Bookings extends Serializable {

	Booking getBooking(DateTime time);

	List<Booking> getBookingList();

	boolean mayBook(Period period, DateTime time);
	
	boolean isBeforeBooking(Period period, DateTime time);
	
}
