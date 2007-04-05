package dk.teachus.frontend.pages.settings;

import dk.teachus.frontend.WicketSpringTestCase;

public class TestWelcomeMailSettingsPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(WelcomeMailSettingsPage.class);
		
		tester.assertRenderedPage(WelcomeMailSettingsPage.class);
	}
	
}
