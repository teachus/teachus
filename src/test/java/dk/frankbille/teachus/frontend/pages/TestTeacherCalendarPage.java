package dk.frankbille.teachus.frontend.pages;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.WicketSpringTestCase;
import dk.frankbille.teachus.frontend.components.CalendarPanel;

public class TestTeacherCalendarPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		tester.assertComponent("calendar", CalendarPanel.class);
		
		tester.assertContains(TeachUsSession.get().getPerson().getName());
	}
}
