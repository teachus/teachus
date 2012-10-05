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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.response.filter.AjaxServerAndClientTimeFilter;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.util.string.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.dao.StatisticsDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jquery.JQueryBehavior;
import dk.teachus.frontend.components.jquery.cluetip.JQueryCluetipBehavior;
import dk.teachus.frontend.ical.IcalPage;
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
import dk.teachus.frontend.pages.messages.CreateMessagePage;
import dk.teachus.frontend.pages.messages.SentMessagesPage;
import dk.teachus.frontend.pages.periods.PeriodsPage;
import dk.teachus.frontend.pages.persons.AdminsPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;
import dk.teachus.frontend.pages.settings.ApplicationConfigurationPage;
import dk.teachus.frontend.pages.settings.InvalidBookingsPage;
import dk.teachus.frontend.pages.settings.TeacherSettingsPage;
import dk.teachus.frontend.pages.settings.WelcomeMailSettingsPage;
import dk.teachus.frontend.pages.stats.admin.TeachersLogPage;
import dk.teachus.frontend.pages.stats.admin.TeachersSummaryPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerMonthPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerPupilPage;
import dk.teachus.frontend.pages.stats.teacher.LessonsPerHourPage;
import dk.teachus.frontend.utils.Resources;

public class TeachUsApplication extends WebApplication {
	
	private static final Log log = LogFactory.getLog(TeachUsApplication.class);
	
	private ApplicationConfiguration configuration;
	private String version;
	
	@Override
	protected void init() {
		// Settings
		CompoundAuthorizationStrategy authorizationStrategy = new CompoundAuthorizationStrategy();
		authorizationStrategy.add(new TeachUsCookieAuthentication());
		authorizationStrategy.add(new TeachUsAuthentication());
		getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
		getApplicationSettings().setPageExpiredErrorPage(PageExpiredPage.class);
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		
		if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
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
		mountPage("/signout", SignOutPage.class); //$NON-NLS-1$
		mountPage("/signedout", SignedOutPage.class); //$NON-NLS-1$
		mountPage("/calendar", PupilCalendarPage.class);
		mountPage("/calendar/teacher", TeacherCalendarPage.class);
		mountPage("/pupils", PupilsPage.class);
		mountPage("/teachers", TeachersPage.class);
		mountPage("/settings/profile", TeacherSettingsPage.class); //$NON-NLS-1$
		mountPage("/settings/welcomemail", WelcomeMailSettingsPage.class); //$NON-NLS-1$
		mountPage("/settings/invalidbookings", InvalidBookingsPage.class); //$NON-NLS-1$
		mountPage("/admins", AdminsPage.class);
		mountPage("/periods", PeriodsPage.class); //$NON-NLS-1$
		mountPage("/agenda", AgendaPage.class); //$NON-NLS-1$
		mountPage("/payment", PaymentPage.class); //$NON-NLS-1$
		mountPage("/stats/incomeperpupil", IncomePerPupilPage.class);
		mountPage("/stats/incomepermonth", IncomePerMonthPage.class); //$NON-NLS-1$
		mountPage("/stats/lessonsperhour", LessonsPerHourPage.class); //$NON-NLS-1$
		mountPage("/stats/teacherssummary", TeachersSummaryPage.class); //$NON-NLS-1$
		mountPage("/stats/teacherslog", TeachersLogPage.class); //$NON-NLS-1$
		mountPage("/info", InfoPage.class); //$NON-NLS-1$
		mountPage("/globalsettings", ApplicationConfigurationPage.class); //$NON-NLS-1$
		mountPage("/messages", SentMessagesPage.class); //$NON-NLS-1$
		mountPage("/messages/create", CreateMessagePage.class); //$NON-NLS-1$
		
		mountPage("/ical", IcalPage.class); //$NON-NLS-1$
	}

	private void mountResources() {		
		mountResource("/images/screenshots/2.jpg", Resources.SCREENSHOT_2); //$NON-NLS-1$
		mountResource("/images/screenshots/2_thumb.jpg", Resources.SCREENSHOT_2_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/4.jpg", Resources.SCREENSHOT_4); //$NON-NLS-1$
		mountResource("/images/screenshots/4_thumb.jpg", Resources.SCREENSHOT_4_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/7.jpg", Resources.SCREENSHOT_7); //$NON-NLS-1$
		mountResource("/images/screenshots/7_thumb.jpg", Resources.SCREENSHOT_7_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/8.jpg", Resources.SCREENSHOT_8); //$NON-NLS-1$
		mountResource("/images/screenshots/8_thumb.jpg", Resources.SCREENSHOT_8_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/9.jpg", Resources.SCREENSHOT_9); //$NON-NLS-1$
		mountResource("/images/screenshots/9_thumb.jpg", Resources.SCREENSHOT_9_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/10.jpg", Resources.SCREENSHOT_10); //$NON-NLS-1$
		mountResource("/images/screenshots/10_thumb.jpg", Resources.SCREENSHOT_10_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/11.jpg", Resources.SCREENSHOT_11); //$NON-NLS-1$
		mountResource("/images/screenshots/11_thumb.jpg", Resources.SCREENSHOT_11_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/12.jpg", Resources.SCREENSHOT_12); //$NON-NLS-1$
		mountResource("/images/screenshots/12_thumb.jpg", Resources.SCREENSHOT_12_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/13.jpg", Resources.SCREENSHOT_13); //$NON-NLS-1$
		mountResource("/images/screenshots/13_thumb.jpg", Resources.SCREENSHOT_13_THUMB); //$NON-NLS-1$
		mountResource("/images/screenshots/14.jpg", Resources.SCREENSHOT_14); //$NON-NLS-1$
		mountResource("/images/screenshots/14_thumb.jpg", Resources.SCREENSHOT_14_THUMB); //$NON-NLS-1$
		
		mountResource("/images/available.png", Resources.AVAILABLE); //$NON-NLS-1$
		mountResource("/images/available_hover.png", Resources.AVAILABLE_HOVER); //$NON-NLS-1$
		mountResource("/images/booked.png", Resources.BOOKED); //$NON-NLS-1$
		mountResource("/images/booked_hover.png", Resources.BOOKED_HOVER); //$NON-NLS-1$
		mountResource("/images/left.png", Resources.LEFT); //$NON-NLS-1$
		mountResource("/images/right.png", Resources.RIGHT); //$NON-NLS-1$
		mountResource("/images/occupied.png", Resources.OCCUPIED); //$NON-NLS-1$
		mountResource("/images/paid.png", Resources.PAID); //$NON-NLS-1$
		mountResource("/images/unpaid.png", Resources.UNPAID); //$NON-NLS-1$
		mountResource("/images/toolbar.png", Resources.TOOLBAR); //$NON-NLS-1$
		mountResource("/images/list_header_back.png", Resources.LIST_HEADER); //$NON-NLS-1$
		mountResource("/images/empty.gif", Resources.EMPTY); //$NON-NLS-1$
		mountResource("/images/calendar.png", Resources.ICON_CALENDAR); //$NON-NLS-1$
		mountResource("/images/email_new_password.png", Resources.ICON_EMAIL_NEW_PASSWORD); //$NON-NLS-1$
		mountResource("/images/delete.png", Resources.ICON_DELETE); //$NON-NLS-1$
		mountResource("/images/username.png", Resources.ICON_USERNAME); //$NON-NLS-1$
		mountResource("/images/password.png", Resources.ICON_PASSWORD); //$NON-NLS-1$
		
		mountResource("/images/bodybg.jpg", Resources.ANDREAS09_BODYBG); //$NON-NLS-1$
		mountResource("/images/footerbg.jpg", Resources.ANDREAS09_FOOTERBG); //$NON-NLS-1$
		mountResource("/images/menuhover.jpg", Resources.ANDREAS09_MENUHOVER); //$NON-NLS-1$
		
		mountResource("/images/bodybg-red.jpg", Resources.ANDREAS09_BODYBG_RED); //$NON-NLS-1$
		mountResource("/images/menuhover-red.jpg", Resources.ANDREAS09_MENUHOVER_RED); //$NON-NLS-1$
		mountResource("/images/bodybg-black.jpg", Resources.ANDREAS09_BODYBG_BLACK); //$NON-NLS-1$
		mountResource("/images/menuhover-black.jpg", Resources.ANDREAS09_MENUHOVER_BLACK); //$NON-NLS-1$
		mountResource("/images/bodybg-green.jpg", Resources.ANDREAS09_BODYBG_GREEN); //$NON-NLS-1$
		mountResource("/images/menuhover-green.jpg", Resources.ANDREAS09_MENUHOVER_GREEN); //$NON-NLS-1$
		mountResource("/images/bodybg-orange.jpg", Resources.ANDREAS09_BODYBG_ORANGE); //$NON-NLS-1$
		mountResource("/images/menuhover-orange.jpg", Resources.ANDREAS09_MENUHOVER_ORANGE); //$NON-NLS-1$
		mountResource("/images/bodybg-purple.jpg", Resources.ANDREAS09_BODYBG_PURPLE); //$NON-NLS-1$
		mountResource("/images/menuhover-purple.jpg", Resources.ANDREAS09_MENUHOVER_PURPLE); //$NON-NLS-1$
		
		mountResource("/images/list_header_back_red.png", Resources.LIST_HEADER_RED); //$NON-NLS-1$
		mountResource("/images/toolbar_red.png", Resources.TOOLBAR_RED); //$NON-NLS-1$
		mountResource("/images/list_header_back_orange.png", Resources.LIST_HEADER_ORANGE); //$NON-NLS-1$
		mountResource("/images/toolbar_orange.png", Resources.TOOLBAR_ORANGE); //$NON-NLS-1$
		mountResource("/images/list_header_back_black.png", Resources.LIST_HEADER_BLACK); //$NON-NLS-1$
		mountResource("/images/toolbar_black.png", Resources.TOOLBAR_BLACK); //$NON-NLS-1$
		mountResource("/images/list_header_back_green.png", Resources.LIST_HEADER_GREEN); //$NON-NLS-1$
		mountResource("/images/toolbar_green.png", Resources.TOOLBAR_GREEN); //$NON-NLS-1$
		mountResource("/images/list_header_back_purple.png", Resources.LIST_HEADER_PURPLE); //$NON-NLS-1$
		mountResource("/images/toolbar_purple.png", Resources.TOOLBAR_PURPLE); //$NON-NLS-1$
		
		mountResource("/images/loading.gif", JFreeChartImage.LOADING_INDICATOR); //$NON-NLS-1$
		mountResource("/images/loading_dots.gif", Resources.DOT_INDICATOR); //$NON-NLS-1$
		
		mountResource("/images/asc.png", Resources.ASC); //$NON-NLS-1$
		mountResource("/images/desc.png", Resources.DESC); //$NON-NLS-1$
		
		mountResource("/images/cluetip/arrowdown.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWDOWN_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/arrowleft.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWLEFT_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/arrowright.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWRIGHT_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/arrowup.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWUP_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/darrowdown.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWDOWN_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/darrowleft.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWLEFT_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/darrowright.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWRIGHT_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/darrowup.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWUP_GIF); //$NON-NLS-1$
		mountResource("/images/cluetip/wait.gif", JQueryCluetipBehavior.IMG_CLUETIP_WAIT_GIF); //$NON-NLS-1$
		
		mountResource("/css/andreas09.css", Resources.CSS_ANDREAS09); //$NON-NLS-1$
		mountResource("/css/andreas09_black.css", Resources.CSS_ANDREAS09_BLACK); //$NON-NLS-1$
		mountResource("/css/andreas09_green.css", Resources.CSS_ANDREAS09_GREEN); //$NON-NLS-1$
		mountResource("/css/andreas09_orange.css", Resources.CSS_ANDREAS09_ORANGE); //$NON-NLS-1$
		mountResource("/css/andreas09_purple.css", Resources.CSS_ANDREAS09_PURPLE); //$NON-NLS-1$
		mountResource("/css/andreas09_red.css", Resources.CSS_ANDREAS09_RED); //$NON-NLS-1$
		mountResource("/css/screen.css", Resources.CSS_SCREEN); //$NON-NLS-1$
		mountResource("/css/print.css", Resources.CSS_PRINT); //$NON-NLS-1$
		mountResource("/css/jquery-cluetip.css", JQueryCluetipBehavior.CSS_CLUETIP_JQUERY); //$NON-NLS-1$
		
		mountResource("/js/wicket-ajax.js", WicketAjaxReference.INSTANCE); //$NON-NLS-1$ //$NON-NLS-2$
		mountResource("/js/wicket-event.js", WicketEventReference.INSTANCE); //$NON-NLS-1$ //$NON-NLS-2$
		mountResource("/js/jquery.js", JQueryBehavior.JS_JQUERY); //$NON-NLS-1$
		mountResource("/js/jquery-cluetip.js", JQueryCluetipBehavior.JS_CLUETIP_JQUERY); //$NON-NLS-1$
		mountResource("/js/calendar.js", CalendarPanel.JS_CALENDAR); //$NON-NLS-1$
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
		return (StatisticsDAO) getApplicationContext().getBean("statisticsDao"); //$NON-NLS-1$
	}
	
	protected ApplicationDAO getApplicationDAO() {
		return (ApplicationDAO) getApplicationContext().getBean("applicationDao"); //$NON-NLS-1$
	}
	
	public MessageDAO getMessageDAO() {
		return (MessageDAO) getApplicationContext().getBean("messageDao"); //$NON-NLS-1$
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
		if (version == null) {
			Package pkg = Teacher.class.getPackage();
			if (pkg != null) {
				version = pkg.getImplementationVersion();
			}
	
			if (version == null) {
				// When running from a project in development mode
				File pomFile = new File("pom.xml");
				if (pomFile.exists() && pomFile.isFile()) {
					try {
						BufferedReader reader = new BufferedReader(new FileReader(pomFile));
						String line = null;
						while ((line = reader.readLine()) != null && line.contains("<version>") == false) {
						}
						reader.close();
						
						if (line != null) {
							Pattern pattern = Pattern.compile(".*<version>([^<]+)</version>.*");
							Matcher matcher = pattern.matcher(line);
							if (matcher.matches()) {
								version = matcher.group(1);
							}
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		
		return version;
	}
	
	public String getServerUrl() {
		String serverUrl = getConfiguration().getConfiguration(ApplicationConfiguration.SERVER_URL);
		
		/*
		 * If the server URL is empty, then the administrator have misconfigured the system (forgot to set the
		 * server URL in the settings). We will get the server URL from the current server, but we will also
		 * warn the administrator by adding an entry to the log.
		 */
		if (Strings.isEmpty(serverUrl)) {
			log.error("No server url is set for the system. It's very important that you set it."); //$NON-NLS-1$
			
			RequestCycle cycle = RequestCycle.get();
			ServletWebRequest request = (ServletWebRequest) cycle.getRequest();
			HttpServletRequest httpServletRequest = request.getContainerRequest();
			
			StringBuilder b = new StringBuilder();
			b.append(httpServletRequest.getScheme()).append("://"); //$NON-NLS-1$
			b.append(httpServletRequest.getServerName());
			if (httpServletRequest.getServerPort() != 80 && httpServletRequest.getServerPort() != 443) {
				b.append(":").append(httpServletRequest.getServerPort()); //$NON-NLS-1$
			}
			
			serverUrl = b.toString();
		}
		
		return serverUrl;
	}

	public Theme getDefaultTheme() {
		return Theme.BLUE;
	}
}
