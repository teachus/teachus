package dk.frankbille.teachus.frontend.pages;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.WicketSpringTestCase;
import dk.frankbille.teachus.frontend.components.CalendarPanel;

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
