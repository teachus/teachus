package dk.frankbille.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;
import wicket.protocol.http.WebApplication;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.pages.periods.PeriodsPage;
import dk.frankbille.teachus.frontend.pages.persons.AdminPage;
import dk.frankbille.teachus.frontend.pages.persons.AdminsPage;
import dk.frankbille.teachus.frontend.pages.persons.PupilPage;
import dk.frankbille.teachus.frontend.pages.persons.PupilsPage;
import dk.frankbille.teachus.frontend.pages.persons.TeacherPage;
import dk.frankbille.teachus.frontend.pages.persons.TeachersPage;
import dk.frankbille.teachus.frontend.pages.stats.StatsPage;
import dk.frankbille.teachus.frontend.utils.Resources;

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
		
		final Person person = teachUsSession.getPerson();
		Link personLink = new Link("personLink") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				if (person instanceof Pupil) {
					getRequestCycle().setResponsePage(new PupilPage((Pupil) person));
				} else if (person instanceof Teacher) {
					getRequestCycle().setResponsePage(new TeacherPage((Teacher) person));
				} else if (person instanceof Admin) {
					getRequestCycle().setResponsePage(new AdminPage((Admin) person));
				}
			}			
		};
		add(personLink);
		personLink.add(new Label("personName", person.getName()).setRenderBodyOnly(true)); //$NON-NLS-1$
		
		Label menuHelp = new Label("menuHelp"); //$NON-NLS-1$
		menuHelp.setOutputMarkupId(true);
		add(menuHelp);
		
		RepeatingView menuItems = new RepeatingView("menuLink"); //$NON-NLS-1$
		add(menuItems);
		
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		
		if (UserLevel.ADMIN.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(AdminsPage.class, Resources.ADMIN_SMALL, TeachUsSession.get().getString("General.administrators"))); //$NON-NLS-1$
			menuItemsList.add(new MenuItem(TeachersPage.class, Resources.TEACHER_SMALL, TeachUsSession.get().getString("General.teachers"))); //$NON-NLS-1$
		}
		if (UserLevel.ADMIN != teachUsSession.getUserLevel()) {
			if (UserLevel.TEACHER.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PupilsPage.class, Resources.PUPIL_SMALL, TeachUsSession.get().getString("General.pupils"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(TeacherSettingsPage.class, Resources.SETTINGS_SMALL, TeachUsSession.get().getString("General.settings"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(PeriodsPage.class, Resources.PERIOD_SMALL, TeachUsSession.get().getString("General.periods"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(AgendaPage.class, Resources.AGENDA_SMALL, TeachUsSession.get().getString("General.agenda"))); //$NON-NLS-1$
				menuItemsList.add(new MenuItem(StatsPage.class, Resources.STATS_SMALL, "Statistik")); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
				menuItemsList.add(new MenuItem(PaymentPage.class, Resources.PAYMENT2_SMALL, TeachUsSession.get().getString("General.payment"))); //$NON-NLS-1$
			}
			if (UserLevel.PUPIL == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(PupilCalendarPage.class, Resources.CALENDAR_SMALL, TeachUsSession.get().getString("General.calendar"))); //$NON-NLS-1$
			} else if (UserLevel.TEACHER == teachUsSession.getUserLevel()) {
				menuItemsList.add(new MenuItem(TeacherCalendarPage.class, Resources.CALENDAR_SMALL, TeachUsSession.get().getString("General.calendar"))); //$NON-NLS-1$
			}
		}
		if (UserLevel.PUPIL.authorized(teachUsSession.getUserLevel())) {
			menuItemsList.add(new MenuItem(SignOutPage.class, Resources.SIGNOUT_SMALL, TeachUsSession.get().getString("AuthenticatedBasePage.signOut"))); //$NON-NLS-1$
		}
		
		for (MenuItem menuItem : menuItemsList) {
			Link menuLink = new BookmarkablePageLink(menuItems.newChildId(), menuItem.getBookmarkablePage());
			menuItems.add(menuLink);
			menuLink.add(new Image("menuIcon", menuItem.getIcon())); //$NON-NLS-1$
			
			menuLink.add(new SimpleAttributeModifier("onmouseover", "$('"+menuHelp.getMarkupId()+"').replace('<span id=\\'"+menuHelp.getMarkupId()+"\\'>"+menuItem.getHelpText()+"</span>')")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			menuLink.add(new SimpleAttributeModifier("onmouseout", "$('"+menuHelp.getMarkupId()+"').hide()"));			 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
	
	@Override
	protected final void onAttach() {
		if (attached == false) {
			add(new Image("pageIcon", getPageIcon())); //$NON-NLS-1$
			add(new Label("pageLabel", getPageLabel())); //$NON-NLS-1$
			attached = true;
		}
		
		onAttach2();
	}
	
	protected void onAttach2() {
	}
	
	protected abstract String getPageLabel();
	
	protected abstract ResourceReference getPageIcon();
	
}
