package dk.teachus.frontend.components.person;

import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.frontend.pages.persons.AdminsPage;
import dk.teachus.frontend.pages.persons.PersonsPage;

public class AdminPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;

	public AdminPanel(String id, Admin admin) {
		super(id, admin);
	}
	
	@Override
	protected Class<? extends PersonsPage> getPersonsPageClass() {
		return AdminsPage.class;
	}

	@Override
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		return true;
	}

}
