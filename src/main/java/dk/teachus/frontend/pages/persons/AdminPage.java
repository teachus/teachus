package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Admin;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.AdminPanel;
import dk.teachus.frontend.components.person.PersonPanel;

public class AdminPage extends PersonPage<Admin> {
	private static final long serialVersionUID = 1L;
	
	public AdminPage(Admin admin) {
		super(UserLevel.ADMIN, admin);
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.ADMINS;
	}

	@Override
	protected PersonPanel createPersonPanel(String wicketId, Admin admin) {
		return new AdminPanel(wicketId, admin);
	}
	
}
