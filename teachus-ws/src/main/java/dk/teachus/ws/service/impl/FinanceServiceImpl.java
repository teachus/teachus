package dk.teachus.ws.service.impl;

import java.util.List;

import org.joda.time.DateMidnight;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.ws.service.FinanceService;

public class FinanceServiceImpl extends AbstractService implements FinanceService {
	
	private BookingDAO bookingDAO;

	public FinanceServiceImpl(PersonDAO personDAO, BookingDAO bookingDAO) {
		super(personDAO);
		this.bookingDAO = bookingDAO;
	}

	public double getIncomeForMonth(String teacherUserId, String password, int year, int month) {
		double amount = 0;
		
		Person person = authenticate(teacherUserId, password);
		
		if (person instanceof Teacher == false) {
			throw new IllegalArgumentException("The specified user was not a teacher.");
		}
		
		Teacher teacher = (Teacher) person;
		
		DateMidnight startDate = new DateMidnight().withYear(year).withMonthOfYear(month).withDayOfMonth(1);
		DateMidnight endDate = new DateMidnight().withYear(year).withMonthOfYear(month+1).withDayOfMonth(1);
		List<PupilBooking> paidBookings = bookingDAO.getPaidBookings(teacher, startDate.toDate(), endDate.toDate());
		
		for (PupilBooking booking : paidBookings) {
			amount += booking.getPeriod().getPrice();
		}
		
		return amount;
	}

}
