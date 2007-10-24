package dk.teachus.frontend.test;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.util.tester.WicketTester;
import org.springframework.context.ApplicationContext;

import dk.teachus.backend.test.SpringTestCase;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public abstract class WicketSpringTestCase extends SpringTestCase {

	private class TestTeachUsApplication extends TeachUsApplication {
		private Long personId;
		
		public TestTeachUsApplication(Long personId) {
			this.personId = personId;
		}

		@Override
		protected ApplicationContext getApplicationContext() {
			return applicationContext;
		}
		
		@Override
		public Session newSession(Request request, Response response) {
			return new TeachUsSession(this, request) {
				private static final long serialVersionUID = 1L;
				
				{
					if (personId != null) {
						person = getPersonDAO().getPerson(personId);
					}
				}
			};
		}
	}

	protected WicketTester createTester() {
		return createTester(2L);
	}
	
	protected WicketTester createTester(Long personId) {
		TestTeachUsApplication application = new TestTeachUsApplication(personId);
		return new WicketTester(application);
	}
	
}
