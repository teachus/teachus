package dk.frankbille.teachus.bean;

import java.io.Serializable;
import java.util.List;

import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;

public interface MailBean extends Serializable {

	void sendWelcomeMail(Pupil pupil);
	
	void sendNewBookingsMail(Teacher teacher, List<PupilBooking> pupilBookings);
	
}
