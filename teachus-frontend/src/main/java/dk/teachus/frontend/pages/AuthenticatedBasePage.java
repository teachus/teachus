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
package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;

import com.newrelic.api.agent.NewRelic;

import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.menu.MenuItem;
import dk.teachus.frontend.components.menu.MenuItemContainer;
import dk.teachus.frontend.components.menu.MenuItemPageLink;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;
import dk.teachus.frontend.pages.calendar.TeacherCalendarPage;
import dk.teachus.frontend.pages.messages.SentMessagesPage;
import dk.teachus.frontend.pages.periods.PeriodsPage;
import dk.teachus.frontend.pages.persons.AdminsPage;
import dk.teachus.frontend.pages.persons.PupilPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;
import dk.teachus.frontend.pages.settings.ApplicationConfigurationPage;
import dk.teachus.frontend.pages.settings.TeacherSettingsPage;
import dk.teachus.frontend.pages.stats.admin.TeachersLogPage;
import dk.teachus.frontend.pages.stats.admin.TeachersSummaryPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerMonthPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerPupilPage;

public abstract class AuthenticatedBasePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public static enum AuthenticatedPageCategory implements PageCategory {
		ADMINS,
		TEACHERS,
		PUPILS,
		MESSAGES,
		AGENDA,
		SETTINGS,
		GLOBAL_CONFIGURATION,
		PERIODS,
		STATISTICS,
		PAYMENT,
		CALENDAR,
		SIGNOUT
	}
	
	public AuthenticatedBasePage(UserLevel userLevel) {
		this(userLevel, false);
	}
		
	public AuthenticatedBasePage(UserLevel userLevel, boolean explicitUserLevel) {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		if (userLevel.authorized(teachUsSession.getUserLevel()) == false) {
			throw new RestartResponseAtInterceptPageException(Application.get().getHomePage());
		}
		
		// If the userlevel is explicit, then only allow users of the same userlevel
		// on this page.
		if (explicitUserLevel) {
			if (teachUsSession.getUserLevel() != userLevel) {
				throw new RestartResponseAtInterceptPageException(Application.get().getHomePage());
			}
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Person person = TeachUsSession.get().getPerson();
		if (person instanceof Teacher) {
			NewRelic.addCustomParameter("teacher", person.getUsername());
		} else if (person instanceof Admin) {
			NewRelic.addCustomParameter("admin", person.getUsername());
		} else if (person instanceof Pupil) {
			NewRelic.addCustomParameter("pupil", person.getUsername());
		}
	}

	@Override
	public List<MenuItem> getMenuItems() {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		
		if (UserLevel.ADMIN.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.ADMINS, teachUsSession.getString("General.administrators"), AdminsPage.class));
			menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.TEACHERS, teachUsSession.getString("General.teachers"), TeachersPage.class));
			MenuItemContainer statItem = new MenuItemContainer(AuthenticatedPageCategory.STATISTICS, teachUsSession.getString("General.statistics"));
			statItem.addSubMenuItem(new MenuItemPageLink(AuthenticatedPageCategory.STATISTICS, teachUsSession.getString("General.teachersSummary"), TeachersSummaryPage.class));
			statItem.addSubMenuItem(new MenuItemPageLink(AuthenticatedPageCategory.STATISTICS, teachUsSession.getString("General.teachersLog"), TeachersLogPage.class));
			menuItemsList.add(statItem);
			menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.GLOBAL_CONFIGURATION, teachUsSession.getString("General.globalConfiguration"), ApplicationConfigurationPage.class));
		}
		if (UserLevel.ADMIN != teachUsSession.getUserLevel()) {
			if (UserLevel.TEACHER.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.PUPILS, teachUsSession.getString("General.pupils"), PupilsPage.class));
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.MESSAGES, teachUsSession.getString("General.messages"), SentMessagesPage.class));
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.SETTINGS, teachUsSession.getString("General.settings"), TeacherSettingsPage.class));
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.PERIODS, teachUsSession.getString("General.periods"), PeriodsPage.class));
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.AGENDA, teachUsSession.getString("General.agenda"), AgendaPage.class));
				MenuItemContainer statItem = new MenuItemContainer(AuthenticatedPageCategory.STATISTICS, teachUsSession.getString("General.statistics"));
				statItem.addSubMenuItem(new MenuItemPageLink(AuthenticatedPageCategory.STATISTICS, teachUsSession.getString("General.incomePerPupil"), IncomePerPupilPage.class));
				statItem.addSubMenuItem(new MenuItemPageLink(AuthenticatedPageCategory.STATISTICS, teachUsSession.getString("General.incomePerMonth"), IncomePerMonthPage.class));
				menuItemsList.add(statItem);
			}
			if (UserLevel.PUPIL == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.CALENDAR, teachUsSession.getString("General.calendar"), PupilCalendarPage.class));
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.SETTINGS, teachUsSession.getString("General.settings"), PupilPage.class));
			} else if (UserLevel.TEACHER == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.CALENDAR, teachUsSession.getString("General.calendar"), TeacherCalendarPage.class));
			}
			if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItemPageLink(AuthenticatedPageCategory.PAYMENT, teachUsSession.getString("General.payment"), PaymentPage.class));
			}
		}
		
		return menuItemsList;
	}
	
	@Override
	public List<MenuItem> getRightMenuItems() {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		
		if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
			MenuItemPageLink signOutItem = new MenuItemPageLink(AuthenticatedPageCategory.SIGNOUT, teachUsSession.getString("AuthenticatedBasePage.signOut"), SignOutPage.class);
			signOutItem.setIcon("signout");
			menuItemsList.add(signOutItem);
		}
		
		return menuItemsList;
	}

	@Override
	protected String getPagePath() {
		return getRequestCycle().urlFor(this.getClass(), null).toString();
	}

	@Override
	public
	abstract AuthenticatedPageCategory getPageCategory();
	
	@Override
	protected Theme getTheme() {
		TeachUsSession teachUsSession = TeachUsSession.get();
		Theme theme = teachUsSession.getPerson().getTheme();
		
		if (theme == null) {
			if (teachUsSession.getPerson() instanceof Pupil) {
				Pupil pupil = (Pupil) teachUsSession.getPerson();
				theme = pupil.getTeacher().getTheme();
			}
		}
		
		if (theme == null) {
			theme = super.getTheme();
		}
		
		return theme;
	}
	
	
}
