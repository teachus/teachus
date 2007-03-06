package dk.teachus.frontend.pages.calendar;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.WicketSpringTestCase;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.pages.calendar.TeacherCalendarPage;

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
	
	public void testBookTeacher() {
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		String timePath = "calendar:calendar:weeks:1:days:2:periods:1:period:rows:3";
		
		assertTimeNotSelected(timePath);

		// Book time
		tester.clickLink(timePath+":contentContainer:content:link");
		
		// Show the page again to check that it is displaying
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		assertTimeSelected(timePath);		
	}
}
