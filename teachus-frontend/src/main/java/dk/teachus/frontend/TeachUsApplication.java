/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.markup.html.AjaxServerAndClientTimeFilter;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.util.io.IObjectStreamFactory;
import org.apache.wicket.util.lang.Objects;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.dao.StatisticsDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.components.imagebox.ImageBox;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jquery.JQueryBehavior;
import dk.teachus.frontend.pages.AgendaPage;
import dk.teachus.frontend.pages.HomePage;
import dk.teachus.frontend.pages.InfoPage;
import dk.teachus.frontend.pages.InternalErrorPage;
import dk.teachus.frontend.pages.PageExpiredPage;
import dk.teachus.frontend.pages.PaymentPage;
import dk.teachus.frontend.pages.SignOutPage;
import dk.teachus.frontend.pages.SignedOutPage;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;
import dk.teachus.frontend.pages.calendar.TeacherCalendarPage;
import dk.teachus.frontend.pages.periods.PeriodsPage;
import dk.teachus.frontend.pages.persons.AdminsPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;
import dk.teachus.frontend.pages.settings.ApplicationConfigurationPage;
import dk.teachus.frontend.pages.settings.TeacherSettingsPage;
import dk.teachus.frontend.pages.settings.WelcomeMailSettingsPage;
import dk.teachus.frontend.pages.stats.admin.TeachersSummaryPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerMonthPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerPupilPage;
import dk.teachus.frontend.pages.stats.teacher.LessonsPerHourPage;
import dk.teachus.frontend.utils.Resources;

public class TeachUsApplication extends WebApplication {
	
	private ApplicationConfiguration configuration;
	
	@Override
	protected void init() {
		Objects.setObjectStreamFactory(new IObjectStreamFactory.DefaultObjectStreamFactory());
		
		// Settings
		getSecuritySettings().setAuthorizationStrategy(new TeachUsAuthentication());
		getApplicationSettings().setPageExpiredErrorPage(PageExpiredPage.class);
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		
		getResourceSettings().setStripJavascriptCommentsAndWhitespace(false);
		
		if (DEVELOPMENT.equals(getConfigurationType())) {
			getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());
		}
		
		loadConfiguration();
		
		mountPages();
		
		mountResources();
	}
	
	private void loadConfiguration() {
		configuration = getApplicationDAO().loadConfiguration();
		configuration.addPropertyListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				getApplicationDAO().saveConfiguration(configuration);
			}
		});
	}

	private void mountPages() {
		mountBookmarkablePage("/signout", SignOutPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/signedout", SignedOutPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/calendar", PupilCalendarPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/calendar/teacher", TeacherCalendarPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/pupils", PupilsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/teachers", TeachersPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/settings/profile", TeacherSettingsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/settings/welcomemail", WelcomeMailSettingsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/admins", AdminsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/periods", PeriodsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/agenda", AgendaPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/payment", PaymentPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/stats/incomeperpupil", IncomePerPupilPage.class); //$NON-NLS-1$
		mount(new IndexedParamUrlCodingStrategy("/stats/incomepermonth", IncomePerMonthPage.class));
		mount(new IndexedParamUrlCodingStrategy("/stats/lessonsperhour", LessonsPerHourPage.class));
		mountBookmarkablePage("/stats/teacherssummary", TeachersSummaryPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/info", InfoPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/globalsettings", ApplicationConfigurationPage.class); //$NON-NLS-1$
	}

	private void mountResources() {
		mountSharedResource("/images/screenshots/2.jpg", Resources.SCREENSHOT_2.getSharedResourceKey());
		mountSharedResource("/images/screenshots/2_thumb.jpg", Resources.SCREENSHOT_2_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/4.jpg", Resources.SCREENSHOT_4.getSharedResourceKey());
		mountSharedResource("/images/screenshots/4_thumb.jpg", Resources.SCREENSHOT_4_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/7.jpg", Resources.SCREENSHOT_7.getSharedResourceKey());
		mountSharedResource("/images/screenshots/7_thumb.jpg", Resources.SCREENSHOT_7_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/8.jpg", Resources.SCREENSHOT_8.getSharedResourceKey());
		mountSharedResource("/images/screenshots/8_thumb.jpg", Resources.SCREENSHOT_8_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/9.jpg", Resources.SCREENSHOT_9.getSharedResourceKey());
		mountSharedResource("/images/screenshots/9_thumb.jpg", Resources.SCREENSHOT_9_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/10.jpg", Resources.SCREENSHOT_10.getSharedResourceKey());
		mountSharedResource("/images/screenshots/10_thumb.jpg", Resources.SCREENSHOT_10_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/11.jpg", Resources.SCREENSHOT_11.getSharedResourceKey());
		mountSharedResource("/images/screenshots/11_thumb.jpg", Resources.SCREENSHOT_11_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/12.jpg", Resources.SCREENSHOT_12.getSharedResourceKey());
		mountSharedResource("/images/screenshots/12_thumb.jpg", Resources.SCREENSHOT_12_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/13.jpg", Resources.SCREENSHOT_13.getSharedResourceKey());
		mountSharedResource("/images/screenshots/13_thumb.jpg", Resources.SCREENSHOT_13_THUMB.getSharedResourceKey());
		mountSharedResource("/images/screenshots/14.jpg", Resources.SCREENSHOT_14.getSharedResourceKey());
		mountSharedResource("/images/screenshots/14_thumb.jpg", Resources.SCREENSHOT_14_THUMB.getSharedResourceKey());
		
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
		mountSharedResource("/images/list_header_back.png", Resources.LIST_HEADER.getSharedResourceKey());
		mountSharedResource("/images/empty.gif", Resources.EMPTY.getSharedResourceKey());
		
		mountSharedResource("/images/bodybg.jpg", Resources.ANDREAS09_BODYBG.getSharedResourceKey());
		mountSharedResource("/images/footerbg.jpg", Resources.ANDREAS09_FOOTERBG.getSharedResourceKey());
		mountSharedResource("/images/menuhover.jpg", Resources.ANDREAS09_MENUHOVER.getSharedResourceKey());
		
		mountSharedResource("/images/bodybg-red.jpg", Resources.ANDREAS09_BODYBG_RED.getSharedResourceKey());
		mountSharedResource("/images/menuhover-red.jpg", Resources.ANDREAS09_MENUHOVER_RED.getSharedResourceKey());
		mountSharedResource("/images/bodybg-black.jpg", Resources.ANDREAS09_BODYBG_BLACK.getSharedResourceKey());
		mountSharedResource("/images/menuhover-black.jpg", Resources.ANDREAS09_MENUHOVER_BLACK.getSharedResourceKey());
		mountSharedResource("/images/bodybg-green.jpg", Resources.ANDREAS09_BODYBG_GREEN.getSharedResourceKey());
		mountSharedResource("/images/menuhover-green.jpg", Resources.ANDREAS09_MENUHOVER_GREEN.getSharedResourceKey());
		mountSharedResource("/images/bodybg-orange.jpg", Resources.ANDREAS09_BODYBG_ORANGE.getSharedResourceKey());
		mountSharedResource("/images/menuhover-orange.jpg", Resources.ANDREAS09_MENUHOVER_ORANGE.getSharedResourceKey());
		mountSharedResource("/images/bodybg-purple.jpg", Resources.ANDREAS09_BODYBG_PURPLE.getSharedResourceKey());
		mountSharedResource("/images/menuhover-purple.jpg", Resources.ANDREAS09_MENUHOVER_PURPLE.getSharedResourceKey());
		
		mountSharedResource("/images/list_header_back_red.png", Resources.LIST_HEADER_RED.getSharedResourceKey());
		mountSharedResource("/images/toolbar_red.png", Resources.TOOLBAR_RED.getSharedResourceKey());
		mountSharedResource("/images/list_header_back_orange.png", Resources.LIST_HEADER_ORANGE.getSharedResourceKey());
		mountSharedResource("/images/toolbar_orange.png", Resources.TOOLBAR_ORANGE.getSharedResourceKey());
		mountSharedResource("/images/list_header_back_black.png", Resources.LIST_HEADER_BLACK.getSharedResourceKey());
		mountSharedResource("/images/toolbar_black.png", Resources.TOOLBAR_BLACK.getSharedResourceKey());
		mountSharedResource("/images/list_header_back_green.png", Resources.LIST_HEADER_GREEN.getSharedResourceKey());
		mountSharedResource("/images/toolbar_green.png", Resources.TOOLBAR_GREEN.getSharedResourceKey());
		mountSharedResource("/images/list_header_back_purple.png", Resources.LIST_HEADER_PURPLE.getSharedResourceKey());
		mountSharedResource("/images/toolbar_purple.png", Resources.TOOLBAR_PURPLE.getSharedResourceKey());
		
		mountSharedResource("/images/loading.gif", JFreeChartImage.LOADING_INDICATOR.getSharedResourceKey());
		mountSharedResource("/images/loading_dots.gif", Resources.DOT_INDICATOR.getSharedResourceKey());
		
		mountSharedResource("/images/asc.png", Resources.ASC.getSharedResourceKey());
		mountSharedResource("/images/desc.png", Resources.DESC.getSharedResourceKey());
		
		mountSharedResource("/css/andreas09.css", Resources.CSS_ANDREAS09.getSharedResourceKey());
		mountSharedResource("/css/screen.css", Resources.CSS_SCREEN.getSharedResourceKey());
		mountSharedResource("/css/print.css", Resources.CSS_PRINT.getSharedResourceKey());
		
		mountSharedResource("/js/wicket-ajax.js", new JavascriptResourceReference(AbstractDefaultAjaxBehavior.class, "wicket-ajax.js").getSharedResourceKey());
		mountSharedResource("/js/wicket-event.js", new JavascriptResourceReference(WicketEventReference.class, "wicket-event.js").getSharedResourceKey());
		mountSharedResource("/js/jquery.js", JQueryBehavior.JS_JQUERY.getSharedResourceKey());
		mountSharedResource("/js/iutil.js", ImageBox.JS_IUTIL_1_2.getSharedResourceKey());
		mountSharedResource("/js/imagebox.js", ImageBox.JS_IMAGEBOX_1_2.getSharedResourceKey());
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
	
	public StatisticsDAO getStatisticsDAO() {
		return (StatisticsDAO) getApplicationContext().getBean("statisticsDao");
	}
	
	protected ApplicationDAO getApplicationDAO() {
		return (ApplicationDAO) getApplicationContext().getBean("applicationDao");
	}
	
	public MessageDAO getMessageDAO() {
		return (MessageDAO) getApplicationContext().getBean("messageDao");
	}
	
	protected ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}
	
	public ApplicationConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}
	
	@Override
	public Session newSession(Request request, Response response) {
		return new TeachUsSession(request);
	}
	
	public static TeachUsApplication get() {
		return (TeachUsApplication) Application.get();
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
		return getConfiguration().getConfiguration(ApplicationConfiguration.VERSION);
	}

	public Theme getDefaultTheme() {
		return Theme.BLUE;
	}
}
