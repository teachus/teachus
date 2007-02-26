package dk.frankbille.teachus.frontend.pages;

import dk.frankbille.teachus.frontend.WicketSpringTestCase;

public class TestAgendaPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(AgendaPage.class);
		
		tester.assertRenderedPage(AgendaPage.class);
	}
	
}
