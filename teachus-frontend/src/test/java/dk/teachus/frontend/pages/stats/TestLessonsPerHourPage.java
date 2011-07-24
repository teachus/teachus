package dk.teachus.frontend.pages.stats;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.joda.time.DateMidnight;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.pages.stats.teacher.LessonsPerHourPage;
import dk.teachus.frontend.test.WicketTestCase;

public class TestLessonsPerHourPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;
	
	public void testRender() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			BookingDAO bookingDAO = createBookingDAO();
			
			one(personDAO).getPerson(2L);
			Teacher teacher = createTeacher();
			will(returnValue(teacher));
						
			List<Integer> years = new ArrayList<Integer>();
			years.add(new DateMidnight().getYear());
			
			one(bookingDAO).getYearsWithBookings(teacher);
			will(returnValue(years));
			
			DateMidnight startDate = new DateMidnight().withMonthOfYear(1).withDayOfMonth(1);
			DateMidnight endDate = new DateMidnight().withMonthOfYear(12).withDayOfMonth(31);
			
			List<PupilBooking> bookings = new ArrayList<PupilBooking>();
			PupilBooking pupilBooking = createPupilBooking(1L);
			pupilBooking.setPaid(true);
			bookings.add(pupilBooking);
			
			one(bookingDAO).getPaidBookings(teacher, startDate, endDate);
			will(returnValue(bookings));
			
			bookings = new ArrayList<PupilBooking>();
			pupilBooking = createPupilBooking(2L);
			pupilBooking.setPaid(false);
			bookings.add(pupilBooking);
			
			one(bookingDAO).getUnPaidBookings(teacher, startDate, endDate);
			will(returnValue(bookings));
			
			tester.setPersonDAO(personDAO);
			tester.setBookingDAO(bookingDAO);
		}});
		
		tester.startPage(LessonsPerHourPage.class);
		
		tester.assertRenderedPage(LessonsPerHourPage.class);
	}
	
}
