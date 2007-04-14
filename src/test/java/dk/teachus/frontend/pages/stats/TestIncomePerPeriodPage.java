package dk.teachus.frontend.pages.stats;

import dk.teachus.test.WicketSpringTestCase;

public class TestIncomePerPeriodPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(IncomePerPeriodPage.class);
		
		tester.assertRenderedPage(IncomePerPeriodPage.class);
	}
	
}
