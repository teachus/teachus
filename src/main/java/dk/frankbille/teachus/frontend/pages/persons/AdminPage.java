package dk.frankbille.teachus.frontend.pages.persons;

import wicket.ResourceReference;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Resources;

public class AdminPage extends PersonPage {
	private static final long serialVersionUID = 1L;
	
	public AdminPage(Admin admin) {
		super(UserLevel.ADMIN, admin);
	}

	@Override
	protected Class<? extends PersonsPage> getPersonsPageClass() {
		return AdminsPage.class;
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.ADMIN;
	}

	@Override
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		return true;
	}
	
}
