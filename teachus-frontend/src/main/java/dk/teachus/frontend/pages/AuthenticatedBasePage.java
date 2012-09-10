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
import dk.teachus.frontend.pages.stats.admin.TeachersSummaryPage;
import dk.teachus.frontend.pages.stats.teacher.IncomePerMonthPage;

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
	protected List<MenuItem> createMenuItems() {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		
		if (UserLevel.ADMIN.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(AdminsPage.class, teachUsSession.getString("General.administrators"), AuthenticatedPageCategory.ADMINS)); //$NON-NLS-1$
			menuItemsList.add(new MenuItem(TeachersPage.class, teachUsSession.getString("General.teachers"), AuthenticatedPageCategory.TEACHERS)); //$NON-NLS-1$
			menuItemsList.add(new MenuItem(TeachersSummaryPage.class, teachUsSession.getString("General.statistics"), AuthenticatedPageCategory.STATISTICS)); //$NON-NLS-1$
			menuItemsList.add(new MenuItem(ApplicationConfigurationPage.class, teachUsSession.getString("General.globalConfiguration"), AuthenticatedPageCategory.GLOBAL_CONFIGURATION)); //$NON-NLS-1$
		}
		if (UserLevel.ADMIN != teachUsSession.getUserLevel()) {
			if (UserLevel.TEACHER.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PupilsPage.class, teachUsSession.getString("General.pupils"), AuthenticatedPageCategory.PUPILS)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(SentMessagesPage.class, teachUsSession.getString("General.messages"), AuthenticatedPageCategory.MESSAGES)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(TeacherSettingsPage.class, teachUsSession.getString("General.settings"), AuthenticatedPageCategory.SETTINGS)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(PeriodsPage.class, teachUsSession.getString("General.periods"), AuthenticatedPageCategory.PERIODS)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(AgendaPage.class, teachUsSession.getString("General.agenda"), AuthenticatedPageCategory.AGENDA)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(IncomePerMonthPage.class, teachUsSession.getString("General.statistics"), AuthenticatedPageCategory.STATISTICS)); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(PupilCalendarPage.class, teachUsSession.getString("General.calendar"), AuthenticatedPageCategory.CALENDAR)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(PupilPage.class, teachUsSession.getString("General.settings"), AuthenticatedPageCategory.SETTINGS)); //$NON-NLS-1$
			} else if (UserLevel.TEACHER == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(TeacherCalendarPage.class, teachUsSession.getString("General.calendar"), AuthenticatedPageCategory.CALENDAR)); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PaymentPage.class, teachUsSession.getString("General.payment"), AuthenticatedPageCategory.PAYMENT)); //$NON-NLS-1$
			}
		}
		if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(SignOutPage.class, teachUsSession.getString("AuthenticatedBasePage.signOut"), AuthenticatedPageCategory.SIGNOUT)); //$NON-NLS-1$
		}
		
		return menuItemsList;
	}

	@Override
	protected String getPagePath() {
		return getRequestCycle().urlFor(this.getClass(), null).toString();
	}

	@Override
	protected abstract AuthenticatedPageCategory getPageCategory();
	
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
