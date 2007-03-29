package dk.teachus.bean.impl;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;

import dk.teachus.bean.MailBean;
import dk.teachus.bean.VelocityBean;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;

/**
 * Mail implementation which doesn't send mails.
 */
public class DummyMailBean implements MailBean {
	private static final long serialVersionUID = 1L;
	
	public DummyMailBean(PersonDAO personDAO, JavaMailSender mailSender, VelocityBean velocityBean) {
	}

	public void sendNewBookingsMail(Teacher teacher, List<PupilBooking> pupilBookings) {
	}

	public void sendWelcomeMail(Pupil pupil, String serverName) {
	}

}
