package dk.frankbille.teachus.bean.impl;

import java.util.List;

import dk.frankbille.teachus.bean.MailBean;
import dk.frankbille.teachus.bean.NewBookings;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.PupilBookings;
import dk.frankbille.teachus.domain.Teacher;

public class NewBookingsImpl implements NewBookings {
	private static final long serialVersionUID = 1L;
	
	private BookingDAO bookingDAO;
	private PersonDAO personDAO;
	private MailBean mailBean;

	public NewBookingsImpl(BookingDAO bookingDAO, PersonDAO personDAO, MailBean mailBean) {
		this.bookingDAO = bookingDAO;
		this.personDAO = personDAO;
		this.mailBean = mailBean;
	}

	public void sendNewBookingsMail() {
		List<Teacher> teachers = personDAO.getPersons(Teacher.class);
		
		for (Teacher teacher : teachers) {
			PupilBookings pupilBookings = bookingDAO.getUnsentBookings(teacher);
			
			mailBean.sendNewBookingsMail(teacher, pupilBookings);
			
			bookingDAO.newBookingsMailSent(pupilBookings);
		}
	}

}
