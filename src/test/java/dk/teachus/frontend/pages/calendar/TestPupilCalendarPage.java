package dk.teachus.frontend.pages.calendar;

import org.joda.time.DateTime;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.domain.Pupil;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.WicketSpringTestCase;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;

public class TestPupilCalendarPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender_teacher() {
		final Pupil pupil = (Pupil) tester.getPerson(11l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilCalendarPage(pupil);
			}			
		});
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.assertComponent("calendar", CalendarPanel.class);
		
		tester.assertContains(pupil.getName());
	}
	
	public void testRender_pupil() {
		// Log in as pupil
		TesterTeachUsSession.get().setPerson(11l);
		
		tester.startPage(PupilCalendarPage.class);
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.assertComponent("calendar", CalendarPanel.class);
		
		tester.assertContains(TeachUsSession.get().getPerson().getName());
	}
	
	public void testPupilBooked() {
		final DateTime dateTime = new DateTime(2007, 6, 11, 11, 0, 0, 0);
		
		final Pupil pupil = (Pupil) getPersonDAO().getPerson(6L);
		
		// First create a booking for the pupil
		createPupilBooking(1, pupil.getId(), dateTime);
		
		// Then a booking for a different pupil
		createPupilBooking(1, 7, dateTime.plusHours(2));
		
		// Start the calendar
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilCalendarPage(dateTime.toDate(), pupil);
			}			
		});
		
		tester.assertRenderedPage(PupilCalendarPage.class);
		
		tester.debugComponentTrees();
		
		assertTimeSelected("calendar:calendar:weeks:1:days:1:periods:1:period:rows:2");
		
		assertTimeOccupied("calendar:calendar:weeks:1:days:1:periods:1:period:rows:4");
	}
}
