package dk.teachus.frontend;

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
import wicket.util.io.IObjectStreamFactory;
import wicket.util.lang.Objects;
import dk.teachus.bean.MailBean;
import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.dao.PersonDAO;
import dk.teachus.frontend.pages.AgendaPage;
import dk.teachus.frontend.pages.HomePage;
import dk.teachus.frontend.pages.InfoPage;
import dk.teachus.frontend.pages.PaymentPage;
import dk.teachus.frontend.pages.SignOutPage;
import dk.teachus.frontend.pages.SignedOutPage;
import dk.teachus.frontend.pages.TeacherSettingsPage;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;
import dk.teachus.frontend.pages.calendar.TeacherCalendarPage;
import dk.teachus.frontend.pages.periods.PeriodsPage;
import dk.teachus.frontend.pages.persons.AdminsPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;
import dk.teachus.frontend.pages.stats.IncomePerPeriodPage;
import dk.teachus.frontend.pages.stats.IncomePerPupilPage;
import dk.teachus.frontend.pages.stats.StatsPage;
import dk.teachus.frontend.utils.Resources;
import dk.teachus.utils.ApplicationUtils;

public class TeachUsApplication extends WebApplication {
	
	@Override
	protected void init() {
		Objects.setObjectStreamFactory(new IObjectStreamFactory.DefaultObjectStreamFactory());
		
		// Settings
		getSecuritySettings().setAuthorizationStrategy(new TeachUsAuthentication());
		
		mountPages();
		
		mountResources();
	}

	private void mountPages() {
		mountBookmarkablePage("/signout", SignOutPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/signedout", SignedOutPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/calendar", PupilCalendarPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/calendar/teacher", TeacherCalendarPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/pupils", PupilsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/teachers", TeachersPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/teachersettings", TeacherSettingsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/admins", AdminsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/periods", PeriodsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/agenda", AgendaPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/payment", PaymentPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/stats", StatsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/stats/incomeperpupil", IncomePerPupilPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/stats/incomeperperiod", IncomePerPeriodPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/info", InfoPage.class); //$NON-NLS-1$
	}

	private void mountResources() {
		mountSharedResource("/images/screenshots/1.png", Resources.SCREENSHOT_1.getSharedResourceKey());
		mountSharedResource("/images/screenshots/1_thumb.jpg", Resources.SCREENSHOT_1_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/2.png", Resources.SCREENSHOT_2.getSharedResourceKey());
		mountSharedResource("/images/screenshots/2_thumb.jpg", Resources.SCREENSHOT_2_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/3.png", Resources.SCREENSHOT_3.getSharedResourceKey());
		mountSharedResource("/images/screenshots/3_thumb.jpg", Resources.SCREENSHOT_3_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/4.png", Resources.SCREENSHOT_4.getSharedResourceKey());
		mountSharedResource("/images/screenshots/4_thumb.jpg", Resources.SCREENSHOT_4_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/5.png", Resources.SCREENSHOT_5.getSharedResourceKey());
		mountSharedResource("/images/screenshots/5_thumb.jpg", Resources.SCREENSHOT_5_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/6.png", Resources.SCREENSHOT_6.getSharedResourceKey());
		mountSharedResource("/images/screenshots/6_thumb.jpg", Resources.SCREENSHOT_6_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/7.png", Resources.SCREENSHOT_7.getSharedResourceKey());
		mountSharedResource("/images/screenshots/7_thumb.jpg", Resources.SCREENSHOT_7_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/8.png", Resources.SCREENSHOT_8.getSharedResourceKey());
		mountSharedResource("/images/screenshots/8_thumb.jpg", Resources.SCREENSHOT_8_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/9.png", Resources.SCREENSHOT_9.getSharedResourceKey());
		mountSharedResource("/images/screenshots/9_thumb.jpg", Resources.SCREENSHOT_9_THUMB.getSharedResourceKey());
		
		mountSharedResource("/images/available.png", Resources.AVAILABLE.getSharedResourceKey());
		mountSharedResource("/images/available_hover.png", Resources.AVAILABLE_HOVER.getSharedResourceKey());
		mountSharedResource("/images/booked.png", Resources.BOOKED.getSharedResourceKey());
		mountSharedResource("/images/booked_hover.png", Resources.BOOKED_HOVER.getSharedResourceKey());
		mountSharedResource("/images/left.png", Resources.LEFT.getSharedResourceKey());
		mountSharedResource("/images/right.png", Resources.RIGHT.getSharedResourceKey());
		mountSharedResource("/images/occupied.png", Resources.OCCUPIED.getSharedResourceKey());
		mountSharedResource("/images/paid.png", Resources.PAID.getSharedResourceKey());
		mountSharedResource("/images/unpaid.png", Resources.UNPAID.getSharedResourceKey());
		mountSharedResource("/images/toolbar.png", Resources.TOOLBAR.getSharedResourceKey());
		mountSharedResource("/images/toolbar_item.png", Resources.TOOLBAR_ITEM.getSharedResourceKey());
		mountSharedResource("/images/toolbar_item_hover.png", Resources.TOOLBAR_ITEM_HOVER.getSharedResourceKey());
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

	public MailBean getMailBean() {
		return (MailBean) getApplicationContext().getBean("mailBean"); //$NON-NLS-1$
	}
	
	protected ApplicationContext getApplicationContext() {
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
				new Locale("en", "US"), //$NON-NLS-1$ //$NON-NLS-2$
				new Locale("en", "US", "singers"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				new Locale("da"), //$NON-NLS-1$
				new Locale("da", "DK", "singers") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		sb.append("://"); //$NON-NLS-1$
		sb.append(httpServletRequest.getServerName());
		if (httpServletRequest.getServerPort() != 80) {
			sb.append(":"); //$NON-NLS-1$
			sb.append(httpServletRequest.getServerPort());
		}
		sb.append("/"); //$NON-NLS-1$
		sb.append(httpServletRequest.getContextPath());
		
		return sb.toString();
	}
}
