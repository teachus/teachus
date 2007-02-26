package dk.frankbille.teachus.frontend.pages.stats;

import dk.frankbille.teachus.frontend.WicketSpringTestCase;
import dk.frankbille.teachus.frontend.pages.stats.IncomePerPeriodPage;

public class TestIncomePerPeriodPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(IncomePerPeriodPage.class);
		
		tester.assertRenderedPage(IncomePerPeriodPage.class);
	}
	
}
