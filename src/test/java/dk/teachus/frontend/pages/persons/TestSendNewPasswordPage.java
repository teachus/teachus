package dk.teachus.frontend.pages.persons;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.test.WicketSpringTestCase;

public class TestSendNewPasswordPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new SendNewPasswordPage(4L);
			}
		});
		
		tester.assertRenderedPage(SendNewPasswordPage.class);
	}
	
}
