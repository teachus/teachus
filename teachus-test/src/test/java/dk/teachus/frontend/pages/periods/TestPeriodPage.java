package dk.teachus.frontend.pages.periods;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.apache.wicket.util.tester.WicketTester;

import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.frontend.test.WicketSpringTestCase;

public class TestPeriodPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testEndDateValidation_newPeriod() {
		WicketTester tester = createTester();
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				PeriodPage page = new PeriodPage(new PeriodImpl());
				return page;
			}
		});
		
		tester.assertRenderedPage(PeriodPage.class);
		
		// Enter an end date
		FormTester formTester = tester.newFormTester("form:form");
		formTester.setValue("elements:3:element:input:dateField", "2007-10-06");
		formTester.submit();
	}
	
}
