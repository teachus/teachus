package dk.frankbille.teachus.frontend.pages.periods;

import dk.frankbille.teachus.frontend.WicketSpringTestCase;
import dk.frankbille.teachus.frontend.pages.periods.PeriodsPage;

public class TestPeriodsPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(PeriodsPage.class);
		
		tester.assertRenderedPage(PeriodsPage.class);
	}
	
}
