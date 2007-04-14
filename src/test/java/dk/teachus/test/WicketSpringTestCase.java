package dk.teachus.test;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

import wicket.Component;
import wicket.ISessionFactory;
import wicket.Request;
import wicket.Session;
import wicket.markup.html.image.Image;
import wicket.protocol.http.WebApplication;
import wicket.util.tester.TagTester;
import wicket.util.tester.WicketTester;
import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.database.StaticDataImport;
import dk.teachus.domain.Period;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherBooking;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.domain.impl.TeacherImpl;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public abstract class WicketSpringTestCase extends AbstractAnnotationAwareTransactionalTests implements Serializable {

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
		
		public Person getPerson(Long personId) {
			return getTeachUsApplication().getPersonDAO().getPerson(personId);
		}
		
		public TeachUsApplication getTeachUsApplication() {
			return (TeachUsApplication) getApplication();
		}
	}
	
	public static class TesterTeachUsSession extends TeachUsSession {
		private static final long serialVersionUID = 1L;
		
		public static TesterTeachUsSession get() {
			return (TesterTeachUsSession) TeachUsSession.get();
		}

		private Long personId;
		
		public TesterTeachUsSession(WebApplication application, Request request) {
			super(application, request);
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
		
		@Override
		public boolean isAuthenticated() {
			return personId != null;
		}
		
		public void setPerson(Long personId) {
			this.personId = personId;
		}
	}
	
	protected TeachUsWicketTester tester;
	
	public WicketSpringTestCase() {
		setDefaultRollback(false);	
	}
	
	protected void assertTimeNotSelected(String componentPath) {
		TagTester tagTester = getTagTesterForComponent(componentPath+":contentContainer:content:link");
		assertNull("The time was booked", tagTester.getAttribute("class"));
	}
	
	protected void assertTimeOccupied(String componentPath) {
		tester.assertComponent(componentPath+":contentContainer:content:icon", Image.class);
	}
	
	protected void assertTimeSelected(String componentPath) {
		TagTester tagTester = getTagTesterForComponent(componentPath+":contentContainer:content:link");
		assertEquals("The time wasn't booked", "selected", tagTester.getAttribute("class"));
	}
	
	protected Long createPupilBooking(long periodId, long pupilId, DateTime dateTime, Date createDate) {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(periodId);
		endTransaction();
		
		Pupil pupil = (Pupil) personDAO.getPerson(pupilId);
		endTransaction();
		
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPeriod(period);
		pupilBooking.setPupil(pupil);
		pupilBooking.setTeacher(pupil.getTeacher());
		pupilBooking.setPaid(false);
		pupilBooking.setNotificationSent(false);
		pupilBooking.setCreateDate(createDate);
		pupilBooking.setDate(dateTime.toDate());
		
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		return pupilBooking.getId();
	}
	
	protected Long createTeacherBooking(long periodId, long teacherId, DateTime date) {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(periodId);
		endTransaction();
		
		Teacher teacher = (Teacher) personDAO.getPerson(teacherId);
		endTransaction();
		
		TeacherBooking teacherBooking = bookingDAO.createTeacherBookingObject();
		teacherBooking.setCreateDate(new DateTime().minusHours(3).toDate());
		teacherBooking.setDate(date.toDate());
		teacherBooking.setPeriod(period);
		teacherBooking.setTeacher(teacher);
		
		bookingDAO.book(teacherBooking);
		endTransaction();
		
		return teacherBooking.getId();
	}

	public BookingDAO getBookingDAO() {
		return tester.getTeachUsApplication().getBookingDAO();
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {
				"/dk/teachus/frontend/applicationContext.xml",
				"/dk/teachus/test/applicationContext-test.xml"
		};
	}

	public PeriodDAO getPeriodDAO() {
		return tester.getTeachUsApplication().getPeriodDAO();
	}

	public PersonDAO getPersonDAO() {
		return tester.getTeachUsApplication().getPersonDAO();
	}

	public SessionFactory getSessionFactory() {
		return (SessionFactory) applicationContext.getBean("sessionFactory");
	}

	private TagTester getTagTesterForComponent(String componentPath) {
		Component comp = tester.getComponentFromLastRenderedPage(componentPath);
		TagTester tagTester = tester.getTagById(comp.getMarkupId());
		return tagTester;
	}
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		tester = new TeachUsWicketTester(applicationContext);
		
		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		new StaticDataImport(sessionFactory.openSession().connection());
	}

	protected Pupil createPupil(Teacher teacher, int pupilNumber) {
		Pupil pupil = new PupilImpl();
		pupil.setName("Test pupil "+pupilNumber);
		pupil.setActive(true);
		pupil.setEmail("pupil"+pupilNumber+"@teachus.dk");
		pupil.setUsername("pupil"+pupilNumber);
		pupil.setTeacher(teacher);
		getPersonDAO().save(pupil);
		endTransaction();
		return pupil;
	}

	protected Teacher createTeacher() {
		Teacher teacher = new TeacherImpl();
		teacher.setName("Test name");
		teacher.setActive(true);
		teacher.setEmail("test@teachus.dk");
		teacher.setUsername("test");
		getPersonDAO().save(teacher);
		endTransaction();
		return teacher;
	}
	
	protected Object loadObject(Class objectClass, Serializable objectId) {
		Object object = null;
		
		org.hibernate.Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		object = session.get(objectClass, objectId);
		
		session.getTransaction().commit();
		session.close();
		
		return object;
	}

	protected Teacher inactivateTeacher() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		teacher.setActive(false);
		
		getPersonDAO().save(teacher);
		endTransaction();
		return teacher;
	}

	protected Teacher getTeacher() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		return teacher;
	}
	
}
