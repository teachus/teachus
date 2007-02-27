package dk.teachus.frontend.pages.stats;

import dk.teachus.frontend.WicketSpringTestCase;
import dk.teachus.frontend.pages.stats.IncomePerPupilPage;

public class TestIncomePerPupilPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(IncomePerPupilPage.class);
		
		tester.assertRenderedPage(IncomePerPupilPage.class);
	}
	
}
