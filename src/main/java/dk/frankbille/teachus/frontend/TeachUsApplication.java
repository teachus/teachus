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
import dk.frankbille.teachus.frontend.pages.stats.IncomePerPupilPage;
import dk.frankbille.teachus.frontend.pages.stats.StatsPage;
import dk.frankbille.teachus.utils.ApplicationUtils;

public class TeachUsApplication extends WebApplication {
	
	@Override
	protected void init() {
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
		mount("/stats", new IndexedParamUrlCodingStrategy("/stats", StatsPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
		mount("/stats/incomeperpupil", new IndexedParamUrlCodingStrategy("/stats/incomeperpupil", IncomePerPupilPage.class)); //$NON-NLS-1$ //$NON-NLS-2$
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
