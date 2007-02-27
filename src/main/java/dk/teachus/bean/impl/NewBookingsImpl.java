package dk.teachus.bean.impl;

import java.util.List;

import dk.teachus.bean.MailBean;
import dk.teachus.bean.NewBookings;
import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;

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
			List<PupilBooking> pupilBookings = bookingDAO.getUnsentBookings(teacher);
			
			mailBean.sendNewBookingsMail(teacher, pupilBookings);
			
			bookingDAO.newBookingsMailSent(pupilBookings);
		}
	}

}
