package dk.frankbille.teachus.frontend;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import wicket.ISessionFactory;
import wicket.Request;
import wicket.RequestCycle;
import wicket.Session;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebRequestCycle;
import wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.dao.PeriodDAO;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.frontend.pages.AdminsPage;
import dk.frankbille.teachus.frontend.pages.AgendaPage;
import dk.frankbille.teachus.frontend.pages.HomePage;
import dk.frankbille.teachus.frontend.pages.PaymentPage;
import dk.frankbille.teachus.frontend.pages.PeriodPage;
import dk.frankbille.teachus.frontend.pages.PupilCalendarPage;
import dk.frankbille.teachus.frontend.pages.PupilsPage;
import dk.frankbille.teachus.frontend.pages.SignInPage;
import dk.frankbille.teachus.frontend.pages.SignOutPage;
import dk.frankbille.teachus.frontend.pages.TeachersPage;
import dk.frankbille.teachus.utils.ApplicationUtils;

public class TeachUsApplication extends WebApplication {
	
	@Override
	protected void init() {
//		PeriodDAO periodDAO = getPeriodDAO();
//		PersonDAO personDAO = getPersonDAO();
//		BookingDAO bookingDAO = getBookingDAO();
//		
//		Teacher teacher = new TeacherImpl();
//		teacher.setName("Signe Stauner"); //$NON-NLS-1$
//		teacher.setEmail("signe@billen.dk");
//		teacher.setUsername("uv1"); //$NON-NLS-1$
//		teacher.setPassword("u"); //$NON-NLS-1$
//		teacher.setLocale(new Locale("da", "DK", "singers")); //$NON-NLS-1$ //$NON-NLS-2$
//		personDAO.save(teacher);
//		
//		
//		Pupil pupil1 = new PupilImpl();
//		pupil1.setName("Sanne"); //$NON-NLS-1$
//		pupil1.setUsername("sanger1"); //$NON-NLS-1$
//		pupil1.setPassword("s"); //$NON-NLS-1$
//		pupil1.setEmail("sanger1@billen.dk"); //$NON-NLS-1$
//		pupil1.setTeacher(teacher);
//		personDAO.save(pupil1);
//		
//		Pupil pupil2 = new PupilImpl();
//		pupil2.setName("Martin"); //$NON-NLS-1$
//		pupil2.setUsername("sanger2"); //$NON-NLS-1$
//		pupil2.setPassword("s"); //$NON-NLS-1$
//		pupil2.setEmail("sanger2@billen.dk"); //$NON-NLS-1$
//		pupil2.setTeacher(teacher);
//		personDAO.save(pupil2);
//		
//		Pupil pupil3 = new PupilImpl();
//		pupil3.setName("Bernadette"); //$NON-NLS-1$
//		pupil3.setUsername("sanger3"); //$NON-NLS-1$
//		pupil3.setPassword("s"); //$NON-NLS-1$
//		pupil3.setEmail("sanger3@billen.dk"); //$NON-NLS-1$
//		pupil3.setTeacher(teacher);
//		personDAO.save(pupil3);
//				
//		
//		Admin admin = new AdminImpl();
//		admin.setName("Frank Bille"); //$NON-NLS-1$
//		admin.setEmail("frank@billen.dk");
//		admin.setUsername("admin"); //$NON-NLS-1$
//		admin.setPassword("a"); //$NON-NLS-1$
//		personDAO.save(admin);
//		
//		Period p = new PeriodImpl();
//		p.setName("Mandag/Torsdag"); //$NON-NLS-1$
//		p.setBeginDate(new DateMidnight(2007, 1, 1).toDate());
//		p.addWeekDay(WeekDay.MONDAY);
//		p.addWeekDay(WeekDay.THURSDAY);
//		p.setStartTime(new GregorianCalendar(1, 1, 1, 10, 0).getTime());
//		p.setEndTime(new GregorianCalendar(1, 1, 1, 15, 0).getTime());
//		p.setTeacher(teacher);
//		p.setPrice(200);
//		periodDAO.save(p);
//		
//		periodDAO.get(p.getId());
//
//		PupilBooking booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil1);
//		booking.setDate(new DateTime(2007, 2, 12, 12, 0, 0, 0).toDate());
//		bookingDAO.book(booking);
//		
//		booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil1);
//		booking.setDate(new DateTime(2007, 2, 19, 12, 0, 0, 0).toDate());
//		bookingDAO.book(booking);
//		
//		booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil1);
//		booking.setDate(new DateTime(2007, 2, 26, 12, 0, 0, 0).toDate());
//		booking.setCreateDate(new DateTime().minusHours(2).toDate());
//		bookingDAO.book(booking);
//		
//		booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil2);
//		booking.setDate(new DateTime(2007, 2, 12, 13, 0, 0, 0).toDate());
//		bookingDAO.book(booking);
//		
//		booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil2);
//		booking.setDate(new DateTime(2007, 2, 26, 13, 0, 0, 0).toDate());
//		bookingDAO.book(booking);
//		
//		booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil3);
//		booking.setDate(new DateTime(2007, 2, 15, 14, 0, 0, 0).toDate());
//		bookingDAO.book(booking);
//		
//		booking = new PupilBookingImpl();
//		booking.setPeriod(p);
//		booking.setPupil(pupil3);
//		booking.setDate(new DateTime(2007, 2, 22, 14, 0, 0, 0).toDate());
//		bookingDAO.book(booking);
		
		// Settings
		getSecuritySettings().setAuthorizationStrategy(new TeachUsAuthentication());
		
		getResourceSettings().setStripJavascriptCommentsAndWhitespace(false);
		
		// Bookmarkable pages
		mountBookmarkablePage("/signin", SignInPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/signout", SignOutPage.class); //$NON-NLS-1$
		mount("/calendar", new IndexedParamUrlCodingStrategy("/calendar", PupilCalendarPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/pupils", new IndexedParamUrlCodingStrategy("/pupils", PupilsPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/teachers", new IndexedParamUrlCodingStrategy("/teachers", TeachersPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/admins", new IndexedParamUrlCodingStrategy("/admins", AdminsPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/periods", new IndexedParamUrlCodingStrategy("/periods", PeriodPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/agenda", new IndexedParamUrlCodingStrategy("/agenda", AgendaPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/payment", new IndexedParamUrlCodingStrategy("/payment", PaymentPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public PersonDAO getPersonDAO() {
		return (PersonDAO) getApplicationContext().getBean("personDao"); //$NON-NLS-1$
	}
	
	public BookingDAO getBookingDAO() {
		return (BookingDAO) getApplicationContext().getBean("bookingDao"); //$NON-NLS-1$
	}

	public PeriodDAO getPeriodDAO() {
		return (PeriodDAO) getApplicationContext().getBean("periodDao"); //$NON-NLS-1$
	}
	
	private ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}

	@Override
	public Class getHomePage() {
		return HomePage.class;
	}
	
	@Override
	protected ISessionFactory getSessionFactory() {
		return new ISessionFactory() {
			public Session newSession(Request request) {
				return new TeachUsSession(TeachUsApplication.this, request);
			}
		};
	}

	public static TeachUsApplication get() {
		return (TeachUsApplication) WebApplication.get();
	}

	public List<Locale> getAvailableLocales() {
		return Arrays.asList(new Locale[] {
				new Locale("en", "US"),
				new Locale("da", "DK"),
				new Locale("da", "DK", "singers")
		});
	}
	
	public String getVersion() {
		return ApplicationUtils.getVersion();
	}
	
	public String getServerName() {
		WebRequestCycle requestCycle = (WebRequestCycle) RequestCycle.get();
		HttpServletRequest httpServletRequest = requestCycle.getWebRequest().getHttpServletRequest();
		
		StringBuilder sb = new StringBuilder();
		sb.append(httpServletRequest.getScheme());
		sb.append("://");
		sb.append(httpServletRequest.getServerName());
		if (httpServletRequest.getServerPort() != 80) {
			sb.append(":");
			sb.append(httpServletRequest.getServerPort());
		}
		sb.append("/");
		sb.append(httpServletRequest.getContextPath());
		
		return sb.toString();
	}
}
