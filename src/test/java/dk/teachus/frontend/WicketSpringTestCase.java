package dk.teachus.frontend;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

import wicket.ISessionFactory;
import wicket.Request;
import wicket.Session;
import wicket.protocol.http.WebApplication;
import wicket.util.tester.WicketTester;
import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.database.DataImport;
import dk.teachus.domain.Person;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public abstract class WicketSpringTestCase extends AbstractAnnotationAwareTransactionalTests implements Serializable {

	protected TeachUsWicketTester tester;
	
	public BookingDAO getBookingDAO() {
		return tester.getTeachUsApplication().getBookingDAO();
	}
	
	public PersonDAO getPersonDAO() {
		return tester.getTeachUsApplication().getPersonDAO();
	}
	
	public PeriodDAO getPeriodDAO() {
		return tester.getTeachUsApplication().getPeriodDAO();
	}
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		tester = new TeachUsWicketTester(applicationContext);
		
		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		new DataImport(sessionFactory.openSession().connection());
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] {
				"/dk/teachus/frontend/applicationContext.xml",
				"/dk/teachus/frontend/applicationContext-test.xml"
		};
	}
	
	public static class TeachUsWicketTester extends WicketTester implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public TeachUsWicketTester(final ApplicationContext applicationContext) {
			super(new TeachUsApplication() {
				@Override
				protected ApplicationContext getApplicationContext() {
					return applicationContext;
				}
				
				@Override
				protected ISessionFactory getSessionFactory() {
					final WebApplication application = this;
					return new ISessionFactory() {
						public Session newSession(Request request) {
							TesterTeachUsSession session = new TesterTeachUsSession(application, request);
							session.setPerson(2L);							
							return session;
						}
					};
				}
			});
		}
		
		public TeachUsApplication getTeachUsApplication() {
			return (TeachUsApplication) getApplication();
		}
		
		public Person getPerson(Long personId) {
			return getTeachUsApplication().getPersonDAO().getPerson(personId);
		}
	}
	
	public static class TesterTeachUsSession extends TeachUsSession {
		private static final long serialVersionUID = 1L;
		
		private Long personId;

		public TesterTeachUsSession(WebApplication application, Request request) {
			super(application, request);
		}
		
		@Override
		public boolean isAuthenticated() {
			return personId != null;
		}
		
		@Override
		public Person getPerson() {
			Person person = null;
			
			if (personId != null) {
				person = TeachUsApplication.get().getPersonDAO().getPerson(personId);
			}
			
			return person;
		}
		
		@Override
		public UserLevel getUserLevel() {
			person = getPerson();
			
			return super.getUserLevel();
		}
		
		public void setPerson(Long personId) {
			this.personId = personId;
		}
		
		public static TesterTeachUsSession get() {
			return (TesterTeachUsSession) TeachUsSession.get();
		}
	}
	
}
