package dk.teachus.frontend.pages;

import dk.teachus.frontend.WicketSpringTestCase;

public class TestPaymentPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(PaymentPage.class);
		
		tester.assertRenderedPage(PaymentPage.class);
	}
	
}
