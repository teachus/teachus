package dk.teachus.frontend.pages;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.pages.persons.PupilPage;
import dk.teachus.test.WicketSpringTestCase;

public class TestAgendaPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		PersonDAO personDAO = getPersonDAO();
		BookingDAO bookingDAO = getBookingDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Pupil pupil = (Pupil) personDAO.getPerson(4L);
		endTransaction();
				
		Period period = periodDAO.get(1L);
		endTransaction();
				
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		endTransaction();
		
		pupilBooking.setPupil(pupil);
		pupilBooking.setDate(new DateTime().plusWeeks(1).withDayOfWeek(DateTimeConstants.MONDAY).withTime(12, 0, 0, 0).toDate());
		pupilBooking.setTeacher(pupil.getTeacher());
		pupilBooking.setPeriod(period);
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		tester.startPage(AgendaPage.class);
		
		tester.assertRenderedPage(AgendaPage.class);
		
		// Test clicking on one of the items
		tester.clickLink("list:table:rows:1:cells:1:cell:link");
		
		tester.assertRenderedPage(PupilPage.class);
	}
	
}
