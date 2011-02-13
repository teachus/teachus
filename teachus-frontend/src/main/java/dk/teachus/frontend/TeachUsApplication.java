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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.markup.html.AjaxServerAndClientTimeFilter;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.util.io.IObjectStreamFactory;
import org.apache.wicket.util.lang.Objects;
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
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.components.fancybox.JQueryFancyboxBehavior;
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
	
	@Override
	protected void init() {
		Objects.setObjectStreamFactory(new IObjectStreamFactory.DefaultObjectStreamFactory());
		
		// Settings
		CompoundAuthorizationStrategy authorizationStrategy = new CompoundAuthorizationStrategy();
		authorizationStrategy.add(new TeachUsCookieAuthentication());
		authorizationStrategy.add(new TeachUsAuthentication());
		getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
		getApplicationSettings().setPageExpiredErrorPage(PageExpiredPage.class);
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		
		getResourceSettings().setJavascriptCompressor(null);
		
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
		mount(new HybridUrlCodingStrategy("/calendar", PupilCalendarPage.class));
		mount(new HybridUrlCodingStrategy("/calendar/teacher", TeacherCalendarPage.class));
		mount(new HybridUrlCodingStrategy("/pupils", PupilsPage.class));
		mount(new HybridUrlCodingStrategy("/teachers", TeachersPage.class));
		mountBookmarkablePage("/settings/profile", TeacherSettingsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/settings/welcomemail", WelcomeMailSettingsPage.class); //$NON-NLS-1$
		mount(new HybridUrlCodingStrategy("/admins", AdminsPage.class));
		mountBookmarkablePage("/periods", PeriodsPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/agenda", AgendaPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/payment", PaymentPage.class); //$NON-NLS-1$
		mount(new HybridUrlCodingStrategy("/stats/incomeperpupil", IncomePerPupilPage.class));
		mount(new IndexedParamUrlCodingStrategy("/stats/incomepermonth", IncomePerMonthPage.class)); //$NON-NLS-1$
		mount(new IndexedParamUrlCodingStrategy("/stats/lessonsperhour", LessonsPerHourPage.class)); //$NON-NLS-1$
		mountBookmarkablePage("/stats/teacherssummary", TeachersSummaryPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/stats/teacherslog", TeachersLogPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/info", InfoPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/globalsettings", ApplicationConfigurationPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/messages", SentMessagesPage.class); //$NON-NLS-1$
		mountBookmarkablePage("/messages/create", CreateMessagePage.class); //$NON-NLS-1$
		
		mount(new IndexedParamUrlCodingStrategy("/ical", IcalPage.class)); //$NON-NLS-1$
	}

	private void mountResources() {
		mountSharedResource("/images/screenshots/2.jpg", Resources.SCREENSHOT_2.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/2_thumb.jpg", Resources.SCREENSHOT_2_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/4.jpg", Resources.SCREENSHOT_4.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/4_thumb.jpg", Resources.SCREENSHOT_4_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/7.jpg", Resources.SCREENSHOT_7.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/7_thumb.jpg", Resources.SCREENSHOT_7_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/8.jpg", Resources.SCREENSHOT_8.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/8_thumb.jpg", Resources.SCREENSHOT_8_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/9.jpg", Resources.SCREENSHOT_9.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/9_thumb.jpg", Resources.SCREENSHOT_9_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/10.jpg", Resources.SCREENSHOT_10.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/10_thumb.jpg", Resources.SCREENSHOT_10_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/11.jpg", Resources.SCREENSHOT_11.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/11_thumb.jpg", Resources.SCREENSHOT_11_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/12.jpg", Resources.SCREENSHOT_12.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/12_thumb.jpg", Resources.SCREENSHOT_12_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/13.jpg", Resources.SCREENSHOT_13.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/13_thumb.jpg", Resources.SCREENSHOT_13_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/14.jpg", Resources.SCREENSHOT_14.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/screenshots/14_thumb.jpg", Resources.SCREENSHOT_14_THUMB.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/available.png", Resources.AVAILABLE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/available_hover.png", Resources.AVAILABLE_HOVER.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/booked.png", Resources.BOOKED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/booked_hover.png", Resources.BOOKED_HOVER.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/left.png", Resources.LEFT.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/right.png", Resources.RIGHT.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/occupied.png", Resources.OCCUPIED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/paid.png", Resources.PAID.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/unpaid.png", Resources.UNPAID.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/toolbar.png", Resources.TOOLBAR.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/list_header_back.png", Resources.LIST_HEADER.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/empty.gif", Resources.EMPTY.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/calendar.png", Resources.ICON_CALENDAR.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/email_new_password.png", Resources.ICON_EMAIL_NEW_PASSWORD.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/delete.png", Resources.ICON_DELETE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/username.png", Resources.ICON_USERNAME.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/password.png", Resources.ICON_PASSWORD.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/bodybg.jpg", Resources.ANDREAS09_BODYBG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/footerbg.jpg", Resources.ANDREAS09_FOOTERBG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/menuhover.jpg", Resources.ANDREAS09_MENUHOVER.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/bodybg-red.jpg", Resources.ANDREAS09_BODYBG_RED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/menuhover-red.jpg", Resources.ANDREAS09_MENUHOVER_RED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/bodybg-black.jpg", Resources.ANDREAS09_BODYBG_BLACK.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/menuhover-black.jpg", Resources.ANDREAS09_MENUHOVER_BLACK.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/bodybg-green.jpg", Resources.ANDREAS09_BODYBG_GREEN.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/menuhover-green.jpg", Resources.ANDREAS09_MENUHOVER_GREEN.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/bodybg-orange.jpg", Resources.ANDREAS09_BODYBG_ORANGE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/menuhover-orange.jpg", Resources.ANDREAS09_MENUHOVER_ORANGE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/bodybg-purple.jpg", Resources.ANDREAS09_BODYBG_PURPLE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/menuhover-purple.jpg", Resources.ANDREAS09_MENUHOVER_PURPLE.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/list_header_back_red.png", Resources.LIST_HEADER_RED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/toolbar_red.png", Resources.TOOLBAR_RED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/list_header_back_orange.png", Resources.LIST_HEADER_ORANGE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/toolbar_orange.png", Resources.TOOLBAR_ORANGE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/list_header_back_black.png", Resources.LIST_HEADER_BLACK.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/toolbar_black.png", Resources.TOOLBAR_BLACK.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/list_header_back_green.png", Resources.LIST_HEADER_GREEN.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/toolbar_green.png", Resources.TOOLBAR_GREEN.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/list_header_back_purple.png", Resources.LIST_HEADER_PURPLE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/toolbar_purple.png", Resources.TOOLBAR_PURPLE.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/loading.gif", JFreeChartImage.LOADING_INDICATOR.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/loading_dots.gif", Resources.DOT_INDICATOR.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/asc.png", Resources.ASC.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/desc.png", Resources.DESC.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/fancybox/fancybox.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCYBOX_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/blank.gif", JQueryFancyboxBehavior.IMG_FANCYBOX_BLANK_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancybox-x.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCYBOX_X_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancybox-y.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCYBOX_Y_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-title-main.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_TITLE_MAIN_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-title-over.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_TITLE_OVER_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-title-left.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_TITLE_LEFT_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-title-right.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_TITLE_RIGHT_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-close.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_CLOSE_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-loading.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_LOADING_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-nav-left.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_NAV_LEFT_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-nav-right.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_NAV_RIGHT_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-e.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_E_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-n.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_N_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-ne.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_NE_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-nw.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_NW_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-s.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_S_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-se.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_SE_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-sw.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_SW_PNG.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/fancybox/fancy-shadow-w.png", JQueryFancyboxBehavior.IMG_FANCYBOX_FANCY_SHADOW_W_PNG.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/images/cluetip/arrowdown.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWDOWN_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/arrowleft.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWLEFT_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/arrowright.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWRIGHT_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/arrowup.gif", JQueryCluetipBehavior.IMG_CLUETIP_ARROWUP_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/darrowdown.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWDOWN_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/darrowleft.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWLEFT_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/darrowright.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWRIGHT_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/darrowup.gif", JQueryCluetipBehavior.IMG_CLUETIP_DARROWUP_GIF.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/images/cluetip/wait.gif", JQueryCluetipBehavior.IMG_CLUETIP_WAIT_GIF.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/css/andreas09.css", Resources.CSS_ANDREAS09.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/andreas09_black.css", Resources.CSS_ANDREAS09_BLACK.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/andreas09_green.css", Resources.CSS_ANDREAS09_GREEN.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/andreas09_orange.css", Resources.CSS_ANDREAS09_ORANGE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/andreas09_purple.css", Resources.CSS_ANDREAS09_PURPLE.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/andreas09_red.css", Resources.CSS_ANDREAS09_RED.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/screen.css", Resources.CSS_SCREEN.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/print.css", Resources.CSS_PRINT.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/css/jquery-cluetip.css", JQueryCluetipBehavior.CSS_CLUETIP_JQUERY.getSharedResourceKey()); //$NON-NLS-1$
		
		mountSharedResource("/js/wicket-ajax.js", WicketAjaxReference.INSTANCE.getSharedResourceKey()); //$NON-NLS-1$ //$NON-NLS-2$
		mountSharedResource("/js/wicket-event.js", WicketEventReference.INSTANCE.getSharedResourceKey()); //$NON-NLS-1$ //$NON-NLS-2$
		mountSharedResource("/js/jquery.js", JQueryBehavior.JS_JQUERY.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/js/jquery-cluetip.js", JQueryCluetipBehavior.JS_CLUETIP_JQUERY.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/js/fancybox.js", JQueryFancyboxBehavior.JS_FANCYBOX.getSharedResourceKey()); //$NON-NLS-1$
		mountSharedResource("/js/calendar.js", CalendarPanel.JS_CALENDAR.getSharedResourceKey()); //$NON-NLS-1$
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
		return getConfiguration().getConfiguration(ApplicationConfiguration.VERSION);
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
			WebRequest request = (WebRequest) cycle.getRequest();
			HttpServletRequest httpServletRequest = request.getHttpServletRequest();
			
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
