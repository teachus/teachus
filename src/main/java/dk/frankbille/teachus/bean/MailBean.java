package dk.frankbille.teachus.bean;

import java.io.Serializable;

import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBookings;
import dk.frankbille.teachus.domain.Teacher;

public interface MailBean extends Serializable {

	void sendWelcomeMail(Pupil pupil);
	
	void sendNewBookingsMail(Teacher teacher, PupilBookings pupilBookings);
	
}
