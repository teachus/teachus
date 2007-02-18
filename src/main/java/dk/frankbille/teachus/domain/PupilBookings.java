package dk.frankbille.teachus.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface PupilBookings extends Serializable {

	PupilBooking getBooking(Date time);

	List<PupilBooking> getBookingList();
	
}
