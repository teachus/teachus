package dk.teachus.frontend.pages;

import dk.teachus.frontend.WicketSpringTestCase;

public class TestTeacherSettingsPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(TeacherSettingsPage.class);
		
		tester.assertRenderedPage(TeacherSettingsPage.class);
	}
	
}
