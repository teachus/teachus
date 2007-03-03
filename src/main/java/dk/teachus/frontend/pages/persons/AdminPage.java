package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.frontend.UserLevel;

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
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		return true;
	}

	@Override
	protected PageCategory getPageCategory() {
		return AuthenticatedPageCategory.ADMINS;
	}

	protected String getPageLabel() {
		return null;
	}
	
}
