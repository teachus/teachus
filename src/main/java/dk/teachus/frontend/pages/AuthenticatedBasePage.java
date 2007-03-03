package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;

import wicket.RestartResponseAtInterceptPageException;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;
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
import dk.teachus.frontend.utils.Resources;

public abstract class AuthenticatedBasePage extends BasePage {
	private boolean attached = false;
	
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
		
		List<MenuItem> menuItemsList = createMenuItems();		
		
		RepeatingView menuItems = new RepeatingView("menuItems"); //$NON-NLS-1$
		add(menuItems);
		
		for (MenuItem menuItem : menuItemsList) {
			WebMarkupContainer menuItemContainer = new WebMarkupContainer(menuItems.newChildId());
			menuItems.add(menuItemContainer);
			
			Link menuLink = new BookmarkablePageLink("menuLink", menuItem.getBookmarkablePage());
			menuItemContainer.add(menuLink);
			if (menuItem.getBookmarkablePage().isInstance(this)) {
				menuLink.add(new SimpleAttributeModifier("class", "current"));
			}
			
			menuLink.add(new Label("menuLabel", menuItem.getHelpText()));
		}
		
		
		
		add(new Label("copyright", "2006-"+new DateMidnight().getYear()+" TeachUs Booking Systems"));
	}

	private List<MenuItem> createMenuItems() {
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		
		if (UserLevel.ADMIN.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(AdminsPage.class, Resources.ADMIN_SMALL, teachUsSession.getString("General.administrators"))); //$NON-NLS-1$
			menuItemsList.add(new MenuItem(TeachersPage.class, Resources.TEACHER_SMALL, teachUsSession.getString("General.teachers"))); //$NON-NLS-1$
		}
		if (UserLevel.ADMIN != teachUsSession.getUserLevel()) {
			if (UserLevel.TEACHER.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PupilsPage.class, Resources.PUPIL_SMALL, teachUsSession.getString("General.pupils"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(TeacherSettingsPage.class, Resources.SETTINGS_SMALL, teachUsSession.getString("General.settings"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(PeriodsPage.class, Resources.PERIOD_SMALL, teachUsSession.getString("General.periods"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(AgendaPage.class, Resources.AGENDA_SMALL, teachUsSession.getString("General.agenda"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(StatsPage.class, Resources.STATS_SMALL, "Statistik")); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PaymentPage.class, Resources.PAYMENT2_SMALL, teachUsSession.getString("General.payment"))); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(PupilCalendarPage.class, Resources.CALENDAR_SMALL, teachUsSession.getString("General.calendar"))); //$NON-NLS-1$
			} else if (UserLevel.TEACHER == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(TeacherCalendarPage.class, Resources.CALENDAR_SMALL, teachUsSession.getString("General.calendar"))); //$NON-NLS-1$
			}
		}
		if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(SignOutPage.class, Resources.SIGNOUT_SMALL, teachUsSession.getString("AuthenticatedBasePage.signOut"))); //$NON-NLS-1$
		}
		return menuItemsList;
	}
	
	@Override
	protected final void onAttach() {
		if (attached == false) {
			add(new Label("pageLabel", getPageLabel())); //$NON-NLS-1$
			attached = true;
		}
		
		onAttach2();
	}
	
	protected void onAttach2() {
	}
	
	protected abstract String getPageLabel();
	
}
