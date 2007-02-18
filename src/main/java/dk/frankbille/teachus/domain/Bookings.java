package dk.frankbille.teachus.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface Bookings extends Serializable {

	Booking getBooking(Date time);

	List<Booking> getBookingList();
	
}
