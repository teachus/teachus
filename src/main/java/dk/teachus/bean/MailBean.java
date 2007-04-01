package dk.teachus.bean;

import java.io.Serializable;
import java.util.List;

import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;

public interface MailBean extends Serializable {

	void sendWelcomeMail(Pupil pupil, String introMessage, String serverName);
	
	void sendNewBookingsMail(Teacher teacher, List<PupilBooking> pupilBookings);
	
}
