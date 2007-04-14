package dk.teachus.frontend.pages;

import dk.teachus.test.WicketSpringTestCase;

public class TestPaymentPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(PaymentPage.class);
		
		tester.assertRenderedPage(PaymentPage.class);
	}
	
	public void testRender_pupil() {
		// Log in as pupil
		TesterTeachUsSession.get().setPerson(11l);
		
		tester.startPage(PaymentPage.class);
		
		tester.assertRenderedPage(PaymentPage.class);
	}
	
}
