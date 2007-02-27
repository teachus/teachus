package dk.teachus.frontend.pages;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.domain.Pupil;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.WicketSpringTestCase;
import dk.teachus.frontend.components.CalendarPanel;
import dk.teachus.frontend.pages.PupilCalendarPage;

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
}
