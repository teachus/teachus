package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import wicket.RestartResponseAtInterceptPageException;
import wicket.protocol.http.WebApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;
import dk.teachus.frontend.pages.calendar.TeacherCalendarPage;
import dk.teachus.frontend.pages.periods.PeriodsPage;
import dk.teachus.frontend.pages.persons.AdminsPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;
import dk.teachus.frontend.pages.stats.StatsPage;

public abstract class AuthenticatedBasePage extends BasePage {
	
	public AuthenticatedBasePage(UserLevel userLevel) {
		this(userLevel, false);
	}
		
	public AuthenticatedBasePage(UserLevel userLevel, boolean explicitUserLevel) {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		if (userLevel.authorized(teachUsSession.getUserLevel()) == false) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
		
		// If the userlevel is explicit, then only allow users of the same userlevel
		// on this page.
		if (explicitUserLevel) {
			if (teachUsSession.getUserLevel() != userLevel) {
				throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
			}
		}

	}

	@Override
	protected List<MenuItem> createMenuItems() {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		
		if (UserLevel.ADMIN.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(AdminsPage.class, teachUsSession.getString("General.administrators"), PageCategory.ADMINS)); //$NON-NLS-1$
			menuItemsList.add(new MenuItem(TeachersPage.class, teachUsSession.getString("General.teachers"), PageCategory.TEACHERS)); //$NON-NLS-1$
		}
		if (UserLevel.ADMIN != teachUsSession.getUserLevel()) {
			if (UserLevel.TEACHER.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PupilsPage.class, teachUsSession.getString("General.pupils"), PageCategory.PUPILS)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(TeacherSettingsPage.class, teachUsSession.getString("General.settings"), PageCategory.SETTINGS)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(PeriodsPage.class, teachUsSession.getString("General.periods"), PageCategory.PERIODS)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(AgendaPage.class, teachUsSession.getString("General.agenda"), PageCategory.AGENDA)); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(StatsPage.class, "Statistik", PageCategory.STATISTICS)); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PaymentPage.class, teachUsSession.getString("General.payment"), PageCategory.PAYMENT)); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(PupilCalendarPage.class, teachUsSession.getString("General.calendar"), PageCategory.CALENDAR)); //$NON-NLS-1$
			} else if (UserLevel.TEACHER == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(TeacherCalendarPage.class, teachUsSession.getString("General.calendar"), PageCategory.CALENDAR)); //$NON-NLS-1$
			}
		}
		if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(SignOutPage.class, teachUsSession.getString("AuthenticatedBasePage.signOut"), PageCategory.SIGNOUT)); //$NON-NLS-1$
		}
		
		return menuItemsList;
	}
	
}
